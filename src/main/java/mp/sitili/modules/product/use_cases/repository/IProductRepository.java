package mp.sitili.modules.product.use_cases.repository;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.user.entities.User;

public interface IProductRepository {

    boolean saveCategory(String name, Double price, Integer stock, String features, Category category, User user);

}
