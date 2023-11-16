package mp.sitili.modules.order.adapters;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasLasOrdenes() {
        List<Order> ordenes = orderRepository.findAll();

        if(ordenes != null){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

}
