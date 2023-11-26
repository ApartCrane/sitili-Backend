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
import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_order.entities.PaymentOrder;
import mp.sitili.modules.payment_order.use_cases.methods.PaymentOrderRepositry;
import mp.sitili.modules.product.entities.Product;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.raiting.entities.Raiting;
import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.methods.RoleRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import mp.sitili.modules.user.use_cases.repository.IUserRepository;
import mp.sitili.utils.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
    private ShoppingCarRepository shoppingCarRepository;

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

    @Autowired
    private PaymentCCRepository paymentCCRepository;

    @Autowired
    private PaymentOrderRepositry paymentOrderRepositry;

    public void initRoleAndUser() throws ParseException {

        // Crear roles
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

// Crear usuarios con roles
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

        User vendedorUser1 = new User();
        vendedorUser1.setEmail("vend@vend");
        vendedorUser1.setPassword(getEncodedPassword("root"));
        vendedorUser1.setStatus(true);
        Set<Role> vendedorRoles1 = new HashSet<>();
        vendedorRoles1.add(vendedorRole);
        vendedorUser1.setRole(vendedorRoles1);
        userRepository.save(vendedorUser1);

// Asociar datos a usuarios
        dataUserRepository.asociarUserData("root@root", "Carlo", "Yael");
        dataUserRepository.asociarUserData("admin@admin", "Daniel", "Wong");
        dataUserRepository.asociarUserData("user@user", "Diego", "Garcia");
        dataUserRepository.asociarUserData("vendedor@vendedor", "Hector", "Marquina");

        dataUserRepository.asociarUserData("vend@vend", "wongsito", "wongsito");

// Crear categorías
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Zapatos", true));
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Ropa", true));
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Higiene", true));
        categoryRepository.save(new Category((int) categoryRepository.count() + 1, "Electrónicos", true));

// Crear productos con categorías y usuarios asociados
        Category category1 = categoryRepository.getCatById(1);
        User user1 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Tennis", 1200.00, 12, "Tennis chidos", category1, user1);

        Category category4 = categoryRepository.getCatById(4);
        User user4 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Laptop", 14000.00, 5, "Laptop ASUS", category4, user4);

        Category category3 = categoryRepository.getCatById(3);
        User user3 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        productService.saveProduct("Pasta de Dientes", 30.00, 8, "Pasta dentífrica colgate", category3, user3);
        User userF = userRepository.findById(String.valueOf("user@user")).orElse(null);


// Asociar valoraciones y favoritos a productos
        User user2 = userRepository.findById(String.valueOf("vendedor@vendedor")).orElse(null);
        Optional<Product> product1 = productRepository.findById(1);
        Optional<Product> product2 = productRepository.findById(2);
        Optional<Product> product3 = productRepository.findById(3);
        //ShoppingCar Integer id, User user, Product product, Integer quantity
        shoppingCarRepository.save(new ShoppingCar((int) (shoppingCarRepository.count() + 1), userF, product1.get(), 1));
        shoppingCarRepository.save(new ShoppingCar((int) (shoppingCarRepository.count() + 1), userF, product2.get(), 1));


        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 4.5, product1.get(), user2));
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 3.7, product1.get(), user2));
        Optional<Product> producto1 = productRepository.findById(2);
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user2, product1.get()));
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), user3, product2.get()));
        favoriteRepository.save(new Favorite((int) (favoriteRepository.count() + 1), userF, product2.get()));
        Optional<Product> producto2 = productRepository.findById(3);
        raitingRepository.save(new Raiting((int) raitingRepository.count() + 1, 3.3, product3.get(), user2));

// Guardar imágenes de productos
        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/7929bdd8-c2a7-40d1-b299-ac838404e968.jpg", 1);
        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/11216399-552f-4ae2-afe2-a1a5bd6cc9e1.jpg", 1);
        imageProductService.saveImgs("https://sitili-e-commerce.s3.amazonaws.com/11216399-552f-4ae2-afe2-a1a5bd6cc9e1.jpg", 2);

// Crear direcciones para usuarios
        User user5 = userRepository.findById(String.valueOf("admin@admin")).orElse(null);
        User user6 = userRepository.findById(String.valueOf("root@root")).orElse(null);
        User user7 = userRepository.findById(String.valueOf("user@user")).orElse(null);
        Address address1 = addressRepository.save(new Address((int) addressRepository.count() + 1, user5, "Japan", "Ozaka", "Okinawa", 60280, "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka"));
        Address address2 = addressRepository.save(new Address((int) addressRepository.count() + 1, user6, "Japan", "Ozaka", "Okinawa", 60280, "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka"));
        Address address3 = addressRepository.save(new Address((int) addressRepository.count() + 1, user7, "Japan", "Ozaka", "Okinawa", 60280, "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka", "Taka taka, taka taka"));

// Crear formas de pago para usuarios
        PaymentCC paymentCC1 = paymentCCRepository.save(new PaymentCC((int) (paymentCCRepository.count() + 1), user5, "1234567891234567", "04", "08", "2002", "123"));
        PaymentCC paymentCC2 = paymentCCRepository.save(new PaymentCC((int) (paymentCCRepository.count() + 1), user6, "1234567891234567", "06", "10", "2004", "123"));

// Crear órdenes para los usuarios
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha1Date = new SimpleDateFormat("dd-MM-yyyy").parse("04-08-2023");
        Timestamp fecha1 = new Timestamp(fecha1Date.getTime());

        Date fecha2Date = new SimpleDateFormat("dd-MM-yyyy").parse("04-10-2022");
        Timestamp fecha2 = new Timestamp(fecha2Date.getTime());
        Timestamp dateOrder = Timestamp.valueOf(sdf.format(timestamp));

        Order orden1 = orderRepository.save(new Order((int) orderRepository.count() + 1, user5, "Pendiente", "No asignado", address1, dateOrder));
        Order orden2 = orderRepository.save(new Order((int) orderRepository.count() + 1, user6, "Trafico", "UPs", address2, fecha1));
        Order orden3 = orderRepository.save(new Order((int) orderRepository.count() + 1, user6, "Entrega", "FEDEX", address2, fecha2));

// Crear detalles de órdenes asociados a productos y órdenes
        OrderDetail orderDetail1 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden1, producto1.get(), 1, producto1.get().getPrice()));
        OrderDetail orderDetail2 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden1, producto2.get(), 2, producto2.get().getPrice()));
        OrderDetail orderDetail3 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden2, producto1.get(), 3, producto1.get().getPrice()));
        OrderDetail orderDetail4 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden3, producto1.get(), 4, producto1.get().getPrice()));
        OrderDetail orderDetail5 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden3, producto2.get(), 5, producto2.get().getPrice()));
        OrderDetail orderDetail6 = orderDetailRepository.save(new OrderDetail((int) orderRepository.count() + 1, orden3, producto2.get(), 6, producto2.get().getPrice()));

// Crear relaciones entre órdenes y formas de pago
        paymentOrderRepositry.save(new PaymentOrder((long) ((int) paymentOrderRepositry.count() + 1), orden1, paymentCC1));
        paymentOrderRepositry.save(new PaymentOrder((long) ((int) paymentOrderRepositry.count() + 1), orden2, paymentCC2));
        paymentOrderRepositry.save(new PaymentOrder((long) ((int) paymentOrderRepositry.count() + 1), orden3, paymentCC2));
    }

    public User registerNewUser(String email, String password, String first_name, String last_name, Integer rol) throws Exception {
        Role role = null;
        Set<Role> userRoles = new HashSet<>();
        User usuario = null;
        User user = null;

        switch (rol) {
            case 1:
                role = roleRepository.findById("Root").get();
                userRoles.add(role);
                usuario = new User(email,
                        passwordEncoder.encode(password),
                        true,
                        userRoles);
                break;
            case 2:
                role = roleRepository.findById("Admin").get();
                userRoles.add(role);
                usuario = new User(email,
                        passwordEncoder.encode(password),
                        true,
                        userRoles);
                break;
            case 3:
                role = roleRepository.findById("Seller").get();
                userRoles.add(role);
                usuario = new User(email,
                        passwordEncoder.encode(password),
                        false,
                        userRoles);
                break;
            case 4:
                role = roleRepository.findById("User").get();
                userRoles.add(role);
                usuario = new User(email,
                        passwordEncoder.encode(password),
                        true,
                        userRoles);
                break;
            default:

        }

        Optional<User> validador = userRepository.findById(usuario.getEmail());

        if(validador.isEmpty()){
            user = userRepository.save(usuario);
            switch (rol){
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
            dataUserRepository.asociarUserData(user.getEmail(), first_name, last_name);
        }else{
            return user = null;
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

    @Override
    public List<SelectVendedorDTO> findSellers(){
        return userRepository.findSellers();
    }

}