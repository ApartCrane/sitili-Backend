package mp.sitili.modules.shopping_car.use_cases.service;


import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import mp.sitili.modules.shopping_car.use_cases.methods.ShoppingCarRepository;
import mp.sitili.modules.shopping_car.use_cases.repository.IShoppingCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingCarService implements IShoppingCarRepository {

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Override
    public List<Map<String, Object>> carXusuario(String email) {
        List<Object[]> rawProducts = new ArrayList<>();
        try {
            rawProducts = shoppingCarRepository.carXusuario(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> productList = new ArrayList<>();

        for (Object[] row : rawProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("car_id", row[0]);
            productData.put("producto", row[1]);
            productData.put("precio", row[2]);
            productData.put("comentarios", row[3]);
            productData.put("calificacion", row[4]);
            productData.put("categoria", row[5]);
            productData.put("vendedor", row[6]);
            String imagenesConcatenadas = (String) row[7];
            if(imagenesConcatenadas == null){
                productData.put("imagenes", "Aun no cuenta con Imagenes");
            }else{
                List<String> listaImagenes = Arrays.asList(imagenesConcatenadas.split(","));
                productData.put("imagenes", listaImagenes);
            }
            productList.add(productData);
        }
        return productList;
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

    @Override
    public Boolean updateCar(Integer car_id, Integer quantity){
        try{
            shoppingCarRepository.updateCar(car_id, quantity);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public ShoppingCar validarExis(Integer product_id, String user_id){
        return shoppingCarRepository.validarExis(product_id, user_id);
    }

    @Override
    public ShoppingCar findById1(Integer id){
        return shoppingCarRepository.findById1(id);
    }

}
