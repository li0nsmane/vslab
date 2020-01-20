package hska.iwi.eShopMaster.controller.manager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import hska.iwi.eShopMaster.controller.oauth.Oauth;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;

public class CategoryManagerImpl {


	private static final String CAT_URL = "http://zuul:8020/categories";
	private final OAuth2RestTemplate oAuth2RestTemplate;
	
	public CategoryManagerImpl() {
		oAuth2RestTemplate = Oauth.getOAuth2RestTemplate();
	}

	public List<Category> getCategories() {
		try {
			Category[] cats = oAuth2RestTemplate.getForEntity(CAT_URL, Category[].class).getBody();
			return new ArrayList<Category>(Arrays.asList(cats));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Category getCategory(int id) {
		return oAuth2RestTemplate.getForEntity(CAT_URL + "/" + id, Category.class).getBody();
	}

	public Category getCategoryByName(String name) {
		OAuth2RestTemplate oAuth2RestTemplate = Oauth.getOAuth2RestTemplate();
	
		List<Category> cats =
				new ArrayList<Category>(Arrays.asList(
						oAuth2RestTemplate.getForEntity(CAT_URL, Category[].class).getBody()));
		for(Category c: cats) {
			if(c.getName() == name) {
				return c;
			}
		}
		return null;
	}

	public void addCategory(String name) {
		try {
			oAuth2RestTemplate.postForEntity(CAT_URL, name, Category.class);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delCategory(Category cat) {
		this.delCategoryById(cat.getCategoryId());
	}

	public void delCategoryById(int id) {
		try {
			
			oAuth2RestTemplate.delete(CAT_URL + "/" + id);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
