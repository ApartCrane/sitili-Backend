package mp.sitili.modules.user.adapters;

import mp.sitili.modules.jwt.entities.JwtRegister;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodosUsuarios() {
        List<User> usuarios = userRepository.findAll();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAdmins")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerAdministradores() {
        List<User> usuarios = userRepository.findAllAdmins();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listSellers")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerVendedores() {
        List<User> usuarios = userRepository.findAllSellers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listUsers")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerUsuarios() {
        List<User> usuarios = userRepository.findAllUsers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> obtenerUsuarios(@RequestBody JwtRegister jwtRegister) {
        Optional<User> usuarios = userRepository.findById(jwtRegister.getEmail());

        if(usuarios.isPresent()){
            usuarios.get().setPassword(passwordEncoder.encode(jwtRegister.getPassword()));
            userRepository.save(usuarios.get());
            return new ResponseEntity<>("Contraseña Actualizada", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Usuario no Encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> obtenerUsuarios(@RequestBody String email) {
        Optional<User> usuario = userRepository.findById(email);
        if(usuario.isPresent()){
            if(usuario.get().getStatus()){
                userRepository.bajaLogica(usuario.get().getEmail(), false);
                return new ResponseEntity<>("Usuario dado de Baja", HttpStatus.OK);
            }else{
                userRepository.bajaLogica(usuario.get().getEmail(), true);
                return new ResponseEntity<>("Usuario dado de Alta", HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
