package mp.sitili.modules.order_detail.use_cases.service;

import mp.sitili.modules.order_detail.use_cases.dto.*;
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

    @Override
    public  List<DetallesSellerDTO> detalle(String sellerEmail){
        return orderDetailRepository.detalle(sellerEmail);
    }

    @Override
    public boolean detalleUpdate(String estado, int id) {
        try {
            orderDetailRepository.detalleUpdate(estado, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer validarOrden(Integer id){
        return orderDetailRepository.validarOrden(id);
    }

    @Override
    public RevisionpendientesDTO revisarPendientes(Integer order_id){
        return orderDetailRepository.revisarPendientes( order_id);
    }

}
