package mp.sitili.modules.category.use_cases.repository;


import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.dto.ProductosxCategoriaDTO;

import java.util.List;
import java.util.Map;

public interface ICategoryRepository {

    List<Category> findAllStatus();

    List<Category> categoriasNombre(String name);

    boolean createCategory(String name);

    boolean getCategoryById(int id);

    Category getNameCategoryById(int id);

    boolean getStatusCategory(int id);

    boolean deleteCategory(int id, boolean status);

    boolean updateCategory(int id, String name);

    public List<ProductosxCategoriaDTO> proXcat();

    public List<ProductosxCategoriaDTO> proXcatSell(String sellerEmail);

    public List<Map<String, Object>> findAllProducts(Integer id);
}
