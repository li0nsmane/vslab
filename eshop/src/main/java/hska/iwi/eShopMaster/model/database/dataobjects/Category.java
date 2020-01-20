package hska.iwi.eShopMaster.model.database.dataobjects;


import java.util.HashSet;
import java.util.Set;

public class Category {

	private int category_id;
	private String name;
	private Set<Product> products = new HashSet<Product>(0);

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, Set<Product> products) {
		this.name = name;
		this.products = products;
	}

	public int getCategoryId() {
		return this.category_id;
	}

	public void setCategoryId(int id) {
		this.category_id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
