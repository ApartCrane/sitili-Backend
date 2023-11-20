package mp.sitili.modules.user.use_cases.service;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.image_product.use_cases.service.ImageProductService;
import mp.sitili.modules.jwt.entities.JwtRegister;
import mp.sitili.modules.order.entities.Order;
import mp.sitili.modules.order.use_cases.methods.OrderRepository;
import mp.sitili.modules.order_detail.entities.OrderDetail;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private ImageProductService imageProductService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

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
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Electronicos", true));

        Category category = categoryRepository.getCatById(1);
        User user = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Tennis", 1200.00, 12, "Tennis chidos", category, user);

        Category category4 = categoryRepository.getCatById(4);
        User user4 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Laptop", 14000.00, 5, "Laptop ASUS", category4, user4);

        Category category1 = categoryRepository.getCatById(3);
        User user1 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Pasta de Dientes", 30.00, 8, "Pasta dentifrica colgate", category1, user1);

        User user2 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        Optional<Product> product = productRepository.findById(1);
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 4.5, product.get(), user2));
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 3.7, product.get(), user2));
        product = productRepository.findById(2);
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user2, product.get()));
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user2, product.get()));

        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 4.5, product.get(), user2));
        product = productRepository.findById(3);

        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 3.3, product.get(), user2));

        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/7929bdd8-c2a7-40d1-b299-ac838404e968.jpg", 1);
        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/11216399-552f-4ae2-afe2-a1a5bd6cc9e1.jpg", 1);
        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/11216399-552f-4ae2-afe2-a1a5bd6cc9e1.jpg", 2);

        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user, product.get()));
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user, product.get()));


        User user5 = userRepository.findById(String.valueOf("admin@admin")).orElse(null);
        addressRepository.save(new Address((int) addressRepository.count() + 1, user5, "Japan", "Ozaka", "Okinawa", 60280, "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka"));

        User user6 = userRepository.findById(String.valueOf("root@root")).orElse(null);
        Address address = addressRepository.save(new Address((int) addressRepository.count() + 1, user6, "Japan", "Ozaka", "Okinawa", 60280, "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka"));

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));

        Order orden = orderRepository.save(new Order((int) orderRepository.count() + 1 , user6, "Pendiente","No asignado", address, dateOrder));
        OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden, product.get(), 1, product.get().getPrice()));
    }

    public User registerNewUser(JwtRegister jwtRegister) throws Exception {
        Role role = null;
        Set<Role> userRoles = new HashSet<>();
        User usuario = null;
        User user = null;

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

        Optional<User> validador = userRepository.findById(usuario.getEmail());

        if(validador.isEmpty()){
            user = userRepository.save(usuario);
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
        }else{
            user = null;
        }

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