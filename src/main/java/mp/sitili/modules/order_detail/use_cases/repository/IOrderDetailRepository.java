package mp.sitili.modules.order_detail.use_cases.repository;

import mp.sitili.modules.order_detail.use_cases.dto.DetailsDTO;
import mp.sitili.modules.order_detail.use_cases.dto.PedidosAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasAnualesDTO;
import mp.sitili.modules.order_detail.use_cases.dto.VentasDTO;

import java.util.List;

public interface IOrderDetailRepository {
    public VentasDTO totalVentas();

    public List<VentasAnualesDTO> totalVentasAnuales();

    public List<PedidosAnualesDTO> totalPedidosAnuales();

    public List<DetailsDTO> details(String userEmail, Integer id);
}
