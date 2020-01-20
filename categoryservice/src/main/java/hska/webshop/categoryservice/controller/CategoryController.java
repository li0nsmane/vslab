package hska.webshop.categoryservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hska.webshop.categoryservice.model.Category;
import hska.webshop.categoryservice.repositories.CategoryRepo;

@RestController
public class CategoryController {

	@Autowired
	private CategoryRepo categoryRepo;

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Category>> getCategories() {
		return new ResponseEntity<Iterable<Category>>(categoryRepo.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
	public ResponseEntity<Category> getCategoryById(@PathVariable long id) {
		Optional<Category> category = categoryRepo.findById(id);
		if (!category.equals(Optional.empty())) {
			return new ResponseEntity<Category>(category.get(), HttpStatus.OK);
		}
		return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		if (categoryRepo.findCategoryByName(category.getName()) == null) {
			if (category.getName() == null || category.getName().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				categoryRepo.save(category);
				Category tmpCategory = this.categoryRepo.findCategoryByName(category.getName());
				return new ResponseEntity<Category>(tmpCategory, HttpStatus.CREATED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
		if (!categoryRepo.findById(id).equals(Optional.empty())) {
			categoryRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
