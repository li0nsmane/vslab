package de.hska.ContentService;

import de.hska.ContentService.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private CategoryClient categoryClient;

    @GetMapping
    public ResponseEntity<Iterable<Product>> getProducts() {
        return new ResponseEntity<Iterable<Product>>(productClient.getProducts(), HttpStatus.OK);
    }

    @GetMapping(value = "category/{categoryId}")
    public ResponseEntity<Iterable<Product>> getProductsByCategoryId(@PathVariable long categoryId) {
        return new ResponseEntity<Iterable<Product>>(productClient.getProductsByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        return new ResponseEntity<Product>(productClient.getProductById(id), HttpStatus.OK);
    }

    @PostMapping(value = "search")
    public ResponseEntity<Iterable<Product>> searchProduct(@RequestBody SearchRequest searchRequest) {


        return new ResponseEntity<Iterable<Product>>(
                productClient.searchProducts(searchRequest),
                HttpStatus.OK);

    }


    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        long categoryId = product.getCategoryId();
        if (categoryClient.getCategoryById(categoryId) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Product product1 = productClient.addProduct(product);
        return new ResponseEntity<Product>(product1, HttpStatus.OK);
    }


    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productClient.deleteProduct(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
