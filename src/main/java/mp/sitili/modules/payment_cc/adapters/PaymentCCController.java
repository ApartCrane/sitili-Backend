package mp.sitili.modules.payment_cc.adapters;

import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.service.PaymentCCService;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
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

@Controller
@RequestMapping("/paymentcc")
public class PaymentCCController {

    @Autowired
    private PaymentCCService paymentCCService;

    @Autowired
    private PaymentCCRepository paymentCCRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<PaymentCC>> listarPagoxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<PaymentCC> pago = paymentCCService.pagoXusuario(userEmail);

        if(pago != null){
            return new ResponseEntity<>(pago, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pago, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> asociarPago(@RequestBody PaymentCC paymentCC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);

        if(user != null){
            if(paymentCC != null){
                PaymentCC pago = paymentCCRepository.save(new PaymentCC((int) (paymentCCRepository.count() + 1), user, paymentCC.getCc(), paymentCC.getDay(), paymentCC.getMonth(), paymentCC.getYear(), paymentCC.getCvv()));
                if(pago != null){
                    return new ResponseEntity<>("Datos de pago cargados exitosamente", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Error al cargar datos de pago", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Datos Faltantes", HttpStatus.NO_CONTENT);
            }
        }else {
            return new ResponseEntity<>("Usaurio inexistente", HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> actualizarPago(@RequestBody PaymentCC paymentCC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);

        if(user != null){
            if(paymentCC != null){
                PaymentCC pago = paymentCCRepository.save(new PaymentCC(paymentCC.getId(), user, paymentCC.getCc(), paymentCC.getDay(), paymentCC.getMonth(), paymentCC.getYear(), paymentCC.getCvv()));
                if(pago != null){
                    return new ResponseEntity<>("Datos de pago actualizados exitosamente", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Error al actualizar datos de pago", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Datos Faltantes", HttpStatus.NO_CONTENT);
            }
        }else {
            return new ResponseEntity<>("Usaurio inexistente", HttpStatus.NO_CONTENT);
        }
    }

}
