package mp.sitili.modules.order.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.service.AddressService;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.dto.PedidosAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasDTO;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.service.OrderDetailService;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_order.entities.PaymentOrder;
import mp.sitili.modules.payment_order.use_cases.methods.PaymentOrderRepositry;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

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

    @GetMapping("/deliveryStart")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> contarTodasLasOrdenes() {
        List<Order> ordenes = orderRepository.findAll();

        if(ordenes != null){
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ordenes, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/saleAll")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<VentasDTO> totalVentas() {
        VentasDTO ventas = orderDetailService.totalVentas();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<VentasAnualesDTO>> totalVentasAnuales() {
        List<VentasAnualesDTO> ventas = orderDetailService.totalVentasAnuales();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/salesPack")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<PedidosAnualesDTO>> totalPedidosAnuales() {
        List<PedidosAnualesDTO> ventas = orderDetailService.totalPedidosAnuales();

        if(ventas != null){
            return new ResponseEntity<>(ventas, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(ventas, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createOne")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> crearOrden(@RequestPart("productData") Map<String, Object> productData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        //product_id
        //quantity
        //address_id
        //user_id

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById((Integer) productData.get("product_id"));

        if (user != null && producto.isPresent() && producto.get().getStatus()) {
            if (producto.get().getStock() > 0 && (Integer) productData.get("quantity") > 0 && (Integer) productData.get("quantity") < producto.get().getStock()) {
                Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
                if (address != null) {
                    Optional<PaymentCC> paymentCC = paymentCCRepository.findById(String.valueOf(productData.get("paymentCC_id")));
                    if (paymentCC.isPresent()) {
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));

                        Order orden = orderRepository.save(new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", address, dateOrder));
                        if (orden != null) {
                            OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden, producto.get(), (Integer) productData.get("quantity"), producto.get().getPrice()));
                            if (orderDetail != null) {
                                return new ResponseEntity<>("Orden creada con exito, N. Orden " + orden.getId(), HttpStatus.OK);
                            } else {
                                orderRepository.delete(orden);
                                return new ResponseEntity<>("Error al crear OrdenDetail / Falta PAGO", HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            return new ResponseEntity<>("Error al crear Orden", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>("Forma de pago no encontrada", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Cantidad no aceptable", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Usuario o producto no válidos", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createMany")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> crearOrden2(
            @RequestPart("productData") Map<String, Object> productData,
            @RequestPart("orderDetails") List<Map<String, Object>> orderDetailsData
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        if (user != null) {
            Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
            if (address != null) {
                Optional<PaymentCC> paymentCC = paymentCCRepository.findById(String.valueOf(productData.get("paymentCC_id")));
                if (paymentCC.isPresent()) {
                    Order orden = new Order((int) orderRepository.count() + 1, user, "Pendiente", "No asignado", address, new Timestamp(System.currentTimeMillis()));
                    orden = orderRepository.save(orden);

                    List<OrderDetail> validOrderDetails = new ArrayList<>();

                    for (Map<String, Object> orderDetailData : orderDetailsData) {
                        Integer productId = (Integer) orderDetailData.get("product_id");
                        Integer quantity = (Integer) orderDetailData.get("quantity");

                        Optional<Product> productoDetail = productRepository.findById(productId);
                        if (productoDetail.isPresent() && productoDetail.get().getStatus()) {
                            if (productoDetail.get().getStock() > 0 && quantity > 0 && quantity <= productoDetail.get().getStock()) {
                                OrderDetail orderDetail = new OrderDetail(null, orden, productoDetail.get(), quantity, productoDetail.get().getPrice());
                                validOrderDetails.add(orderDetail);
                            } else {
                                orderRepository.delete(orden);
                                return new ResponseEntity<>("Cantidad requerida excede el stock, el producto es: " + productoDetail.get().getName(), HttpStatus.BAD_REQUEST);
                            }
                        } else {
                            orderRepository.delete(orden);
                            return new ResponseEntity<>("Producto no encontrado o no disponible", HttpStatus.BAD_REQUEST);
                        }
                    }

                    orderDetailRepository.saveAll(validOrderDetails);

                    return new ResponseEntity<>("Orden creada con éxito, N. Orden " + orden.getId(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Forma de pago no encontrada", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delivery")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> asignarRepartidor(@RequestBody Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        Optional<Order> orden = orderRepository.findById(String.valueOf(order.getId()));
        if (user != null && orden.isPresent()) {
            boolean revision = orderService.updateDelivery(orden.get().getId(), order.getRepartidor(), "Trafico");
            if(revision){
                return new ResponseEntity<>("Repartidor Actualizado", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>("Error al asignar repartidor", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/recive")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> pedidoEntregado(@RequestBody Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);
        Optional<Order> orden = orderRepository.findById(String.valueOf(order.getId()));
        if (user != null && orden.isPresent()) {
            boolean revision = orderService.updateRecive(orden.get().getId(), "Entregado");
            if(revision){
                return new ResponseEntity<>("Estado Actualizado", HttpStatus.BAD_REQUEST);
            }else{
                return new ResponseEntity<>("Error al asignar estado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}