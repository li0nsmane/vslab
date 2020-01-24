package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataobjects.ApiProduct;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.SearchRequest;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductManagerImpl implements ProductManager {
	private static final String PRODUCT_URL = "http://zuul:8020/products";
	private static final String CAT_URL = "http://zuul:8020/categories";
	private final OAuth2RestTemplate oAuth2RestTemplate;
	
	public ProductManagerImpl() {
		oAuth2RestTemplate = Oauth.getOAuth2RestTemplate();
	}

	public List<Product> getProducts() {
		try {
			ApiProduct[] aps = oAuth2RestTemplate.getForEntity(PRODUCT_URL, ApiProduct[].class).getBody();
			List<Product> ps = new ArrayList<Product>();
			for(ApiProduct p: aps) {
				CategoryManagerImpl catManager = new CategoryManagerImpl();
				Category g = catManager.getCategory(p.getCategoryId());
				Product product = new Product(p.getName(), p.getPrice(), g, p.getDetails());
				product.setId(p.getId());
				ps.add(product);
			}
			return ps;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {
		try {

			ApiProduct[] aps = oAuth2RestTemplate.postForEntity(PRODUCT_URL+"/search", new SearchRequest(searchDescription, searchMinPrice, searchMaxPrice), ApiProduct[].class).getBody();


			List<Product> ps = new ArrayList<Product>();
			for(ApiProduct p: aps) {
				CategoryManagerImpl catManager = new CategoryManagerImpl();
				Category g = catManager.getCategory(p.getCategoryId());
				Product product = new Product(p.getName(), p.getPrice(), g, p.getDetails());
				product.setId(p.getId());
				ps.add(product);
			}
			return ps;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Product getProductById(int id){
		System.out.println("get product by id "+id);
		ApiProduct ap = oAuth2RestTemplate.getForEntity(PRODUCT_URL + "/" + id, ApiProduct.class).getBody();
		System.out.println("product_: "+ap.getName());
	CategoryManagerImpl catManager = new CategoryManagerImpl();
	Category g = catManager.getCategory(ap.getCategoryId());
		System.out.println("category: "+g.getName());
	Product product = new Product(ap.getName(), ap.getPrice(), g, ap.getDetails());
		product.setId(ap.getId());
		return product;
	}

	public Product getProductByName(String name) {

		List<ApiProduct> ps =
				new ArrayList<ApiProduct>(Arrays.asList(
						oAuth2RestTemplate.getForEntity(PRODUCT_URL, ApiProduct[].class).getBody()));
		for(ApiProduct p: ps) {
			if(p.getName() == name) {
				CategoryManagerImpl catManager = new CategoryManagerImpl();
				Category g = catManager.getCategory(p.getCategoryId());
				Product product = new Product(p.getName(), p.getPrice(), g, p.getDetails());
				return product;
			}
		}
		return null;
	}
	
	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;

		CategoryManagerImpl categoryManager = new CategoryManagerImpl();
		Category category = categoryManager.getCategory(categoryId);

		if(category != null){
			ApiProduct product= new ApiProduct(name, category.getId(), price);
			if(details != null){
				product.setDetails(details);
			}

			try {
				ApiProduct p = oAuth2RestTemplate.postForEntity(PRODUCT_URL, product, ApiProduct.class).getBody();
				productId = p.getId();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return productId;
	}
	

	public void deleteProductById(int id) {
		try {
			oAuth2RestTemplate.delete(PRODUCT_URL + "/" + id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		List<Product> ps =
				new ArrayList<Product>(Arrays.asList(
						oAuth2RestTemplate.getForEntity(
								CAT_URL + "/" + categoryId + "/products", Product[].class).getBody()));
		for(Product p: ps) {
			this.deleteProductById(p.getId());
		}
		return false;
	}

}
