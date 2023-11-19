package mp.sitili.modules.shopping_car.adapters;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shoppingCar")
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<ShoppingCar>> listarCarxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<ShoppingCar> carrito = shoppingCarService.carXusuario(userEmail);

        if(carrito != null){
            return new ResponseEntity<>(carrito, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(carrito, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuarios(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        if(user != null && producto.isPresent() && product.getStock() > producto.get().getStock() && product.getStock() > 0){
            ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), product.getStock()));
            if(shoppingCar != null){
                return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al agregar o Cantidad excedente", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> delCarxUsuarios(@RequestBody ShoppingCar shoppingCar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(shoppingCar.getProduct().getId());

        if(user != null && producto.isPresent()){
            boolean revision = shoppingCarService.deleteCar(user.getEmail(), producto.get().getId());
            if(revision == true){
                return new ResponseEntity<>("Eliminado de carrito de compras", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
        }

    }
}
