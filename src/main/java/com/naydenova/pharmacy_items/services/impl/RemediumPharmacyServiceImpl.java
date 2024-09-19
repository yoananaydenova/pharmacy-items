package com.naydenova.pharmacy_items.services.impl;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.services.PharmacyService;
import com.naydenova.pharmacy_items.services.RemediumPharmacyService;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;


@Service
public class RemediumPharmacyServiceImpl implements RemediumPharmacyService {

    public static final String SEARCH_DOMAIN = "https://remedium.bg/search?q=%s&sort=price-asc&#";
    private static final String PHARMACY_NAME = "Remedium";
    private static final String ITEM_XPATH = "//a[@class='LineItem__ItemLinkWrapper-sc-1imtm0n-0 gRUunT']";
    public static final String NEXT_PAGE_XPATH = "//a[@aria-label='Next page']";

    private long limit = DEFAULT_LIMIT;

    @Override
    public String getNextPageUrl(HtmlPage resultPage) {
        final String nextPageUrl = RemediumPharmacyService.super.getNextPageUrl(resultPage);
        return nextPageUrl == null ? null : "https://remedium.bg" + nextPageUrl;
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
    public ItemDto createItem(HtmlElement divItem) {
        final String itemName = divItem.querySelector("p.LineItem__ItemName-sc-1imtm0n-3").getTextContent().trim();

        HtmlElement priceElement = divItem.querySelector("div.Price__RegularPrice-sc-14hy5o8-2");
        if (priceElement == null) {
            priceElement = divItem.querySelector("strong.Price__DiscountedPrice-sc-14hy5o8-7");
        }
        final Double price = convertPrice(priceElement.getTextContent().trim());

        final String itemUrlString = divItem
                .getAttributes().getNamedItem("href").getTextContent();
        final String itemUrl = "https://remedium.bg" + itemUrlString;

        final String imageUrl = divItem.querySelector("img.LazyResponsiveImage__StyledImage-sc-5eewe7-1")
                .getAttributes().getNamedItem("src").getTextContent();

        return new ItemDto(PHARMACY_NAME, itemName, price, itemUrl, imageUrl);
    }

    private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.replace("лв.", "").replace(",", ".").trim();
        return Double.parseDouble(resultPrice);
    }
}
