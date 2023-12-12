package mp.sitili.modules.address.use_cases.service;

import mp.sitili.modules.address.entities.Address;
import mp.sitili.modules.address.use_cases.methods.AddressRepository;
import mp.sitili.modules.address.use_cases.repository.IAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService implements IAddressRepository {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> dirXusuario(String user_id){
        return addressRepository.dirXusuario(user_id);
    }

    @Override
    public Address dirActXusuario(String user_id){
        return addressRepository.dirActXusuario(user_id);
    }

    @Override
    public Address buscarDir(Integer id, String user_id){
        return addressRepository.buscarDir(id, user_id);
    }

    @Override
    public Boolean deleteAddress(Integer id, String user_id){
        try{
            addressRepository.deleteAddress(id, user_id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
