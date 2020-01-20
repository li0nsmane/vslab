package de.hska.ContentService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import de.hska.ContentService.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.*;

@Component
public class ProductClient {

    private final Map<Long, Product> cache = new LinkedHashMap<Long, Product>();

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT_URL = "http://product-service/products";


    @HystrixCommand(fallbackMethod = "getProductsFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public Iterable<Product> getProducts() {

        Product[] prods = restTemplate.getForObject(PRODUCT_URL, Product[].class);
        cache.clear();
        for (Product p : prods) {
            cache.put(p.getId(), p);
        }

        return Arrays.asList(prods);
    }

    @HystrixCommand(fallbackMethod = "getProductByIdCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public Product getProductById(long productId) {
        Product prod = restTemplate.getForObject(PRODUCT_URL + "/" + productId, Product.class);

            cache.putIfAbsent(prod.getId(), prod);

        return prod;
    }

    @HystrixCommand(fallbackMethod = "getProductsByCatIdCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public Iterable<Product> getProductsByCategoryId(long categoryId) {

        Product[] prods = restTemplate.getForObject(PRODUCT_URL + "/cat/" + categoryId, Product[].class);
        for (Product p : prods) {
            cache.putIfAbsent(p.getId(), p);
        }
        return Arrays.asList(prods);
    }

    @HystrixCommand(fallbackMethod = "searchProductCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")})
    public Iterable<Product> searchProducts(SearchRequest searchRequest) {
        Product[] prods = restTemplate.postForObject(PRODUCT_URL + "/search", searchRequest, Product[].class);

        for (Product p : prods) {
            cache.putIfAbsent(p.getId(), p);
        }
        return Arrays.asList(prods);
    }

    public Product addProduct(Product product) {
        Product prod = restTemplate.postForObject(PRODUCT_URL, product, Product.class);
        cache.putIfAbsent(prod.getId(), prod);
        return prod;
    }

    public void deleteProduct(long productId) {
        restTemplate.delete(PRODUCT_URL + "/" + productId);
        cache.remove(productId);
    }

    public Iterable<Product> getProductsFromCache() {
        return cache.values();
    }

    public Iterable<Product> getProductsByCatIdCache(long categoryId) {
        ArrayList<Product> foundProducts = new ArrayList<Product>();
        for (Map.Entry<Long, Product> productEntry : cache.entrySet()) {
            if(productEntry.getValue().getCategoryId() == categoryId){
                foundProducts.add(productEntry.getValue());
            }
        }

        return foundProducts;


    }

    public Product getProductByIdCache(long productId) {
        return cache.getOrDefault(productId, new Product());
    }

    public Iterable<Product> searchProductCache(SearchRequest request) {

        ArrayList<Product> foundProducts = new ArrayList<Product>();
        for (Map.Entry<Long, Product> productEntry : cache.entrySet()) {
            if(compareProductWithSearch(productEntry.getValue(), request)){
                foundProducts.add(productEntry.getValue());
            }
        }

        return foundProducts;



    }

    private boolean compareProductWithSearch(Product p, SearchRequest search){
        return p.getDetails().toLowerCase().equals(search.getDescription().toLowerCase()) &&
                (p.getPrice() >= search.getMinPrice() && p.getPrice() <= search.getMaxPrice());


    }


}
