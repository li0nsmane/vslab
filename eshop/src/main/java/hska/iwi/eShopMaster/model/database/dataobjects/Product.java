package hska.iwi.eShopMaster.model.database.dataobjects;

public class Product {

	private int product_id;

	private String name;

	private double price;

	private Category category;

	private String details;

	public Product() {
	}

	public Product(String name, double price, Category category) {
		this.name = name;
		this.price = price;
		this.category = category;
	}

	public Product(String name, double price, Category categoryId, String details) {
		this.name = name;
		this.price = price;
		this.category = category;
		this.details = details;
	}

	public int getProductId() {
		return this.product_id;
	}

	public void setProductId(int id) {
		this.product_id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategoryId(Category category) {
		this.category = category;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
