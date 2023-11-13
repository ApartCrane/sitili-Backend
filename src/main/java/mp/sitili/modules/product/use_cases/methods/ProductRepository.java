package mp.sitili.modules.product.use_cases.methods;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;
import mp.sitili.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT p.name AS producto, p.price AS precio, p.stock AS cantidad, p.features AS comentarios, avg(r.raiting) AS califiaccion , p.status AS estado, c.name AS categoria, u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "INNER JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "WHERE p.status = true\n" +
            "GROUP BY p.id ", nativeQuery = true)
    public List<ProductDTO> findAllByStatusEquals();

    @Query(value = "SELECT p.name AS producto, p.price AS precio, p.stock AS cantidad, p.features AS comentarios, avg(r.raiting) AS califiaccion , p.status AS estado, c.name AS categoria, u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "INNER JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "GROUP BY p.id", nativeQuery = true)
    public List<ProductDTO> findAllProducts();

    @Query(value = "SELECT p.name AS producto, p.price AS precio, p.stock AS cantidad, p.features AS comentarios, avg(r.raiting) AS califiaccion , p.status AS estado, c.name AS categoria, u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "INNER JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "WHERE p.user_id LIKE \"%:email%\"\n" +
            "GROUP BY p.id ", nativeQuery = true)
    public List<ProductDTO> findAllxVend(@Param("email") String email);

    @Query(value = "SELECT p.name AS producto, p.price AS precio, p.stock AS cantidad, p.features AS comentarios, avg(r.raiting) AS califiaccion , p.status AS estado, c.name AS categoria, u.email AS vendedor, du.first_name AS nombreVendedor, du.last_name AS apellidoVendedor\n" +
            "FROM product p\n" +
            "INNER JOIN categories c ON p.category_id = c.id\n" +
            "INNER JOIN users u ON u.email = p.user_id\n" +
            "INNER JOIN raiting r ON p.id = r.product_id\n" +
            "INNER JOIN data_users du ON u.email = du.user_id\n" +
            "WHERE p.user_id LIKE \"%:email%\" && p.status = true\n" +
            "GROUP BY p.id ", nativeQuery = true)
    public List<ProductDTO> findAllUpxVend(@Param("email") String email);

    @Modifying
    @Query(value = "INSERT INTO product(name, price, stock, features, status, category_id, user_id) " +
            "VALUES(:name, :price, :stock, :features, true, :category, :user)", nativeQuery = true)
    void saveCategory(@Param("name") String name,
                      @Param("price") Double price,
                      @Param("stock") Integer stock,
                      @Param("features") String features,
                      @Param("category") Category category,
                      @Param("user") User user );


}
