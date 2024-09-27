package com.naydenova.pharmacy_items.dtos;

import java.util.List;

public class SearchDto {

    private String searchedText;
    private List<String> pharmacies;
    private int searchLimit;

    public SearchDto() {
    }

    public SearchDto(String searchedText, List<String> pharmacies, int searchLimit) {
        this.searchedText = searchedText;
        this.pharmacies = pharmacies;
        this.searchLimit = searchLimit;
    }

    public String getSearchedText() {
        return searchedText;
    }

    public SearchDto setSearchedText(String searchedText) {
        this.searchedText = searchedText;
        return this;
    }

    public List<String> getPharmacies() {
        return pharmacies;
    }

    public SearchDto setPharmacies(List<String> pharmacies) {
        this.pharmacies = pharmacies;
        return this;
    }

    public int getSearchLimit() {
        return searchLimit;
    }

    public SearchDto setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
        return this;
    }
}