package mp.sitili.modules.payment_order.adapter;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.service.AddressService;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.service.OrderDetailService;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.service.PaymentCCService;
import mp.sitili.modules.payment_order.entities.PaymentOrder;
import mp.sitili.modules.payment_order.use_cases.http.PaymentIntentDto;
import mp.sitili.modules.payment_order.use_cases.methods.PaymentOrderRepositry;
import mp.sitili.modules.payment_order.use_cases.service.PaymentService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.shopping_car.use_cases.service.ShoppingCarService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentCCRepository paymentCCRepository;

    @Autowired
    private PaymentOrderRepositry paymentOrderRepositry;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private PaymentCCService paymentCCService;

    /*
     * @PostMapping("/paymentintent")
     * public ResponseEntity<String> payment(@RequestBody PaymentIntentDto
     * paymentIntentDto) throws StripeException {
     * PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/paymentintent")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> payment(@RequestBody List<PaymentIntentDto> paymentIntentDtoList)
            throws StripeException {
        List<PaymentIntent> paymentIntents = paymentService.paymentIntents(paymentIntentDtoList);
        List<String> paymentStrList = paymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    @PostMapping("paymentStripe")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> pagoStripe() throws StripeException{
        return new ResponseEntity<>("Hola", HttpStatus.OK);
    }

    /*
     * @PostMapping("/confirm/{id}")
     * public ResponseEntity<String> confirm(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.confirm(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/confirm")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> confirm(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> confirmedPaymentIntents = paymentService.confirmPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = confirmedPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    /*
     * @PostMapping("/cancel/{id}")
     * public ResponseEntity<String> cancel(@PathVariable("id") String id) throws
     * StripeException {
     * PaymentIntent paymentIntent = paymentService.cancel(id);
     * String paymentStr = paymentIntent.toJson();
     * return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
     * }
     */
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> cancel(@RequestBody List<String> paymentIntentIds) throws StripeException {
        List<PaymentIntent> cancelledPaymentIntents = paymentService.cancelPaymentIntents(paymentIntentIds);
        List<String> paymentStrList = cancelledPaymentIntents.stream()
                .map(PaymentIntent::toJson)
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentStrList.toString(), HttpStatus.OK);
    }

    @PostMapping("/saleCar")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> comprarCarrito(@RequestBody PaymentCC paymentCC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findById(userEmail).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> carrito = orderService.buscarTodo(userEmail);
        Address direccion = addressService.buscarDir(paymentCC.getAddress_id(), userEmail);
        PaymentCC pago = paymentCCService.findById((Integer) paymentCC.getCc_id());

        if (carrito.isEmpty()) {
            return new ResponseEntity<>("El carrito está vacío", HttpStatus.BAD_REQUEST);
        }

        if (direccion == null) {
            return new ResponseEntity<>("Dirección no encontrada", HttpStatus.NOT_FOUND);
        }

        if (pago == null) {
            return new ResponseEntity<>("Forma de pago no asociada", HttpStatus.NOT_FOUND);
        }

        Order orden = new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", direccion, new Timestamp(System.currentTimeMillis()));
        orden = orderRepository.save(orden);

        List<OrderDetail> validOrderDetails = new ArrayList<>();
        List<Product> bajarCantidades = new ArrayList<>();

        for (Map<String, Object> orderDetailData : carrito) {
            Integer productId = (Integer) orderDetailData.get("product_id");
            Integer quantity = (Integer) orderDetailData.get("quantity");

            Optional<Product> productoDetail = productRepository.findById(productId);
            if (productoDetail.isPresent() && productoDetail.get().getStatus()) {
                if (productoDetail.get().getStock() > 0 && quantity > 0 && quantity <= productoDetail.get().getStock()) {
                    OrderDetail orderDetail = new OrderDetail(null, orden, productoDetail.get(), quantity, productoDetail.get().getPrice());
                    validOrderDetails.add(orderDetail);
                    productoDetail.get().setStock(productoDetail.get().getStock() - quantity);
                    bajarCantidades.add(productoDetail.get());
                } else {
                    orderRepository.delete(orden);
                    return new ResponseEntity<>("La cantidad excede el stock disponible para el producto: " + productoDetail.get().getName(), HttpStatus.BAD_REQUEST);
                }
            } else {
                orderRepository.delete(orden);
                return new ResponseEntity<>("Producto no encontrado o no disponible", HttpStatus.BAD_REQUEST);
            }
        }

        // Guardar detalles de orden, actualizar cantidades y limpiar carrito
        orderDetailRepository.saveAll(validOrderDetails);
        productRepository.saveAll(bajarCantidades);
        shoppingCarRepository.deleteAllCar(userEmail);
        paymentOrderRepositry.save(new PaymentOrder(paymentCCRepository.count() + 1, orden, pago));

        return new ResponseEntity<>("Compra realizada con éxito", HttpStatus.OK);
    }

}