package com.naydenova.pharmacy_items.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "search",
        uniqueConstraints = { @UniqueConstraint(
                name = "UniqueTextPharmaciesLimit",
                columnNames = { "searchedText", "pharmacies","searchLimit" }) })
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String searchedText;

    @Column
    @Enumerated(EnumType.STRING)
    private List<PharmacyName> pharmacies;
    @Column
    private int searchLimit;

    public Search() {
    }

    public Search(String searchedText, List<PharmacyName> pharmacies, int searchLimit) {
        this.searchedText = searchedText;
        this.pharmacies = pharmacies;
        this.searchLimit = searchLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }

    public List<PharmacyName> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(List<PharmacyName> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public int getSearchLimit() {
        return searchLimit;
    }

    public void setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
    }
}
