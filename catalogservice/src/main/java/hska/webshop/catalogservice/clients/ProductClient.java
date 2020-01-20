package hska.webshop.catalogservice.clients;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import hska.webshop.catalogservice.model.Product;

@Component
public class ProductClient {

	private final Map<Long, Product> productCache = new LinkedHashMap<Long, Product>();

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getProductsCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Iterable<Product> getProducts() {
		Collection<Product> products = new HashSet<Product>();
		Product[] tmpProducts = restTemplate.getForObject("http://product-service/products", Product[].class);
		Collections.addAll(products, tmpProducts);
		productCache.clear();
		products.forEach(product -> productCache.put(product.getId(), product));
		return products;
	}
	
	@HystrixCommand(fallbackMethod = "getProductsForCategoryIdCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Iterable<Product> getProductsByCategoryId(long categoryId) {
		Collection<Product> products = new HashSet<Product>();
		Product[] tmpProducts = restTemplate.getForObject("http://product-service/products/categories/" + categoryId, Product[].class);
		Collections.addAll(products, tmpProducts);
		products.forEach(product -> productCache.putIfAbsent(product.getId(), product));
		return products;
	}

	@HystrixCommand(fallbackMethod = "getProductCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Product getProductById(long productId) {
		Product tmpProduct = restTemplate.getForObject("http://product-service/products/" + productId, Product.class);
		productCache.putIfAbsent(productId, tmpProduct);
		return tmpProduct;
	}

	@HystrixCommand(fallbackMethod = "getProductsForSearchValueCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Iterable<Product> getProductsForSearchValue(String searchDescription, double searchMinPrice,
			double searchMaxPrice) {
		Collection<Product> products = new HashSet<Product>();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://product-service/products/search")
				.queryParam("searchDescription", searchDescription).queryParam("searchMinPrice", searchMinPrice)
				.queryParam("searchMaxPrice", searchMaxPrice);
		Product[] tmpProducts = restTemplate.getForObject(builder.toUriString(), Product[].class);
		Collections.addAll(products, tmpProducts);
		products.forEach(product -> productCache.putIfAbsent(product.getId(), product));
		return products;
	}

	public long addProduct(Product product) {
		Product tmpProduct = restTemplate.postForObject("http://product-service/products", product, Product.class);
		productCache.putIfAbsent(tmpProduct.getId(), tmpProduct);
		return tmpProduct.getId();
	}

	public void deleteProduct(long productId) {
		restTemplate.delete("http://product-service/products/" + productId);
		productCache.remove(productId);
	}

	public void deleteProductsByCategoryId(long categoryId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://product-service/products")
				.queryParam("categoryId", categoryId);
		restTemplate.delete(builder.toUriString());

		Iterator<Entry<Long, Product>> productEntries = productCache.entrySet().iterator();

		while (productEntries.hasNext()) {
			Entry<Long, Product> entry = productEntries.next();
			if (entry.getValue().getCategoryId() == categoryId) {
				productEntries.remove();
			}
		}
	}

	public Iterable<Product> getProductsCache() {
		return productCache.values();
	}
	
	public Iterable<Product> getProductsForCategoryIdCache(long categoryId) {
		Collection<Product> products = new HashSet<Product>();
		for (Entry<Long, Product> entry : productCache.entrySet()) {
			Product product = entry.getValue();
			if (product.getCategoryId() == categoryId) {
				products.add(product);
			}
		}
		return products;
	}

	public Product getProductCache(long productId) {
		return productCache.getOrDefault(productId, new Product());
	}

	public Iterable<Product> getProductsForSearchValueCache(String searchDescription, double searchMinPrice,
			double searchMaxPrice) {
		Collection<Product> products = new HashSet<Product>();
		for (Entry<Long, Product> entry : productCache.entrySet()) {
			Product product = entry.getValue();
			if (product.getDetails().toLowerCase().contains(searchDescription.toLowerCase())
					&& product.getPrice() >= searchMaxPrice && product.getPrice() <= searchMaxPrice) {
				products.add(product);
			}
		}
		return products;
	}

}
