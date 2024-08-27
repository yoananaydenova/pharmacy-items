package com.naydenova.pharmacy_items.service.impl;

import com.naydenova.pharmacy_items.Item;
import com.naydenova.pharmacy_items.service.SopharmacyPharmacyService;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class SopharmacyPharmacyServiceImpl implements SopharmacyPharmacyService {

    public static final String DOMAIN = "https://sopharmacy.bg/bg/sophSearch";
    private static final String PHARMACY_NAME = "SOpharmacy";

    public HtmlPage typeSearchedWord(String itemName, HtmlPage searchPage) throws IOException {
        final HtmlForm form = searchPage.getFormByName("search_form_HeaderSearchBoxComponent");
        final HtmlInput inputField = form.getInputByName("text");
        final HtmlButton submitButton = (HtmlButton) form.getFirstByXPath("//button[@type='submit']");

        inputField.type(itemName);
        return submitButton.click();
    }

    public String getNextPageUrl(HtmlPage resultPage)  {
        final List<Object> nextPageAnchors = resultPage.
                getByXPath("//a[@class='pagination__arrow']");

        if (nextPageAnchors.isEmpty()) {
            return null;
        }

        final HtmlAnchor anchor = (HtmlAnchor) resultPage.
                getByXPath("//a[@class='pagination__arrow']").get(0);
        final String hrefAttribute = anchor.getHrefAttribute();
        return "https://sopharmacy.bg/bg/sophSearch/" + hrefAttribute;

    }

    @Override
    public String getDomainUrl() {
        return DOMAIN;
    }


    public  List<Item> parseResults(HtmlPage resultPage) {
        final List<HtmlDivision> pageItems = resultPage.getByXPath("//div[@class='products-item ']");
        return pageItems.stream().map(SopharmacyPharmacyServiceImpl::createItem).collect(Collectors.toList());
    }

    private static Item createItem(HtmlDivision divItem) {
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

