package mp.sitili.modules.order_detail.use_cases.repository;

import mp.sitili.modules.order_detail.use_cases.dto.*;

import java.util.List;

public interface IOrderDetailRepository {
    public VentasDTO totalVentas();

    public List<VentasAnualesDTO> totalVentasAnuales();

    public List<PedidosAnualesDTO> totalPedidosAnuales();

    public List<DetailsDTO> details(String userEmail, Integer id);

    public  List<DetallesSellerDTO> detalle(String sellerEmail);

    boolean detalleUpdate(String estado, int id);

    Integer validarOrden(Integer id);

    RevisionpendientesDTO revisarPendientes(Integer order_id);
}
