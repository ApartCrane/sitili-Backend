package mp.sitili.modules.order_detail.use_cases.service;

import mp.sitili.modules.order_detail.use_cases.dto.DetailsDTO;
import mp.sitili.modules.order_detail.use_cases.dto.PedidosAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasDTO;
import mp.sitili.modules.order_detail.use_cases.methods.OrderDetailRepository;
import mp.sitili.modules.order_detail.use_cases.repository.IOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailRepository {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public VentasDTO totalVentas(){
        return orderDetailRepository.totalVentas();
    }

    @Override
    public List<VentasAnualesDTO> totalVentasAnuales(){
        return orderDetailRepository.totalVentasAnuales();
    }

    @Override
    public List<PedidosAnualesDTO> totalPedidosAnuales(){
        return orderDetailRepository.totalPedidosAnuales();
    }

    @Override
    public List<DetailsDTO> details(String userEmail, Integer id){
        return orderDetailRepository.details(userEmail, id);
    }

}
