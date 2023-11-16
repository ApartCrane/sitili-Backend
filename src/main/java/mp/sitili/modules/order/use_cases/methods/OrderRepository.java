package mp.sitili.modules.order.use_cases.methods;

import mp.sitili.modules.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderRepository extends JpaRepository<Order, String> {
}
