package mp.sitili.modules.acept_seller.adapters;

import mp.sitili.modules.acept_seller.entities.AceptSeller;
import mp.sitili.modules.acept_seller.use_cases.dto.AceptSellerDTO;
import mp.sitili.modules.acept_seller.use_cases.methods.AceptSellerRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
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
@RequestMapping("/aceptSeller")
public class AceptSellerController {

    @Autowired
    private AceptSellerRepository aceptSellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/listSellersNa")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<AceptSellerDTO>> obtenerVendedoresNa() {
        List<AceptSellerDTO> usuarios = aceptSellerRepository.findAllSellersNa();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/acept")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> aceptarVendedor(@RequestBody User data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        User user = userRepository.findById(data.getEmail()).get();

        if (user != null) {
            AceptSeller aceptSeller = new AceptSeller();
            aceptSeller.setAdmin(userRepository.findById(adminEmail).orElse(null));
            aceptSeller.setSeller(user);
            aceptSellerRepository.save(aceptSeller);
            userRepository.bajaLogica(user.getEmail(), true);
            userService.sendEmail(user.getEmail(), "Aceptado como Vendedor", adminEmail + " te acepto como vendedor en SITILI");
            return new ResponseEntity<>("Usuario aceptado como vendedor", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

}
