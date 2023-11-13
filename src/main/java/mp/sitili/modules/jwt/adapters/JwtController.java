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
    public JwtResponse registerNewUser(@RequestBody JwtRegister jwtRegister) throws Exception {
        userService.registerNewUser(jwtRegister);
        return jwtService.createJwtToken(new JwtRequest(jwtRegister.getEmail(), jwtRegister.getPassword()));
    }

}
