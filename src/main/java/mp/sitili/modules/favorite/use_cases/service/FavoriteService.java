package mp.sitili.modules.favorite.use_cases.service;


import mp.sitili.modules.favorite.entities.Favorite;
import mp.sitili.modules.favorite.use_cases.methods.FavoriteRepository;
import mp.sitili.modules.favorite.use_cases.repository.IFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService implements IFavoriteRepository {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> favXusuario(String userEmail) {
        return favoriteRepository.favXusuario(userEmail);
    }

    @Override
    public Boolean deleteFav(String user_id, Integer product_id){
        try{
            favoriteRepository.deleteFav(user_id, product_id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
