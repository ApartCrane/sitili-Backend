package mp.sitili.modules.order.use_cases.service;

import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.repository.IOrederRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrederRepository {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

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


    @Override
    public Integer sellerEnvs(String sellerEmail){
        return orderRepository.sellerEnvs(sellerEmail);
    }

    @Override
    public Double sellerSales(String sellerEmail){
        return orderRepository.sellerSales(sellerEmail);
    }

    @Override
    public List<ShoppingCar> buscarTodo(String userEmail){
        return orderRepository.buscarTodo(userEmail);
    }

}
