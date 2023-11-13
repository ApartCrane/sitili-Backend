package mp.sitili.modules.category.use_cases.methods;

import mp.sitili.modules.category.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    public List<Category> getAllCategory();

    @Query(value = "SELECT id FROM categories WHERE id = :id", nativeQuery = true)
    public Integer getCategoryById(@Param("id") int id);

    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    public Category getCatById(@Param("id") int id);

    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    public Category getNameCategoryById(@Param("id") int id);

    @Query(value = "SELECT status FROM categories WHERE id = :id", nativeQuery = true)
    public boolean getStatusCategory(@Param("id") int id);

    @Modifying
    @Query(value = "INSERT INTO categories (name, status) VALUES (:name, true)", nativeQuery = true)
    void createCategory(@Param("name") String name);

    @Modifying
    @Query(value = "UPDATE categories SET status = :status WHERE id = :id", nativeQuery = true)
    void deleteCategory(@Param("id") int id, @Param("status") boolean status);

    @Modifying
    @Query(value = "UPDATE categories SET name = :name WHERE id = :id", nativeQuery = true)
    void updateCategory(@Param("id") int id, @Param("name") String name);


}
