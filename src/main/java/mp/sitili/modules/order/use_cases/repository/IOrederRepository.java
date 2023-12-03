package mp.sitili.modules.order.use_cases.repository;

import mp.sitili.modules.order.use_cases.dto.EntregasMesDTO;
import mp.sitili.modules.order.use_cases.dto.VentasMesDTO;
import mp.sitili.modules.shopping_car.entities.ShoppingCar;

import java.util.List;

public interface IOrederRepository {

    public boolean updateDelivery(int id, String repartidor, String status);

    public boolean updateRecive(int id, String status);

    public Integer sellerEnvs(String sellerEmail);

    public Double sellerSales(String sellerEmail);

    public List<ShoppingCar> buscarTodo(String userEmail);

    List<VentasMesDTO> ventasAnioMonth(String sellerEmail);

    public List<EntregasMesDTO>  enviosAnioMonth(String sellerEmail);

}
