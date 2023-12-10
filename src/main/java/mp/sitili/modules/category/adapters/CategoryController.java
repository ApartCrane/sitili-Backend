package mp.sitili.modules.category.adapters;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.dto.ProductosxCategoriaDTO;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasCategorias() {
        List<Category> categories = categoryRepository.findAll();

        if(categories != null){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(categories, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<List> obtenerTodasCategoriasFree() {
        List<Category> categories = categoryService.findAllStatus();

        if(categories != null){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(categories, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listCategorie")
    public ResponseEntity<List> obtenerCategoriaFree(@RequestBody Category category) {
        List<Category> categories = categoryService.categoriasNombre(category.getName());

        if(categories != null){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(categories, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/proxcat")
    public ResponseEntity<List> productosPorCategoria(@RequestBody Category category) {
        List<Map<String, Object>> categories = categoryService.findAllProducts(category.getId());

        if(categories != null){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(categories, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        Category cat = categoryRepository.save(new Category((int) categoryRepository.count() + 1, category.getName(), true));
        if(cat != null){
            return new ResponseEntity<>("Categoria creada exitosamente", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Error al crear Categoria", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<String> actualizarCategory(@RequestBody Category category) {
        if(categoryService.getCategoryById(category.getId())){
            if(categoryService.updateCategory(category.getId(), category.getName())){
                return new ResponseEntity<>("Categoria Actualizada exitosamente", HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>("Error al actualizar Categoria", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Categoria no Encontrada", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<String> eliminarCategory(@RequestBody Category category) {
        if (categoryService.getStatusCategory(category.getId())) {
            categoryService.deleteCategory(category.getId(), false);
            return new ResponseEntity<>("Categoria dada de baja exitosamente", HttpStatus.OK);
        } else {
            categoryService.deleteCategory(category.getId(), true);
            return new ResponseEntity<>("Categoria dada de alta exitosamente", HttpStatus.OK);
        }
    }

    @GetMapping("/catTot")
    @PreAuthorize("hasRole('Root') or hasRole('Admin')")
    public ResponseEntity<List<ProductosxCategoriaDTO>> productosPorCategoria() {
        List<ProductosxCategoriaDTO> productos = categoryService.proXcat();

        if(productos != null){
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(productos, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/catTotSeller")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<List<ProductosxCategoriaDTO>>  productosPorCategoriaSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        List<ProductosxCategoriaDTO>  productos = categoryService.proXcatSell(adminEmail);

        if(productos != null){
            return new ResponseEntity<>(productos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(productos, HttpStatus.NO_CONTENT);
        }
    }
}
