package mp.sitili.modules.user.use_cases.repository;


import mp.sitili.modules.user.use_cases.dto.SelectVendedorDTO;

import java.util.List;

public interface IUserRepository {

    String sendEmail(String username, String title, String bob);

    boolean bajaLogica(String email, boolean status);

    public List<SelectVendedorDTO> findSellers();

}
