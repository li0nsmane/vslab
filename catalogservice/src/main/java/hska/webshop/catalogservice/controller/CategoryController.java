package hska.webshop.catalogservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hska.webshop.catalogservice.clients.CategoryClient;
import hska.webshop.catalogservice.clients.ProductClient;
import hska.webshop.catalogservice.model.Category;

@RestController
public class CategoryController {

	@Autowired
	private CategoryClient categoryClient;
	
	@Autowired
	private ProductClient productClient;

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Category>> getCategories() {
		return new ResponseEntity<Iterable<Category>>(categoryClient.getCategories(), HttpStatus.OK);
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
	public ResponseEntity<Category> getCategoryById(@PathVariable long id) {
		return new ResponseEntity<Category>(categoryClient.getCategoryById(id), HttpStatus.OK);
	}


	@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Long> addCategory(@RequestBody Category category) {
		long categoryId = categoryClient.addCategory(category);
		return new ResponseEntity<Long>(categoryId, HttpStatus.CREATED);
	}

	@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
		categoryClient.deleteCategory(id);
		productClient.deleteProductsByCategoryId(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}