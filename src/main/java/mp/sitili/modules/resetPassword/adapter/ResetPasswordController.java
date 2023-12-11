package mp.sitili.modules.resetPassword.adapter;

import mp.sitili.modules.resetPassword.entities.PasswordResetToken;
import mp.sitili.modules.resetPassword.use_cases.service.PasswordResetTokenService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.service.UserService;
import mp.sitili.utils.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/resetPassword")
public class ResetPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/request")
    public ResponseEntity<String> resetPasswordRequest(@RequestParam("email") String userEmail) {
        try {
            User user = userRepository.findById(String.valueOf(userEmail)).orElse(null);
            if (user != null) {
                PasswordResetToken token = tokenService.createToken(user);
                emailService.sendPasswordResetEmail(user, token.getToken());
                return ResponseEntity.ok("Solicitud de restablecimiento de contraseña exitosa. Revise su correo electrónico.");
            } else {
                return ResponseEntity.badRequest().body("Correo electrónico no registrado.");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Correo electrónico no encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }
    @PostMapping("/confirm")
    public ResponseEntity<String> resetPasswordConfirm(@RequestParam("token") String token,
                                                       @RequestParam("newPassword") String newPassword) {
        try {
            PasswordResetToken resetToken = tokenService.findByToken(token);
            if (resetToken != null && !resetToken.isExpired()) {
                User user = resetToken.getUser();
                userService.updatePassword(user, newPassword);
                return ResponseEntity.ok("Contraseña restablecida exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Token inválido o expirado.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Agrega este logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }
}