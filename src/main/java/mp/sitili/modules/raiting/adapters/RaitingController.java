package mp.sitili.modules.raiting.adapters;

import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/raiting")
public class RaitingController {

    @Autowired
    private RaitingRepository raitingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/rate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> calificarProducto(@RequestBody Raiting raiting) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> product = productRepository.findById(raiting.getId());
        Raiting rate = raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, raiting.getRaiting(), product.get(), user));

        if(rate != null && user != null && product.isPresent()){
            return new ResponseEntity<>("Producto valorado", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error al valorar producto", HttpStatus.BAD_REQUEST);
        }
    }
}
