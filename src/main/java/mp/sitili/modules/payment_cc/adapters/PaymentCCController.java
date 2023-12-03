package mp.sitili.modules.payment_cc.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.dto.PaymentDTO;
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
import java.util.Map;

@Controller
@RequestMapping("/paymentcc")
public class PaymentCCController {

    @Autowired
    private PaymentCCService paymentCCService;

    @Autowired
    private PaymentCCRepository paymentCCRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/lists")
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

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<PaymentCC> listarUnaTarjeta() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        PaymentCC pago = paymentCCService.tarjetaXusuario(userEmail);

        if(pago != null){
            return new ResponseEntity<>(pago, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pago, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> asociarPago(@RequestBody PaymentCC paymentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(userEmail).orElse(null);

        if (user != null && paymentDTO != null) {
            String cc = paymentDTO.getCc();
            String cvv = paymentDTO.getCvv();
            String expiryDate = paymentDTO.getExpiryDate();
            String month = expiryDate.substring(0, 1);
            String year = expiryDate.substring(3, 4);

            System.out.println(cc);
            System.out.println(cvv);
            System.out.println(expiryDate);
            System.out.println(month);
            System.out.println(year);

            PaymentCC pago = paymentCCRepository.save(new PaymentCC((int) (paymentCCRepository.count() + 1), user, cc, month, year, cvv));

            if (pago != null) {
                return new ResponseEntity<>("Datos de pago cargados exitosamente: " + pago.getId(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al cargar datos de pago", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Usuario inexistente o datos faltantes", HttpStatus.NO_CONTENT);
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
                PaymentCC pago = paymentCCRepository.save(new PaymentCC(paymentCC.getId(), user, paymentCC.getCc(), paymentCC.getMonth(), paymentCC.getYear(), paymentCC.getCvv()));
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
