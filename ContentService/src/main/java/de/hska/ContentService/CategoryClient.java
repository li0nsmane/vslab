package de.hska.ContentService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import de.hska.ContentService.data.Category;
import de.hska.ContentService.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class CategoryClient {


    private final Map<Long, Category> cache = new LinkedHashMap<Long, Category>();

    @Autowired
    private RestTemplate restTemplate;

    private final String CATEGORY_URL = "http://category-service/categories";


    @HystrixCommand(fallbackMethod = "getCategoriesFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Iterable<Category> getCategories() {

        Category[] cats = restTemplate.getForObject(CATEGORY_URL, Category[].class);
        cache.clear();
        for (Category c: cats) {
            cache.put(c.getId(), c);
        }
       return Arrays.asList(cats);
    }

    @HystrixCommand(fallbackMethod = "getCategoryByIdFromCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Category getCategoryById(long categoryId) {
        Category cat= restTemplate.getForObject(CATEGORY_URL +"/"+ categoryId,
                Category.class);

        return cat;
    }

    public Category addCategory(Category category) {
        Category cat = restTemplate.postForObject(CATEGORY_URL, category,
                Category.class);
        cache.putIfAbsent(cat.getId(), cat);
        return cat;
    }

    public void deleteCategory(long categoryId) {
        restTemplate.delete(CATEGORY_URL+"/" + categoryId);
        cache.remove(categoryId);

    }

    public Iterable<Category> getCategoriesFromCache() {
        return cache.values();
    }

    public Category getCategoryByIdFromCache(long catId) {
        return cache.getOrDefault(catId, new Category());
    }

}
