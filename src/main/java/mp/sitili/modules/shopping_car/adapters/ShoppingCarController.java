package mp.sitili.modules.shopping_car.adapters;

import mp.sitili.modules.favorite.entities.Favorite;
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
import java.util.Map;
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
    public ResponseEntity<List> listarCarxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Map<String, Object>> favoritos = shoppingCarService.carXusuario(userEmail);

        if(favoritos != null){
            return new ResponseEntity<>(favoritos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(favoritos, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuarios(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        ShoppingCar shopp = shoppingCarService.validarExis(producto.get().getId(), userEmail);

        if(shopp == null){
            if(user != null && producto.isPresent()){
                if(product.getStock() < producto.get().getStock() && product.getStock() > 0){
                    ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), product.getStock()));
                    if(shoppingCar != null){
                        return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }else{
                    return new ResponseEntity<>("Cantidad excedente", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Prodcuto repetido", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/createF")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addCarxUsuariosf(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        ShoppingCar shopp = shoppingCarService.validarExis(producto.get().getId(), userEmail);

        if(shopp == null){
            if(user != null && producto.isPresent()){
                    ShoppingCar shoppingCar = shoppingCarRepository.save(new ShoppingCar((int) shoppingCarRepository.count() + 1,user , producto.get(), 1));
                    if(shoppingCar != null){
                        return new ResponseEntity<>("Agregado a carrito de compras", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
            }else{
                return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("Prodcuto repetido", HttpStatus.BAD_REQUEST);
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
