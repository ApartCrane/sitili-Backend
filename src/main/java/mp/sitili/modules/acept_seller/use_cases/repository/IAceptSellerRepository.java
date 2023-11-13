package mp.sitili.modules.acept_seller.use_cases.repository;

import mp.sitili.modules.acept_seller.use_cases.dto.AceptSellerDTO;
import mp.sitili.modules.user.entities.User;

import java.util.List;

public interface IAceptSellerRepository {

    List<AceptSellerDTO> findAllSellersNa();


}
