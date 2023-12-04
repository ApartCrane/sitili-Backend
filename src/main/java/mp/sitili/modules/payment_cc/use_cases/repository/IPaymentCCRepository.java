package mp.sitili.modules.payment_cc.use_cases.repository;

import mp.sitili.modules.payment_cc.entities.PaymentCC;

import java.util.List;

public interface IPaymentCCRepository {

    public List<PaymentCC> pagoXusuario(String userEmail);
    public PaymentCC tarjetaXusuario(String userEmail);
    public PaymentCC findById(Integer id);

}
