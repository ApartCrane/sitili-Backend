package mp.sitili.modules.acept_seller.use_cases.service;

import mp.sitili.modules.acept_seller.use_cases.dto.AceptSellerDTO;
import mp.sitili.modules.acept_seller.use_cases.methods.AceptSellerRepository;
import mp.sitili.modules.acept_seller.use_cases.repository.IAceptSellerRepository;
import mp.sitili.modules.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AceptSellerService implements IAceptSellerRepository {

    @Autowired
    private AceptSellerRepository aceptSellerRepository;

    @Override
    public List<AceptSellerDTO> findAllSellersNa() {
        return aceptSellerRepository.findAllSellersNa();
    }

}
