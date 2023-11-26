package mp.sitili.modules.favorite.use_cases.repository;

import com.amazonaws.services.dynamodbv2.xspec.S;
import mp.sitili.modules.favorite.entities.Favorite;

import java.util.List;
import java.util.Map;

public interface IFavoriteRepository {

    public List<Map<String, Object>> favXusuario(String email);

    public Boolean deleteFav(String user_id, Integer product_id);

    public Favorite validarExis(Integer product_id, String user_id);

}
