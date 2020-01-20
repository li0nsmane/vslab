package de.hska.CategoryService;

import org.springframework.data.repository.CrudRepository;

import java.util.Locale;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    public Category findCategoryByName(String name);
}
