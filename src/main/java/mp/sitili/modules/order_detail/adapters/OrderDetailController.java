package mp.sitili.modules.order_detail.adapters;

import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.dto.DetailsDTO;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

        if(!ordenes.isEmpty()){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listDetails")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<DetailsDTO>> obtenerDetallesOrden(@RequestBody Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<DetailsDTO> detalles = orderDetailRepository.details(userEmail, order.getId());

        if(!detalles.isEmpty()){
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(detalles, HttpStatus.BAD_REQUEST);
        }
    }
}
