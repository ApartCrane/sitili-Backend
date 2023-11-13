package mp.sitili.modules.data_user.adapters;

import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dataUser")
public class DataUserController {

    @Autowired
    private DataUserRepository dataUserRepository;


    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<DataUserDTO>> datosPorUsuario() {
        List<DataUserDTO> usuarios = dataUserRepository.findAllDataUsers();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }

    /*
    @PostMapping("/save")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> guardarPorUsuario() {
        List<User> usuarios = dataUserRepository.findAll();

        if(usuarios != null){
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(usuarios, HttpStatus.BAD_REQUEST);
        }
    }
     */

}
