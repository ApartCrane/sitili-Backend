package mp.sitili.modules.order.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.service.AddressService;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order.use_cases.service.OrderService;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.service.OrderDetailService;
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

import java.security.PrivateKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/create")
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

        if(user != null){
            if(producto.isPresent() && producto.get().getStatus()){
                if(producto.get().getStock() > 0 && (Integer) productData.get("quantity") > 0 && (Integer) productData.get("quantity") < producto.get().getStock()){
                    Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
                    if(address != null){
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));
                        Order orden = orderRepository.save(new Order((int) orderRepository.count() + 1 , user, "Pendiente","No asignado", address, dateOrder));
                        if(orden != null){
                            OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden, producto.get(), (Integer) productData.get("quantity"), producto.get().getPrice()));
                            if(orderDetail != null){
                                return new ResponseEntity<>("Orden creada con exito, N. Orden " + orden.getId(), HttpStatus.OK);
                            }else{
                                return new ResponseEntity<>("Error al crear OrdenDetail", HttpStatus.BAD_REQUEST);
                            }
                        }else{
                            return new ResponseEntity<>("Error al crear Orden", HttpStatus.BAD_REQUEST);
                        }
                    }else{
                        return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return new ResponseEntity<>("Cantidad no aceptable", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Producto no disponible para compra", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create2")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> crearOrden2(@RequestPart("productData") Map<String, Object> productData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        //product_id
        //quantity
        //address_id
        //user_id

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById((Integer) productData.get("product_id"));

        if(user != null){
            if(producto.isPresent() && producto.get().getStatus()){
                if(producto.get().getStock() > 0 && (Integer) productData.get("quantity") > 0 && (Integer) productData.get("quantity") < producto.get().getStock()){
                    Address address = addressService.buscarDir((Integer) productData.get("address_id"), user.getEmail());
                    if(address != null){
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));
                        Order orden = orderRepository.save(new Order((int) orderRepository.count() + 1 , user, "Pendiente","No asignado", address, dateOrder));
                        if(orden != null){
                            OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden, producto.get(), (Integer) productData.get("quantity"), producto.get().getPrice()));
                            if(orderDetail != null){
                                return new ResponseEntity<>("Orden creada con exito, N. Orden " + orden.getId(), HttpStatus.OK);
                            }else{
                                return new ResponseEntity<>("Error al crear OrdenDetail", HttpStatus.BAD_REQUEST);
                            }
                        }else{
                            return new ResponseEntity<>("Error al crear Orden", HttpStatus.BAD_REQUEST);
                        }
                    }else{
                        return new ResponseEntity<>("Direccion no valida", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return new ResponseEntity<>("Cantidad no aceptable", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Producto no disponible para compra", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.BAD_REQUEST);
        }
    }

}
