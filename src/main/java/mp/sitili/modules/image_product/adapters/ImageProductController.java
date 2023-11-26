package mp.sitili.modules.image_product.adapters;

import mp.sitili.modules.image_product.entities.ImageProduct;
import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.jwt.entities.JwtRequest;
import mp.sitili.modules.jwt.entities.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imageProduct")
public class ImageProductController {

    @Autowired
    private ImageProductService imageProductService;

    /*
    @DeleteMapping({"/delete"})
    public ResponseEntity<JwtResponse> borrarImagen(@RequestBody ImageProduct imageProduct) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        boolean revision = imageProductService.deleteImage(imageProduct.getId(), imageProduct.getImageUrl());

        if(validador.getUser().getStatus()){
            return new ResponseEntity<>(validador, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

     */
}
