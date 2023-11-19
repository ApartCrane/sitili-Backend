package mp.sitili.modules.favorite.use_cases.methods;

import mp.sitili.modules.favorite.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    @Query(value = "SELECT * FROM favorities WHERE user_id = :userEmail", nativeQuery = true)
    List<Favorite> favXusuario(@Param("userEmail") String userEmail);

    @Modifying
    @Query(value = "DELETE FROM favorities WHERE user_id = :user_id AND product_id = :product_id", nativeQuery = true)
    void deleteFav(@Param("user_id") String user_id, @Param("product_id") Integer product_id);

}
