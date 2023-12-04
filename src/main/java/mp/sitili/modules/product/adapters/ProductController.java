package mp.sitili.modules.product.adapters;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.service.CategoryService;
import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import mp.sitili.utils.aws.AWSS3ServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSS3ServiceImp awss3ServiceImp;

    @Autowired
    private ImageProductService imageProductService;

    @Autowired
    private RaitingRepository raitingRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/listAll")
    public ResponseEntity<List> obtenerTodoProductos() {
        List<Map<String, Object>> products = productService.findAllProducts();

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listProduct")
    public ResponseEntity<List> obtenerProducto(@RequestBody Product product) {
        List<Map<String, Object>> products = productService.findProduct(product.getId());

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totalProducts")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<Long> totalProductos() {
        Long total = productRepository.count();

        if(total == 0){
            return new ResponseEntity<>(total, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listAllVend")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List> obtenerTodoProductosxVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        List<Map<String, Object>> products = productService.findAllxVend(sellerEmail);

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totSeller")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<Integer> obtenerTotalProductosxVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        Integer contador = productService.countProSel(sellerEmail);
        System.out.println(contador);

        if(contador != 0){
            return new ResponseEntity<>(contador, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(contador, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/listSeller")
    public ResponseEntity<List> obtenerTodoProductosxVendedorPublico(@RequestBody User user) {
        String sellerEmail = user.getEmail();
        List<Map<String, Object>> products = productService.findAllxVend(sellerEmail);

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/selectSeller")
    public ResponseEntity<List<SelectVendedorDTO>> obtenerVendedores() {
        List<SelectVendedorDTO> vendedores = userService.findSellers();

        if(vendedores != null){
            return new ResponseEntity<>(vendedores, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(vendedores, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> guardarProductoConImagenes(@RequestPart("productData") Map<String, Object> productData,
                                                             @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();
        Integer contador = 0;

        if (!productData.isEmpty()) {
            String name = (String) productData.get("name");
            int stock = (int) productData.get("stock");
            System.out.println(productData.get("price"));
            String price = (String) productData.get("price");
            Double price1 = Double.valueOf(price);
            String features = (String) productData.get("features");
            int categoryId = (int) productData.get("category_id");
            Category category = categoryRepository.getCatById(categoryId);
            User user = userRepository.findById(String.valueOf(sellerEmail)).orElse(null);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp registerProduct = Timestamp.valueOf(sdf.format(timestamp));

            Product productSaved = productRepository.save(new Product(name, stock, price1, features, category, user, registerProduct, true));
            if (productSaved != null) {
                raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 0.0, productSaved, user));
                if (files != null && !files.isEmpty()) {

                    for (MultipartFile file : files) {

                        String key = awss3ServiceImp.uploadFile(file);
                        String url = awss3ServiceImp.getObjectUrl(key);
                        if(imageProductService.saveImgs(url, productSaved.getId())){
                            contador++;
                        }

                    }
                }
                return new ResponseEntity<>("Producto creado exitosamente, se cargaron " + contador + " de "+ files.size() + " imagenes correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al guardar producto", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/update")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> actualizarProductoConImagenes(@RequestPart("productData") Map<String, Object> productData,
                                                             @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();
        Integer contador = 0;

        if (!productData.isEmpty()) {
            Integer product_id = (Integer) productData.get("product_id");
            String name = (String) productData.get("name");
            int stock = (int) productData.get("stock");
            System.out.println(productData.get("price"));
            String price = (String) productData.get("price");
            Double price1 = Double.valueOf(price);
            String features = (String) productData.get("features");
            int categoryId = (int) productData.get("category_id");
            Category category = categoryRepository.getCatById(categoryId);
            User user = userRepository.findById(String.valueOf(sellerEmail)).orElse(null);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp registerProduct = Timestamp.valueOf(sdf.format(timestamp));

            Product productSaved = productRepository.save(new Product(product_id, name, stock, price1, features, category, user, registerProduct, true));
            if (productSaved != null) {

                if (files != null && !files.isEmpty()) {

                    for (MultipartFile file : files) {

                        String key = awss3ServiceImp.uploadFile(file);
                        String url = awss3ServiceImp.getObjectUrl(key);
                        if(imageProductService.saveImgs(url, productSaved.getId())){
                            contador++;
                        }

                    }
                }
                return new ResponseEntity<>("Producto creado exitosamente, se cargaron " + contador + " de "+ files.size() + " imagenes correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al guardar producto", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deleteImages")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<String> eliminarImages(@RequestPart("productData") Map<String, Object> productData) {

        if (!productData.isEmpty()) {
            Integer product_id = (Integer) productData.get("product_id");
            String image_url = (String) productData.get("image_url");

            Optional<Product> productSaved = productRepository.findById(product_id);
            if (productSaved.isPresent() && image_url != null) {

                if (imageProductService.deleteImage(image_url)) {
                    return new ResponseEntity<>("Imagen eliminada correctamente" ,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("No se elimino la imagen correctamente" ,HttpStatus.EXPECTATION_FAILED);
                }

            } else {
                return new ResponseEntity<>("El producto no existe", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Los datos del producto son inválidos", HttpStatus.BAD_REQUEST);
        }
    }

}
