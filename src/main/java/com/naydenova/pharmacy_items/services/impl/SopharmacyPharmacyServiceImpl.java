package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.services.PharmacyService;
import com.naydenova.pharmacy_items.services.SopharmacyPharmacyService;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.util.List;

@Service
public class SopharmacyPharmacyServiceImpl implements SopharmacyPharmacyService {

    public static final String SEARCH_DOMAIN = "https://sopharmacy.bg/bg/sophSearch/?text=%s";
    private static final String PHARMACY_NAME = "SOpharmacy";
    private static final List<String> ITEM_XPATHS = List.of("//div[@class='products-item discount']", "//div[@class='products-item ']");
    public static final String NEXT_PAGE_XPATH = "//a[@class='pagination__arrow']";

    private int limit = DEFAULT_LIMIT;

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
    public List<String> getItemXpaths() {
        return ITEM_XPATHS;
    }

    @Override
    public String getNextPageXpath() {
        return NEXT_PAGE_XPATH;
    }

    @Override
    public PharmacyService setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public int getLimit() {
        return this.limit;
    }

    @Override
    public ItemDto createItem(HtmlElement divItem) {
        final String itemName = divItem.querySelector("h2").getTextContent().trim();

        final String priceString = divItem.querySelector("strong.price--s").getTextContent().trim();
        final Double price = convertPrice(priceString);

        final String itemUrlString = divItem.querySelector("a").getAttributes().getNamedItem("href").getTextContent();
        final String itemUrl = "https://sopharmacy.bg" + itemUrlString;

        final Node imgNode = divItem.querySelector("img").getAttributes().getNamedItem("data-srcset");
        final String imageUrl = imgNode == null? null :"https://sopharmacy.bg" + imgNode.getTextContent();


        return new ItemDto(PHARMACY_NAME,itemName, price,itemUrl, imageUrl);
    }

   private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.split("лв.")[0].replace(",", ".").replace(" ", "").trim();
        return Double.parseDouble(resultPrice);
    }
}

