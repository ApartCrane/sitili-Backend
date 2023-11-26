package mp.sitili.modules.order_detail.adapters;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasLasOrdenDetails() {
        List<OrderDetail> ordenes = orderDetailRepository.findAll();

        if(ordenes != null){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }
}
