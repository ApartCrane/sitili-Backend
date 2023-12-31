package mp.sitili.modules.image_product.use_cases.service;

import mp.sitili.modules.image_product.use_cases.methods.ImageProductRepository;
import mp.sitili.modules.image_product.use_cases.repository.IImageProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageProductService implements IImageProductRepository {

    private final ImageProductRepository imageProductRepository;

    public ImageProductService(ImageProductRepository imageProductRepository) {
        this.imageProductRepository = imageProductRepository;
    }

    @Override
    public boolean saveImgs(String image_url, Integer product_id) {
        try {
            imageProductRepository.saveImgs(image_url, product_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteImage(String image_url, Integer produc_id) {
        try {
            imageProductRepository.deleteImage(image_url, produc_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
