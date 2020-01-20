package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.CreationProduct;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.oauth.OAuthConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

public class ProductManagerImpl implements ProductManager {
	private static final String PRODUCT_URI = "http://zuul:8770/products";

	public ProductManagerImpl() {
	}

	public List<Product> getProducts() {
		ArrayList<Product> allProducts = new ArrayList<Product>();
		CreationProduct[] products = OAuthConfig.getOAuth2RestTemplate().getForObject(PRODUCT_URI, CreationProduct[].class);
		for (CreationProduct p : products) {
			CategoryManagerImpl cm = new CategoryManagerImpl();
			Category category = cm.getCategory(p.getCategoryId());
			Product product = new Product(p.getName(), p.getPrice(), category, p.getDetails());
			product.setId(p.getId());
			allProducts.add(product);
		}
		return allProducts;
	}

	public HashSet<Product> getProductsForCategory(Category category) {
		HashSet<Product> productsForCategoryId = new HashSet<Product>();
		CreationProduct[] allProducts = OAuthConfig.getOAuth2RestTemplate().getForObject(PRODUCT_URI + "/category/" + category.getId(),
				CreationProduct[].class);
		for (CreationProduct p : allProducts) {
			Product product = new Product(p.getName(), p.getPrice(), category, p.getDetails());
			product.setId(p.getId());
			productsForCategoryId.add(product);
		}
		return productsForCategoryId;
	}

	public List<Product> getProductsForSearchValues(String searchDescription, Double searchMinPrice,
			Double searchMaxPrice) {

		ArrayList<Product> productsForSearchValues = new ArrayList<Product>();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(PRODUCT_URI + "/search")
				.queryParam("searchDescription", searchDescription).queryParam("searchMinPrice", searchMinPrice)
				.queryParam("searchMaxPrice", searchMaxPrice);

		CreationProduct[] products = OAuthConfig.getOAuth2RestTemplate().getForObject(builder.toUriString(), CreationProduct[].class);

		for (CreationProduct p : products) {

			CategoryManagerImpl cm = new CategoryManagerImpl();
			Category category = cm.getCategory(p.getCategoryId());
			Product product = new Product(p.getName(), p.getPrice(), category, p.getDetails());
			product.setId(p.getId());
			productsForSearchValues.add(product);
		}
		return productsForSearchValues;
	}

	public Product getProductById(int id) {
		CreationProduct tmpProduct = OAuthConfig.getOAuth2RestTemplate().getForObject(PRODUCT_URI + "/{id}", CreationProduct.class, id);
		CategoryManagerImpl cm = new CategoryManagerImpl();
		Category category = cm.getCategory(tmpProduct.getCategoryId());
		Product product = new Product(tmpProduct.getName(), tmpProduct.getPrice(), category, tmpProduct.getDetails());
		product.setId(tmpProduct.getId());
		return product;
	}

	public long addProduct(String name, double price, int categoryId, String details) {
		long productId = -1;
		CreationProduct product = new CreationProduct(name, price, categoryId, details);
		try {
			productId = OAuthConfig.getOAuth2RestTemplate().postForObject(PRODUCT_URI, product, Long.class);
			return productId;
		} catch (Exception e) {
			e.printStackTrace();
			return productId;
		}
	}

	public void deleteProductById(int id) {
		OAuthConfig.getOAuth2RestTemplate().delete(PRODUCT_URI + "/{id}", id);
	}
}
