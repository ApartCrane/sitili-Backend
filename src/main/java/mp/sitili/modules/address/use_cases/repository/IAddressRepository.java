package mp.sitili.modules.address.use_cases.repository;

import mp.sitili.modules.address.entities.Address;

import java.util.List;

public interface IAddressRepository {

    public List<Address> dirXusuario(String user_id);

    public Address dirActXusuario(String user_id);

    public Boolean deleteAddress(Integer id, String user_id);

    public Address buscarDir(Integer id, String user_id);

}
