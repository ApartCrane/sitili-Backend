package mp.sitili.modules.address.use_cases.methods;

import mp.sitili.modules.address.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface AddressRepository extends JpaRepository<Address, String> {

    @Query(value = "SELECT * FROM addresses WHERE user_id = :user_id", nativeQuery = true)
    public List<Address> dirXusuario(@Param("user_id") String user_id);

    @Query(value = "SELECT * \n" +
            "FROM addresses \n" +
            "WHERE user_id = :user_id \n" +
            "ORDER BY id DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    public Address dirActXusuario(@Param("user_id") String user_id);

    @Query(value = "SELECT * FROM addresses WHERE id = :id && user_id = :user_id", nativeQuery = true)
    public Address buscarDir(@Param("id") Integer id, @Param("user_id") String user_id);

    @Modifying
    @Query(value = "DELETE FROM addresses WHERE id = :id AND user_id = :user_id", nativeQuery = true)
    void deleteAddress(@Param("id") Integer id, @Param("user_id") String user_id);

}
