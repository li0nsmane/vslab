package hska.webshop.catalogservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hska.webshop.catalogservice.clients.CategoryClient;
import hska.webshop.catalogservice.clients.ProductClient;
import hska.webshop.catalogservice.model.Product;

@RestController
public class ProductController {

	@Autowired
	private CategoryClient categoryClient;

	@Autowired
	private ProductClient productClient;

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProducts() {
		return new ResponseEntity<Iterable<Product>>(productClient.getProducts(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/products/category/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProductsByCategoryId(@PathVariable long categoryId) {
		return new ResponseEntity<Iterable<Product>>(productClient.getProductsByCategoryId(categoryId), HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProductById(@PathVariable long id) {
		return new ResponseEntity<Product>(productClient.getProductById(id), HttpStatus.OK);
	}

	@RequestMapping(value = "/products/search", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProductsForSearchValue(@RequestParam String searchDescription,
			@RequestParam double searchMinPrice, @RequestParam double searchMaxPrice) {
		try {
			searchDescription = URLDecoder.decode(searchDescription, "UTF-8");
			if (searchMaxPrice < 0) {
				searchMaxPrice = Double.MAX_VALUE;
			}
			return new ResponseEntity<Iterable<Product>>(
					productClient.getProductsForSearchValue(searchDescription, searchMinPrice, searchMaxPrice),
					HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Long> addProduct(@RequestBody Product product) {
		long categoryId = product.getCategoryId();
		if (categoryClient.getCategoryById(categoryId) != null) {
			long id = productClient.addProduct(product);
			return new ResponseEntity<Long>(id, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
		productClient.deleteProduct(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}