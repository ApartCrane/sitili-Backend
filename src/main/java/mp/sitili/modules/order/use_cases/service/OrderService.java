package mp.sitili.modules.order.use_cases.service;

import mp.sitili.modules.order.use_cases.dto.EntregasMesDTO;
import mp.sitili.modules.order.use_cases.dto.OrdersDTO;
import mp.sitili.modules.order.use_cases.dto.VentasMesDTO;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.repository.IOrederRepository;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> buscarTodo(String userEmail) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = orderRepository.buscarTodo(userEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("id", row[0]);
            productData.put("user_id", row[1]);
            productData.put("product_id", row[2]);
            productData.put("quantity", row[3]);
            productList.add(productData);
        }
        return productList;
    }

    @Override
    public List<VentasMesDTO> ventasAnioMonth(String sellerEmail){
        return orderRepository.ventasAnioMonth(sellerEmail);
    }

    @Override
    public List<EntregasMesDTO>  enviosAnioMonth(String sellerEmail){
        return orderRepository.enviosAnioMonth(sellerEmail);
    }

    @Override
    public List<OrdersDTO> ordersPerUser(String userEmail){
        return orderRepository.ordersPerUser(userEmail);
    }

}
