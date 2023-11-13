package mp.sitili.modules.category.adapters;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List> obtenerTodasCategorias() {
        List<Category> categories = categoryRepository.findAll();

        if(categories != null){
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(categories, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        Category cat = categoryRepository.save(new Category((int) categoryRepository.count() + 1, category.getName(), true));
        if(cat != null){
            return new ResponseEntity<>("Categoria creada exitosamente", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Error al crear Categoria", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> actualizarCategory(@RequestBody Category category) {
        if(categoryService.getCategoryById(category.getId())){
            if(categoryService.updateCategory(category.getId(), category.getName())){
                return new ResponseEntity<>("Categoria Actualizada exitosamente", HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>("Error al actualizar Categoria", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("Categoria no Encontrada", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> eliminarCategory(@RequestBody Category category) {
        if (categoryService.getStatusCategory(category.getId())) {
            categoryService.deleteCategory(category.getId(), false);
            return new ResponseEntity<>("Categoria dada de baja exitosamente", HttpStatus.OK);
        } else {
            categoryService.deleteCategory(category.getId(), true);
            return new ResponseEntity<>("Categoria dada de alta exitosamente", HttpStatus.OK);
        }
    }
}
