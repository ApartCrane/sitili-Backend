package mp.sitili.modules.jwt.adapters;

import mp.sitili.modules.jwt.entities.JwtRegister;
import mp.sitili.modules.jwt.entities.JwtRequest;
import mp.sitili.modules.jwt.entities.JwtResponse;
import mp.sitili.modules.jwt.use_cases.service.JwtService;
import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.methods.RoleRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import mp.sitili.utils.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping({"/authenticate"})
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse validador = jwtService.createJwtToken(jwtRequest);
        if(validador.getUser().getStatus()){
            return new ResponseEntity<>(validador, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping({"/registerNewUser"})
    public ResponseEntity<JwtResponse> registerNewUser(@RequestBody Map<String, Object> productData) throws Exception {
        if(!productData.isEmpty()){
            String email = (String) productData.get("email");
            String password = (String) productData.get("password");
            String first_name = (String) productData.get("first_name");
            String last_name = (String) productData.get("last_name");
            Integer role = (Integer) productData.get("role");

            User user = userService.registerNewUser(email, password, first_name, last_name, role);
            if(user != null){
                JwtResponse validador = jwtService.createJwtToken(new JwtRequest(email, password));
                if(validador != null){
                    System.out.println("Hasta Aca cool");
                    return new ResponseEntity<>(validador, HttpStatus.OK);
                }else{
                    System.out.println("Erro JWT");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }else{
                System.out.println("Error creandolo");
                return new ResponseEntity<>(HttpStatus.SEE_OTHER);
            }
        }else{
            System.out.println("Data vacia");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
