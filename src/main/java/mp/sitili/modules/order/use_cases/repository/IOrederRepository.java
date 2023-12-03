package mp.sitili.modules.order.use_cases.repository;

public interface IOrederRepository {

    public boolean updateDelivery(int id, String repartidor, String status);

    public boolean updateRecive(int id, String status);

    public Integer sellerEnvs(String sellerEmail);

    public Double sellerSales(String sellerEmail);

}
