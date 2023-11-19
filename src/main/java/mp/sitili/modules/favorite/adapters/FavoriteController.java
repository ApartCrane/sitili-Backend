package mp.sitili.modules.favorite.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.favorite.use_cases.service.FavoriteService;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<Favorite>> listarFavxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<Favorite> favoritos = favoriteService.favXusuario(userEmail);

        if(favoritos != null){
            return new ResponseEntity<>(favoritos, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(favoritos, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> addFavxUsuarios(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(product.getId());

        if(user != null && producto.isPresent()){
            Favorite favorite = favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user, producto.get()));
            if(favorite != null){
                return new ResponseEntity<>("Agregado a favoritos", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al agregar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> delFavxUsuarios(@RequestBody Favorite favorite) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        Optional<Product> producto = productRepository.findById(favorite.getId());

        if(user != null && producto.isPresent()){
            boolean revision = favoriteService.deleteFav(user.getEmail(), producto.get().getId());
            if(revision == true){
                return new ResponseEntity<>("Eliminado de favoritos", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al eliminar", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Prodcuto no encontrado", HttpStatus.BAD_REQUEST);
        }

    }


}
