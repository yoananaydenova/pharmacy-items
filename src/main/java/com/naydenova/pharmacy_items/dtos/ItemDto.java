package com.naydenova.pharmacy_items.dtos;

public class ItemDto {

    private Long id;
    private String pharmacyName;
    private String itemName;
    private Double price;
    private String itemUrl;
    private String imageUrl;

    public ItemDto() {
    }

    public ItemDto(String pharmacyName, String itemName, Double price, String itemUrl, String imageUrl) {
        this.pharmacyName = pharmacyName;
        this.itemName = itemName;
        this.price = price;
        this.itemUrl = itemUrl;
        this.imageUrl = imageUrl;
    }

    public ItemDto(Long id, String pharmacyName, String itemName, Double price, String itemUrl, String imageUrl) {
        this.id = id;
        this.pharmacyName = pharmacyName;
        this.itemName = itemName;
        this.price = price;
        this.itemUrl = itemUrl;
        this.imageUrl = imageUrl;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
