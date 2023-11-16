package mp.sitili.modules.product.use_cases.repository;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.user.entities.User;

import java.util.List;
import java.util.Map;

public interface IProductRepository {

    boolean saveProduct(String name, Double price, Integer stock, String features, Category category, User user);

    public List<Map<String, Object>> findAllProducts();

    public List<Map<String, Object>> findAllxVend(String email);

}
