package mp.sitili.modules.data_user.use_cases.repository;

import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;

import javax.xml.crypto.Data;
import java.util.List;

public interface IDataUserRepository {

    public void asociarUserData(String email);

    public List<DataUserDTO> findAllDataUsers();

    public DataUserDTO findAllDataUser(String email);

    public boolean setCompany(String userEmail, String company, String phone);

    public boolean update(String company, String first_name, String last_name, String phone, Integer id, String user_id);

}
