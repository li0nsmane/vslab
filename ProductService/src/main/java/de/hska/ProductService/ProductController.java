package de.hska.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<Iterable<Product>> getProducts() {
        return new ResponseEntity<Iterable<Product>>(productRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping(value = "{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.equals(Optional.empty())) {
            return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
        }
        return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
    }

    // TODO implement in content service: by cat id


    @PostMapping(value = "/search")
    public ResponseEntity<Iterable<Product>> searchProducts(@RequestBody SearchRequest searchRequest) {

        if (searchRequest.getMinPrice() < 0) {
            searchRequest.setMinPrice(0);
        }

        if (searchRequest.getMaxPrice() < searchRequest.getMinPrice()) {
            searchRequest.setMaxPrice(Double.MAX_VALUE);
        }
        Iterable<Product> products = this.productRepository.findProductsByDetailsContainingIgnoreCaseAndPriceBetween(
                searchRequest.getDescription(), searchRequest.getMinPrice(), searchRequest.getMaxPrice());
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        if (productRepository.findProductByName(product.getName()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        // category must have a name
        if (product.getName() == null || product.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // everything ok -> save
        productRepository.save(product);
        Product createdProduct = this.productRepository.findProductByName(product.getName());
        return new ResponseEntity<Product>(createdProduct, HttpStatus.CREATED);


    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        Optional<Product> prod = productRepository.findById(id);
        if (prod != null && !prod.isEmpty()) {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "cat/{categoryId}")
    public ResponseEntity<Iterable<Product>> getProductsByCategoryId(@PathVariable long categoryId) {
        return new ResponseEntity<Iterable<Product>>(productRepository.findProductsByCategoryId(categoryId), HttpStatus.OK);
    }


}
