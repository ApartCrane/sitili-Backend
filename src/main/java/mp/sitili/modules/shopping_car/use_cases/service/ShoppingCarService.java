package mp.sitili.modules.shopping_car.use_cases.service;


import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.shopping_car.use_cases.repository.IShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCarService implements IShoppingCarRepository {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Override
    public List<ShoppingCar> carXusuario(String userEmail) {
        return shoppingCarRepository.carXusuario(userEmail);
    }

    @Override
    public Boolean deleteCar(String user_id, Integer product_id){
        try{
            shoppingCarRepository.deleteCar(user_id, product_id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
