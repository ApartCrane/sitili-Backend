package mp.sitili.modules.raiting.use_cases.service;

import mp.sitili.modules.raiting.use_cases.methods.RaitingRepository;
import mp.sitili.modules.raiting.use_cases.repository.IRaitingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RaitingService implements IRaitingRepository {

    @Autowired
    private RaitingRepository raitingRepository;

    @Override
    public Integer cal4(String sellerEmail){
        return raitingRepository.cal4(sellerEmail);
    }
}
