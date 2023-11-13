package mp.sitili.modules.product.use_cases.service;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.repository.IProductRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductRepository {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean saveCategory(String name, Double price, Integer stock, String features, Category category, User user){
        try{
            productRepository.saveCategory(name, price, stock, features, category, user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
