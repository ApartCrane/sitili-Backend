package mp.sitili.modules.shopping_car.use_cases.methods;

import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, String> {

    @Query(value = "SELECT * FROM shopping_car WHERE user_id = :userEmail", nativeQuery = true)
    List<ShoppingCar> carXusuario(@Param("userEmail") String userEmail);

    @Modifying
    @Query(value = "DELETE FROM shopping_car WHERE user_id = :user_id AND product_id = :product_id", nativeQuery = true)
    void deleteCar(@Param("user_id") String user_id, @Param("product_id") Integer product_id);

}
