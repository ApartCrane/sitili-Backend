package mp.sitili.modules.image_product.use_cases.methods;

import mp.sitili.modules.image_product.entities.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ImageProductRepository extends JpaRepository<ImageProduct, String> {
}
