package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.CreationCategory;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.oauth.OAuthConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CategoryManagerImpl implements CategoryManager {
	private static final String CATEGORY_URI = "http://zuul:8770/categories";

	public CategoryManagerImpl() {
	}

	public List<Category> getCategories() {
		ArrayList<Category> allCategories = new ArrayList<Category>();
		CreationCategory[] categories = OAuthConfig.getOAuth2RestTemplate().getForObject(CATEGORY_URI, CreationCategory[].class);
		for (CreationCategory c : categories) {
			Category category = new Category(c.getName());
			category.setId(c.getId());
			ProductManagerImpl pm = new ProductManagerImpl();
			HashSet<Product> productsForCategoryId = pm.getProductsForCategory(category);
			category.setProducts(productsForCategoryId);
			allCategories.add(category);
		}
		return allCategories;
	}

	public Category getCategory(int id) {
		CreationCategory tmpCategory = OAuthConfig.getOAuth2RestTemplate().getForObject(CATEGORY_URI + "/{id}", CreationCategory.class, id);
		Category category = new Category(tmpCategory.getName());
		category.setId(tmpCategory.getId());
		ProductManagerImpl pm = new ProductManagerImpl();
		HashSet<Product> productsForCategoryId = pm.getProductsForCategory(category);
		category.setProducts(productsForCategoryId);
		return category;
	}

	public long addCategory(String name) {
		long id = -1;
		CreationCategory creationCategory = new CreationCategory(name);
		try {
			System.out.println(OAuthConfig.getDefaultRestTemplate());
			id = OAuthConfig.getOAuth2RestTemplate().postForObject(CATEGORY_URI, creationCategory, Long.class);
			System.out.println(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public void delCategoryById(int id) {
		OAuthConfig.getOAuth2RestTemplate().delete(CATEGORY_URI + "/{id}", id);
	}
}
