package ch.wisv.payments.model;

import lombok.Getter;

public class ProductResponse {

    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private float price;
    @Getter
    private String key;
    @Getter
    private String committeeName;
    @Getter
    private Boolean available;

    public ProductResponse(String name, String description, float price, String key, String committeeName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.key = key;
        this.committeeName = committeeName;
    }

    public ProductResponse(Product product, boolean available) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.key = product.getKey();
        this.committeeName = product.getCommittee().getName().getName();
        this.available = available;
    }
}
