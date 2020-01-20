package hska.webshop.categoryservice.repositories;

import org.springframework.data.repository.CrudRepository;

import hska.webshop.categoryservice.model.Category;

public interface CategoryRepo extends CrudRepository<Category, Long> {
	
	public Category findCategoryByName(String name);
}
