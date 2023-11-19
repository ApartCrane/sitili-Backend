package mp.sitili.modules.favorite.use_cases.repository;

import mp.sitili.modules.favorite.entities.Favorite;

import java.util.List;

public interface IFavoriteRepository {

    public List<Favorite> favXusuario(String userEmail);

    public Boolean deleteFav(String user_id, Integer product_id);

}
