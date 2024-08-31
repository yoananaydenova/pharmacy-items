package com.naydenova.pharmacy_items.service.impl;

import com.naydenova.pharmacy_items.Item;
import com.naydenova.pharmacy_items.service.PharmacyService;
import com.naydenova.pharmacy_items.service.SubraPharmacyService;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubraPharmacyServiceImpl implements SubraPharmacyService {

    private static final List<String> OTHER_PRICE_STATUS = List.of("Изчерпан", "Очаква доставка");
    private static final String PHARMACY_NAME = "Subra";
    public static final String SEARCH_DOMAIN = "https://subra.bg/bg/-base-/5/1?search=%s&sort=price_asc";
    public static final String ITEM_XPATH = "//div[@class='product-card product-block product-block_backend']";

    public static final String NEXT_PAGE_XPATH = "//a[@class='button pagination__list-last center-arrow']";


    private long limit = 10;

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
        final String itemName = divItem.querySelector("a.product-card__text-link").getTextContent().trim();

        final String priceString = divItem.querySelector("div.product-card__price").getTextContent().trim();



        final Double price = OTHER_PRICE_STATUS.stream().anyMatch(w -> w.equals(priceString)) ? null : convertPrice(
                divItem.querySelector("span.product-card__price-value").getTextContent().trim()
        );

        final String itemUrl = divItem.querySelector("a.product-card__text-link").getAttributes().getNamedItem("href").getTextContent();

        final String imageUrlString = divItem.querySelector("img").getAttributes().getNamedItem("data-src").getTextContent();
        final String imageUrl = "https://subra.bg/" + imageUrlString;

        return new Item(PHARMACY_NAME, itemName, price, itemUrl, imageUrl);
    }

    private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.replace(",", ".").trim();
        return Double.parseDouble(resultPrice);
    }
}
