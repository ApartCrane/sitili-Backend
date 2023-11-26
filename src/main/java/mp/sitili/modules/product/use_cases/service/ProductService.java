package mp.sitili.modules.product.use_cases.service;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.repository.IProductRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService implements IProductRepository {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean saveProduct(String name, Double price, Integer stock, String features, Category category, User user){
        try{
            productRepository.saveProduct(name, price, stock, features, category, user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> findAllProducts() {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public List<Map<String, Object>> findProduct(Integer product_id) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findProduct(product_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public List<Map<String, Object>> findAllxVend(String email) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = productRepository.findAllxVend(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("cantidad", row[3]);
            productData.put("comentarios", row[4]);
            productData.put("calificacion", row[5]);
            productData.put("estado", row[6]);
            productData.put("categoria", row[7]);
            productData.put("vendedor", row[8]);
            productData.put("nombreVendedor", row[9]);
            productData.put("apellidoVendedor", row[10]);
            String imagenesConcatenadas = (String) row[11];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
    }

}
