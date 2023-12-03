package mp.sitili.modules.shopping_car.use_cases.repository;

import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;

import java.util.List;
import java.util.Map;

public interface IShoppingCarRepository {

    public List<Map<String, Object>> carXusuario(String email);

    public Boolean deleteCar(String user_id, Integer product_id);

    public ShoppingCar validarExis(Integer product_id, String user_id);

    public ShoppingCar findById1(Integer id);

    public Boolean updateCar(Integer car_id, Integer quantity);

}
