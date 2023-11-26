package mp.sitili.modules.order.use_cases.methods;

import mp.sitili.modules.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderRepository extends JpaRepository<Order, String> {

    @Modifying
    @Query(value = "UPDATE orders SET repartidor = :repartidor, status = :status WHERE id = :id", nativeQuery = true)
    void updateDelivery(@Param("id") int id, @Param("repartidor") String repartidor, @Param("status") String status);

    @Modifying
    @Query(value = "UPDATE orders SET status = :status WHERE id = :id", nativeQuery = true)
    void updateRecive(@Param("id") int id, @Param("status") String status);
}
