package hska.webshop.catalogservice.clients;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import hska.webshop.catalogservice.model.Category;

@Component
public class CategoryClient {

	private final Map<Long, Category> categoryCache = new LinkedHashMap<Long, Category>();

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getCategoriesCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Iterable<Category> getCategories() {
		Collection<Category> categories = new HashSet<Category>();
		Category[] tmpCategories = restTemplate.getForObject("http://category-service/categories", Category[].class);
		Collections.addAll(categories, tmpCategories);
		categoryCache.clear();
		categories.forEach(category -> categoryCache.put(category.getId(), category));
		return categories;
	}

	@HystrixCommand(fallbackMethod = "getCategoryCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Category getCategoryById(long categoryId) {
		Category tmpCategory = restTemplate.getForObject("http://category-service/categories/" + categoryId,
				Category.class);
		categoryCache.putIfAbsent(categoryId, tmpCategory);
		return tmpCategory;
	}

	public long addCategory(Category category) {
		Category tmpCategory = restTemplate.postForObject("http://category-service/categories", category,
				Category.class);
		categoryCache.putIfAbsent(tmpCategory.getId(), tmpCategory);
		return tmpCategory.getId();
	}

	public void deleteCategory(long categoryId) {
		restTemplate.delete("http://category-service/categories/" + categoryId);
		categoryCache.remove(categoryId);
	}

	public Iterable<Category> getCategoriesCache() {
		return categoryCache.values();
	}

	public Category getCategoryCache(long categoryId) {
		return categoryCache.getOrDefault(categoryId, new Category());
	}
}
