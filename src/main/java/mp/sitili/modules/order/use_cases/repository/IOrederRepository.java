package mp.sitili.modules.order.use_cases.repository;

public interface IOrederRepository {

    boolean updateDelivery(int id, String repartidor, String status);

    boolean updateRecive(int id, String status);

}
