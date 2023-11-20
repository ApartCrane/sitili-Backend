package mp.sitili.modules.order.use_cases.service;

import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.repository.IOrederRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrederRepository {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean updateDelivery(int id, String repartidor, String status) {
        try {
            orderRepository.updateDelivery(id, repartidor, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRecive(int id, String status) {
        try {
            orderRepository.updateRecive(id, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
