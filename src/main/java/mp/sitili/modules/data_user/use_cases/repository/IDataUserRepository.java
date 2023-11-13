package mp.sitili.modules.data_user.use_cases.repository;

import mp.sitili.modules.data_user.entities.DataUser;
import mp.sitili.modules.data_user.use_cases.dto.DataUserDTO;

import java.util.List;

public interface IDataUserRepository {

    public void asociarUserData(String email);

    List<DataUserDTO> findAllDataUsers();

}
