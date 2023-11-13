package mp.sitili.modules.user.use_cases.service;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.jwt.entities.JwtRegister;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.methods.RoleRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.repository.IUserRepository;
import mp.sitili.utils.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService implements IUserRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DataUserRepository dataUserRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RaitingRepository raitingRepository;

    public void initRoleAndUser() {

        Role rootRole = new Role();
        rootRole.setRoleName("Root");
        rootRole.setRoleDescription("Root role");
        roleRepository.save(rootRole);

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("User role");
        roleRepository.save(userRole);

        Role vendedorRole = new Role();
        vendedorRole.setRoleName("Seller");
        vendedorRole.setRoleDescription("Seller role");
        roleRepository.save(vendedorRole);


        User rootUser = new User();
        rootUser.setEmail("root@root");
        rootUser.setPassword(getEncodedPassword("root"));
        rootUser.setStatus(true);
        Set<Role> rootRoles = new HashSet<>();
        rootRoles.add(adminRole);
        rootUser.setRole(rootRoles);
        userRepository.save(rootUser);

        User adminUser = new User();
        adminUser.setEmail("admin@admin");
        adminUser.setPassword(getEncodedPassword("root"));
        adminUser.setStatus(true);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);

        User userUser = new User();
        userUser.setEmail("user@user");
        userUser.setPassword(getEncodedPassword("root"));
        userUser.setStatus(true);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        userUser.setRole(userRoles);
        userRepository.save(userUser);

        User vendedorUser = new User();
        vendedorUser.setEmail("vendedor@vendedor");
        vendedorUser.setPassword(getEncodedPassword("root"));
        vendedorUser.setStatus(false);
        Set<Role> vendedorRoles = new HashSet<>();
        vendedorRoles.add(vendedorRole);
        vendedorUser.setRole(vendedorRoles);
        userRepository.save(vendedorUser);

        dataUserRepository.asociarUserData("root@root");
        dataUserRepository.asociarUserData("admin@admin");
        dataUserRepository.asociarUserData("user@user");
        dataUserRepository.asociarUserData("vendedor@vendedor");

        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Zapatos", true));
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Ropa", true));
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Higiene", true));

        Category category = categoryRepository.getCatById(1);
        User user = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveCategory("Tennis", 1200.00, 12, "Tennis chidos", category, user);

        Category category1 = categoryRepository.getCatById(3);
        User user1 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveCategory("Pasta de Dientes", 30.00, 8, "Pasta dentifrica colgate", category1, user1);

        User user2 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        Optional<Product> product = productRepository.findById(1);
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 4.5, product.get(), user2));
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 3.7, product.get(), user2));
        product = productRepository.findById(2);
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 4.5, product.get(), user2));
    }

    public User registerNewUser(JwtRegister jwtRegister) throws Exception {
        Role role = null;
        Set<Role> userRoles = new HashSet<>();
        User usuario = null;

        switch (jwtRegister.getRole()) {
            case 1:
                role = roleRepository.findById("Root").get();
                userRoles.add(role);
                usuario = new User(jwtRegister.getEmail(),
                        passwordEncoder.encode(jwtRegister.getPassword()),
                        true,
                        userRoles);
                break;
            case 2:
                role = roleRepository.findById("Admin").get();
                userRoles.add(role);
                usuario = new User(jwtRegister.getEmail(),
                        passwordEncoder.encode(jwtRegister.getPassword()),
                        true,
                        userRoles);
                break;
            case 3:
                role = roleRepository.findById("Seller").get();
                userRoles.add(role);
                usuario = new User(jwtRegister.getEmail(),
                        passwordEncoder.encode(jwtRegister.getPassword()),
                        false,
                        userRoles);
                break;
            case 4:
                role = roleRepository.findById("User").get();
                userRoles.add(role);
                usuario = new User(jwtRegister.getEmail(),
                        passwordEncoder.encode(jwtRegister.getPassword()),
                        true,
                        userRoles);
                break;
            default:

        }

        User user = userRepository.save(usuario);
        switch (jwtRegister.getRole()){
            case 1:
                sendEmail(user.getEmail(), "Bienvenido Super Admin", "Te registraron como SuperAdmin de SITILI");
                break;
            case 2:
                sendEmail(user.getEmail(),  "Bienvenido Admin", "Te registraron como administrador de SITILI");
                break;
            case 3:
                sendEmail(user.getEmail(), "Bienvenido Seller", "Te registraste como vendedor de SITILI, espera a que un administrador te acepte en la plataforma");
                break;
            case 4:
                sendEmail(user.getEmail(), "Bienvenido User", "Te registraste como cliente de SITILI, podras ver y adquirir diversos productos en nuestra plataforma");
                break;
            default:
        }

        dataUserRepository.asociarUserData(user.getEmail());

        return user;

    }

    @Override
    public String sendEmail(String username, String title, String bob) {
        String to = username;
        String subject = title;
        String body = bob;

        String result = emailService.sendMail(to, subject, body);
        return result;
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean bajaLogica(String email, boolean status) {
        try {
            userRepository.bajaLogica(email, status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}