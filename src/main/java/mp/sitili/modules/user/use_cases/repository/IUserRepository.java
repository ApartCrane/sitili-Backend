package mp.sitili.modules.user.use_cases.repository;


import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface IUserRepository {

    String sendEmail(String username, String title, String bob);

    boolean bajaLogica(String email, boolean status);

}
