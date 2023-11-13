package mp.sitili.modules.raiting.use_cases.methods;

import mp.sitili.modules.raiting.entities.Raiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RaitingRepository extends JpaRepository<Raiting, String> {
}
