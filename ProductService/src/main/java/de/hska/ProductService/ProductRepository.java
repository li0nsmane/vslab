package de.hska.ProductService;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    // named query to implement search
    public List<Product> findProductsByDetailsContainingIgnoreCaseAndPriceBetween(String searchDescription, Double searchMinPrice, Double searchMaxPrice);

    public List<Product> findProductsByCategoryId(long categoryId);

    public Product findProductByName(String name);

}
