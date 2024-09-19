package com.naydenova.pharmacy_items.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String pharmacyName;

    @Column
    private String itemName;

    @Column
    private Double price;

    @Column(unique = true)
    private String itemUrl;

    @Column
    private String imageUrl;

    //    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "app_user_item_mapping", joinColumns = @JoinColumn(name = "item_id"),
//            inverseJoinColumns = @JoinColumn(name = "app_user_id"))
    @ManyToMany(mappedBy = "favorites")
    private Set<User> users = new HashSet<>();


    public Item() {
    }

    public Item(String pharmacyName, String itemName, Double price, String itemUrl, String imageUrl) {
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


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
