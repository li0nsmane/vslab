package de.hska.ContentService.data;

public class Product {

    private long id;
    private String name;
    private double price;
    private long categoryId;
    private String details;

    public Product() {
    }

    public Product(String name, double price, long categoryId, String details) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.details = details;
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getCategoryId() {
        return this.categoryId;
    }

    public void setCategory(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
