package mp.sitili.modules.order_detail.use_cases.methods;

import mp.sitili.modules.order_detail.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
