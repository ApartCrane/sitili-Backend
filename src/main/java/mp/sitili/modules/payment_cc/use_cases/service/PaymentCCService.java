package mp.sitili.modules.payment_cc.use_cases.service;

import mp.sitili.modules.payment_cc.entities.PaymentCC;
import mp.sitili.modules.payment_cc.use_cases.methods.PaymentCCRepository;
import mp.sitili.modules.payment_cc.use_cases.repository.IPaymentCCRepository;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentCCService implements IPaymentCCRepository {

    @Autowired
    private PaymentCCRepository paymentCCRepository;

    @Override
    public List<PaymentCC> pagoXusuario(String userEmail) {
        return paymentCCRepository.pagoXusuario(userEmail);
    }
}
