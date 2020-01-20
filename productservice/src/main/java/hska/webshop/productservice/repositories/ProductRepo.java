package hska.webshop.productservice.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hska.webshop.productservice.model.Product;

public interface ProductRepo extends CrudRepository<Product, Long> {
	
	public Product findProductByName(String name);
	
	public List<Product> findProductsByCategoryId(long categoryId);
	
	public List<Product> findProductsByDetailsContainingIgnoreCaseAndPriceBetween(String searchDescription, Double searchMinPrice, Double searchMaxPrice);
}
