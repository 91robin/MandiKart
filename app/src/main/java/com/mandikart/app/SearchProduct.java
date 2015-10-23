package com.mandikart.app;

public class SearchProduct {
    private String id;
    private String name;
    private String brand_name;
    private String category_id;
    private String type;


    public SearchProduct(String id, String name, String brand_name, String category_id, String type) {
        this.id = id;
        this.name = name;
        this.brand_name = brand_name;
        this.category_id = category_id;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getBrandName() {
        return brand_name;
    }

    public String getName() {
        return this.name;
    }

    public String getCategoryId() {
        return category_id;
    }

    public String getType() {
        return type;
    }
}
