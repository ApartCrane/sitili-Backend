package mp.sitili.modules.data_user.use_cases.methods;

import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DataUserRepository extends JpaRepository<DataUser, Integer> {

    @Modifying
    @Query(value = "INSERT INTO data_users(user_id, first_name, last_name) VALUES(:email, :first_name, :last_name)", nativeQuery = true)
    public void asociarUserData(@Param("email") String email, @Param("first_name") String first_name, @Param("last_name") String last_name);

    @Query(value = "SELECT du.company, du.first_name, du.last_name, du.phone, du.register_date, du.user_id, u.status, ur.role_id FROM data_users du INNER JOIN users u ON du.user_id = u.email INNER JOIN user_role ur ON u.email = ur.user_id", nativeQuery = true)
    public List<DataUserDTO> findAllDataUsers();

    @Query(value = "SELECT du.company, du.first_name, du.last_name, du.phone, du.register_date, du.user_id, u.status, ur.role_id FROM data_users du INNER JOIN users u ON du.user_id = u.email INNER JOIN user_role ur ON u.email = ur.user_id && u.email = :email", nativeQuery = true)
    public DataUserDTO findAllDataUser(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE data_users SET company = :company, phone = :phone WHERE user_id = :userEmail", nativeQuery = true)
    boolean setCompany(@Param("userEmail") String userEmail, @Param("company") String company, @Param("phone") String phone);



}
