package mp.sitili.modules.image_product.use_cases.dto;

import com.amazonaws.services.dynamodbv2.xspec.L;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;

import java.util.List;

public interface ImagesDTO{
    String getProducto();
    Double getPrecio();
    Integer getCantidad();
    String getComentarios();
    Double getCalifiaccion();
    Boolean getEstado();
    String getCategoria();
    String getVendedor();
    String getNombreVendedor();
    String getApellidoVendedor();
    List<String> getImagenes();
}
