package mp.sitili.modules.data_user.use_cases.service;

import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;
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
            boolean revision = dataUserRepository.setCompany(userEmail, company, phone);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
