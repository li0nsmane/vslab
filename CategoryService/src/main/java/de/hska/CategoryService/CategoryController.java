package de.hska.CategoryService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping
    public ResponseEntity<Iterable<Category>> getCategories() {

        return new ResponseEntity<Iterable<Category>>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable long id) {
        Optional<Category> c = categoryRepository.findById(id);
        if (!c.equals(Optional.empty())) {
            return new ResponseEntity<Category>(c.get(), HttpStatus.OK);
        }
        return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // check if category exists
        if (categoryRepository.findCategoryByName(category.getName()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        // category must have a name
        if (category.getName() == null || category.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // everything ok -> save
        categoryRepository.save(category);
        Category createdCategory = this.categoryRepository.findCategoryByName(category.getName());
        return new ResponseEntity<Category>(createdCategory, HttpStatus.CREATED);

    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        Optional<Category> cat = categoryRepository.findById(id);
        if (cat != null && !cat.isEmpty()) {
            categoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}


