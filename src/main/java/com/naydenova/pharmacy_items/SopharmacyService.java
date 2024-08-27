package com.naydenova.pharmacy_items;

import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.NamedNodeMap;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SopharmacyService {

    public List<Item> findItems(String itemName) {
        List<String> domains = List.of("https://sopharmacy.bg/bg/sophSearch");

        return domains.stream().map(domain -> parseItems(domain, itemName)).flatMap(Collection::stream).collect(Collectors.toList());


    }

    private List<Item> parseItems(String domain, String itemName) {

        try (WebClient client = new WebClient()) {
            client.getOptions().setJavaScriptEnabled(false);
            client.getOptions().setCssEnabled(false);

            HtmlPage searchPage = client.getPage(domain);

            HtmlPage resultPage = typeSearchedWord(itemName, searchPage);

            List<Item> allItems = new ArrayList<>();
            String nextPageUrl = getNextPageUrl(resultPage);
            do {
                allItems.addAll(parseResults(resultPage));

                resultPage = client.getPage(nextPageUrl);

                nextPageUrl = getNextPageUrl(resultPage);
            } while (nextPageUrl != null);


            return allItems;

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HtmlPage typeSearchedWord(String itemName, HtmlPage searchPage) throws IOException {
        final HtmlForm form = searchPage.getFormByName("search_form_HeaderSearchBoxComponent");
        final HtmlInput inputField = form.getInputByName("text");
        final HtmlButton submitButton = (HtmlButton) form.getFirstByXPath("//button[@type='submit']");

        inputField.type("\"" + itemName + "\"");
        return submitButton.click();
    }

    private static String getNextPageUrl(HtmlPage resultPage) {
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


    private static List<Item> parseResults(HtmlPage resultPage) {
        final List<HtmlDivision> pageItems = resultPage.getByXPath("//div[@class='products-item ']");
        return pageItems.stream().map(SopharmacyService::createItem).collect(Collectors.toList());
    }

    private static Item createItem(HtmlDivision divItem) {
        final String name = divItem.querySelector("h2").getTextContent().trim();
        final String priceString = divItem.querySelector("strong.price--s").getTextContent().trim();
        final Double price = convertPrice(priceString);

        String imageUrlString =divItem.querySelector("img").getAttributes().getNamedItem("data-srcset").getTextContent();
        final String imageUrl = "https://sopharmacy.bg/" + imageUrlString;
        return new Item(name, price, imageUrl);
    }

    private static Double convertPrice(String priceString) {
        final String resultPrice = priceString.split("лв.")[0].replace(",", ".").replace(" ", "").trim();
        return Double.parseDouble(resultPrice);
    }
}

