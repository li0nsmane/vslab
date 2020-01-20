package hska.webshop.productservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hska.webshop.productservice.model.Product;
import hska.webshop.productservice.repositories.ProductRepo;

@RestController
public class ProductController {

	@Autowired
	private ProductRepo productRepo;

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProducts() {
		return new ResponseEntity<Iterable<Product>>(productRepo.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProductById(@PathVariable long id) {
		Optional<Product> product = productRepo.findById(id);
		if (!product.equals(Optional.empty())) {
			return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
		}
		return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/products/category/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProductsByCategoryId(@PathVariable long categoryId) {
		return new ResponseEntity<Iterable<Product>>(productRepo.findProductsByCategoryId(categoryId), HttpStatus.OK);
	}

	@RequestMapping(value = "/products/search", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProductsForSearchValue(@RequestParam String searchDescription,
			@RequestParam double searchMinPrice, @RequestParam double searchMaxPrice) {
		try {
			searchDescription = URLDecoder.decode(searchDescription, "UTF-8");
			if (searchMaxPrice < 0) {
				searchMaxPrice = Double.MAX_VALUE;
			}
			Iterable<Product> products = this.productRepo.findProductsByDetailsContainingIgnoreCaseAndPriceBetween(
					searchDescription, searchMinPrice, searchMaxPrice);
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		if (productRepo.findProductByName(product.getName()) == null) {
			if (product.getName() == null || product.getName().isEmpty() || product.getDetails() == null
					|| product.getDetails().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				productRepo.save(product);
				Product tmpProduct = this.productRepo.findProductByName(product.getName());
				return new ResponseEntity<Product>(tmpProduct, HttpStatus.CREATED);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
		if (!productRepo.findById(id).equals(Optional.empty())) {
			productRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/products", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProductsByCategoryId(@RequestParam long categoryId) {
		List<Product> products = productRepo.findProductsByCategoryId(categoryId);
		for (Product product : products) {
			productRepo.delete(product);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
