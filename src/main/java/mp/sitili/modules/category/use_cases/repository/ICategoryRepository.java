package mp.sitili.modules.category.use_cases.repository;


import mp.sitili.modules.category.entities.Category;
import java.util.List;

public interface ICategoryRepository {
    List<Category> getAllCategory();

    boolean createCategory(String name);

    boolean getCategoryById(int id);

    Category getNameCategoryById(int id);

    boolean getStatusCategory(int id);

    boolean deleteCategory(int id, boolean status);

    boolean updateCategory(int id, String name);
}
