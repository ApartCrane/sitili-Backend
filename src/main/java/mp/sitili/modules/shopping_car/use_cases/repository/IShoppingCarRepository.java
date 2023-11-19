package mp.sitili.modules.shopping_car.use_cases.repository;

import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;

import java.util.List;

public interface IShoppingCarRepository {

    public List<ShoppingCar> carXusuario(String userEmail);

    public Boolean deleteCar(String user_id, Integer product_id);

}
