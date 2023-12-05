package mp.sitili.modules.data_user.use_cases.service;

import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
import mp.sitili.modules.data_user.use_cases.dto.UsuariosxMesDTO;
import mp.sitili.modules.data_user.use_cases.methods.DataUserRepository;
import mp.sitili.modules.data_user.use_cases.repository.IDataUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataUserService implements IDataUserRepository {

    @Autowired
    private DataUserRepository dataUserRepository;

    @Override
    public void asociarUserData(String email){
    }

    @Override
    public List<DataUserDTO> findAllDataUsers(){
        return this.dataUserRepository.findAllDataUsers();
    }

    @Override
    public DataUserDTO findAllDataUser(String email){
        return this.dataUserRepository.findAllDataUser(email);
    }

    @Override
    public boolean setCompany(String userEmail, String company, String phone){
        try {
            dataUserRepository.setCompany(userEmail, company, phone);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(String company, String first_name, String last_name, String phone, Integer id, String user_id){
        try {
            dataUserRepository.update(company, first_name, last_name, phone, id, user_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<UsuariosxMesDTO> usuXmes(){
        return dataUserRepository.usuXmes();
    }


}
