package mp.sitili.modules.shopping_car.use_cases.methods;

import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ShoppingCarRepository extends JpaRepository<ShoppingCar, String> {
}
