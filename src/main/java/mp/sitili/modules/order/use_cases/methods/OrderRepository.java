package mp.sitili.modules.order.use_cases.methods;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, String> {

    @Modifying
    @Query(value = "UPDATE orders SET repartidor = :repartidor, status = :status WHERE id = :id", nativeQuery = true)
    void updateDelivery(@Param("id") int id, @Param("repartidor") String repartidor, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE orders SET status = :status WHERE id = :id", nativeQuery = true)
    void updateRecive(@Param("id") int id, @Param("status") String status);

    @Query(value = "SELECT COUNT(o.id)\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od  ON o.id = od.order_id\n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE p.user_id = :sellerEmail && o.status IN(\"Trafico\", \"Entrega\")", nativeQuery = true)
    public Integer sellerEnvs(String sellerEmail);

    @Query(value = "SELECT SUM(od.price * od.quantity)\n" +
            "FROM orders o\n" +
            "INNER JOIN order_details od ON o.id = od.order_id\n" +
            "INNER JOIN product p ON od.product_id = p.id\n" +
            "WHERE p.user_id = :sellerEmail AND o.status IN ('Trafico', 'Entrega');", nativeQuery = true)
    public Double sellerSales(String sellerEmail);

    @Query(value = "SELECT * FROM user_id = : :userEmail", nativeQuery = true)
    public List<ShoppingCar> buscarTodo(@Param("userEmail") String userEmail);
}
