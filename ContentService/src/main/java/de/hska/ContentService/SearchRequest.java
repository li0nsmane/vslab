package de.hska.ContentService;

public class SearchRequest {

    private String description;
    private double minPrice;
    private double maxPrice;

    public SearchRequest(String description, double minPrice, double maxPrice) {
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
