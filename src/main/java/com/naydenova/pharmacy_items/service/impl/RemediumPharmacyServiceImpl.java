package com.naydenova.pharmacy_items.service.impl;

import com.naydenova.pharmacy_items.Item;
import com.naydenova.pharmacy_items.service.RemediumPharmacyService;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RemediumPharmacyServiceImpl implements RemediumPharmacyService {

    public static final String SEARCH_DOMAIN = "https://remedium.bg/search?q=";
    private static final String PHARMACY_NAME = "Remedium";
    private static final String ITEM_XPATH = "//a[@class='LineItem__ItemLinkWrapper-sc-1imtm0n-0 gRUunT']";
    public static final String NEXT_PAGE_XPATH = "//a[@aria-label='Next page']";
    @Override
    public String getNextPageUrl(HtmlPage resultPage)  {
        final String nextPageUrl = RemediumPharmacyService.super.getNextPageUrl(resultPage);
        return nextPageUrl == null ? null : "https://remedium.bg" +nextPageUrl;
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
    public List<Item> parseResults(HtmlPage resultPage, String itemXpath) {

        final List<HtmlAnchor> pageItems = resultPage.getByXPath(itemXpath);
        return pageItems.stream().map(this::createItem).collect(Collectors.toList());
    }

    @Override
    public Item createItem(HtmlElement divItem) {
        final String itemName = divItem.querySelector("p.LineItem__ItemName-sc-1imtm0n-3").getTextContent().trim();

        final String priceString = divItem.querySelector("div.Price__RegularPrice-sc-14hy5o8-2").getTextContent().trim();
        final Double price = convertPrice(priceString);

        final String itemUrlString = divItem
                .getAttributes().getNamedItem("href").getTextContent();
        final String itemUrl = "https://remedium.bg" + itemUrlString;

        final String imageUrl = divItem.querySelector("img.LazyResponsiveImage__StyledImage-sc-5eewe7-1")
                .getAttributes().getNamedItem("src").getTextContent();

        return new Item(PHARMACY_NAME,itemName, price,itemUrl, imageUrl);
    }

    private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.replace("лв.", "").replace(",", ".").trim();
        return Double.parseDouble(resultPrice);
    }
}
