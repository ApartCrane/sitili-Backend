package mp.sitili.modules.user.use_cases.methods;

import mp.sitili.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Query(value = "UPDATE users SET status = :status WHERE email = :email", nativeQuery = true)
    void bajaLogica(@Param("email") String email, @Param("status") boolean status);

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %Admin%", nativeQuery = true)
    List<User> findAllAdmins();

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %Seller%", nativeQuery = true)
    List<User> findAllSellers();

    @Query(value = "SELECT * FROM user_role WHERE role_id LIKE %User%", nativeQuery = true)
    List<User> findAllUsers();
}