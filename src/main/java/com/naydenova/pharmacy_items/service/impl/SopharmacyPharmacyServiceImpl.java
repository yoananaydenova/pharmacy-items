package com.naydenova.pharmacy_items.service.impl;

import com.naydenova.pharmacy_items.Item;
import com.naydenova.pharmacy_items.service.PharmacyService;
import com.naydenova.pharmacy_items.service.SopharmacyPharmacyService;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;

@Service
public class SopharmacyPharmacyServiceImpl implements SopharmacyPharmacyService {

    public static final String SEARCH_DOMAIN = "https://sopharmacy.bg/bg/sophSearch/?text=";
    private static final String PHARMACY_NAME = "SOpharmacy";
    private static final String ITEM_XPATH = "//div[@class='products-item ']";
    public static final String NEXT_PAGE_XPATH = "//a[@class='pagination__arrow']";

    private long limit = 10;

    @Override
    public String getNextPageUrl(HtmlPage resultPage)  {
       final String nextPageUrl = SopharmacyPharmacyService.super.getNextPageUrl(resultPage);
        return nextPageUrl == null ? null : "https://sopharmacy.bg/bg/sophSearch/" +nextPageUrl;
    }

    @Override
    public String getSearchDomainUrl() {
        return SEARCH_DOMAIN;
    }

    @Override
    public String getItemXpath() {
        return ITEM_XPATH;
    }

    @Override
    public String getNextPageXpath() {
        return NEXT_PAGE_XPATH;
    }

    @Override
    public PharmacyService setLimit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public long getLimit() {
        return this.limit;
    }

    @Override
    public Item createItem(HtmlElement divItem) {
        final String itemName = divItem.querySelector("h2").getTextContent().trim();

        final String priceString = divItem.querySelector("strong.price--s").getTextContent().trim();
        final Double price = convertPrice(priceString);

        final String itemUrlString = divItem.querySelector("a").getAttributes().getNamedItem("href").getTextContent();
        final String itemUrl = "https://sopharmacy.bg" + itemUrlString;

        final String imageUrlString = divItem.querySelector("img").getAttributes().getNamedItem("data-srcset").getTextContent();
        final String imageUrl = "https://sopharmacy.bg" + imageUrlString;

        return new Item(PHARMACY_NAME,itemName, price,itemUrl, imageUrl);
    }

   private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.split("лв.")[0].replace(",", ".").replace(" ", "").trim();
        return Double.parseDouble(resultPrice);
    }
}

