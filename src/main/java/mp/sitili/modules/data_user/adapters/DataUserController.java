package mp.sitili.modules.data_user.adapters;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.data_user.use_cases.service.DataUserService;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dataUser")
public class DataUserController {

    @Autowired
    private DataUserRepository dataUserRepository;

    @Autowired
    private DataUserService dataUserService;


    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<DataUserDTO>> datosUsuarios() {
        List<DataUserDTO> usuarios = dataUserRepository.findAllDataUsers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listu")
    @PreAuthorize("hasRole('Admin') or hasRole('Seller') or hasRole('User')")
    public ResponseEntity<DataUserDTO> datosPorUsuario() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataUserDTO usuarios = dataUserRepository.findAllDataUser(userEmail);

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin') or hasRole('Seller') or hasRole('User')")
    public ResponseEntity<DataUser> actualizarPorUsuario(@RequestBody DataUser dataUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataUser usuarios = dataUserRepository.save(dataUser);

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateCompany")
    @PreAuthorize("hasRole('Admin') or hasRole('Seller')")
    public ResponseEntity<String> actualizarCompany(@RequestBody DataUser dataUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        boolean revision = dataUserService.setCompany(userEmail, dataUser.getCompany(), dataUser.getPhone());

        if(revision){
            return new ResponseEntity<>("Compañia asociar correctamente", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Error al asociar compañia", HttpStatus.BAD_REQUEST);
        }
    }

}
