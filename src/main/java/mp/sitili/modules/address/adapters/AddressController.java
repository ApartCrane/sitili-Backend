package mp.sitili.modules.address.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.service.AddressService;
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
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/lists")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<List<Address>> listarDireccionesxUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<Address> direcciones = addressRepository.dirXusuario(userEmail);

        if(direcciones != null){
            return new ResponseEntity<>(direcciones, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(direcciones, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<Address> listarDireccionActUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Address direccion = addressRepository.dirActXusuario(userEmail);

        if(direccion != null){
            return new ResponseEntity<>(direccion, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(direccion, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> crearDireccionxUsuarios(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        if(user != null){
            Address address1 = addressRepository.save(new Address((int) (addressRepository.count() + 1), user,
                    address.getCountry(), address.getState(), address.getCity(),
                    address.getPostalCode(), address.getMainAddress(), address.getStreetAddress1(),
                    address.getStreetAddress2(), address.getDescription()));
            if(address1 != null){
                return new ResponseEntity<>("Direccion agregada correctamente ", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al crear Direccion", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> updateDireccionxUsuarios(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        if(user != null){
            Address address1 = addressRepository.save(new Address(address.getId(), user,
                    address.getCountry(), address.getState(), address.getCity(),
                    address.getPostalCode(), address.getMainAddress(), address.getStreetAddress1(),
                    address.getStreetAddress2(), address.getDescription()));
            if(address1 != null){
                return new ResponseEntity<>("Direccion actualizada correctamente ", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al actualizar Direccion", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> deleteDireccionxUsuarios(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
        if(user != null){
            Boolean revision = addressService.deleteAddress(address.getId(), user.getEmail());
            if(revision != true){
                return new ResponseEntity<>("Direccion eliminada correctamente ", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error al aliminar Direccion", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.BAD_REQUEST);
        }

    }

}
