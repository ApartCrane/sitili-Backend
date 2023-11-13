package mp.sitili.modules.category.use_cases.service;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService implements ICategoryRepository {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return this.categoryRepository.getAllCategory();
    }

    @Override
    public boolean createCategory(String name) {
        try {
            categoryRepository.createCategory(name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean getCategoryById(int id) {
        int revision = categoryRepository.getCategoryById(id);
        if(revision != 0){
            return true;
        }
        return false;
    }

    @Override
    public Category getNameCategoryById(int id) {
        return categoryRepository.getNameCategoryById(id);
    }

    @Override
    public boolean getStatusCategory(int id) {
        boolean revision = this.categoryRepository.getStatusCategory(id);
        if(revision){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int id, boolean status) {
        try {
            categoryRepository.deleteCategory(id, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCategory(int id, String name) {
        try {
            categoryRepository.updateCategory(id, name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
