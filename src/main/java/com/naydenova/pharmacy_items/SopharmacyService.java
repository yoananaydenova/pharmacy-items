package com.naydenova.pharmacy_items;

import org.htmlunit.WebClient;
import org.htmlunit.html.*;
import org.springframework.stereotype.Service;


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
        HtmlForm form = searchPage.getFormByName("search_form_HeaderSearchBoxComponent");
        HtmlInput inputField = form.getInputByName("text");
        HtmlButton submitButton = (HtmlButton) form.getFirstByXPath("//button[@type='submit']");

        inputField.type("\""+itemName+"\"");
        return submitButton.click();
    }

    private static String getNextPageUrl(HtmlPage resultPage) {
        List<Object> nextPageAnchors = resultPage.
                getByXPath("//a[@class='pagination__arrow']");

        if (nextPageAnchors.isEmpty()) {
            return null;
        }

        HtmlAnchor anchor = (HtmlAnchor) resultPage.
                getByXPath("//a[@class='pagination__arrow']").get(0);
        String hrefAttribute = anchor.getHrefAttribute();
        return "https://sopharmacy.bg/bg/sophSearch/" + hrefAttribute;

    }


    private static List<Item> parseResults(HtmlPage resultPage) {
        List<HtmlDivision> pageItems = resultPage.getByXPath("//div[@class='products-item ']");

        return pageItems.stream().map(item -> {
            String name = item.querySelector("h2").getTextContent().trim();
            String priceString = item.querySelector("strong.price--s").getTextContent().trim();
            Double price = convertPrice(priceString);

            return new Item(name, price);
        }).collect(Collectors.toList());
    }

    private static Double convertPrice(String priceString) {
//        String resultPrice = priceString
//                .substring(0, priceString.length() - 3).trim()
//                .replace(",", ".").replace(" ", "").trim();

        String resultPrice = priceString.split("лв.")[0].replace(",", ".").replace(" ", "").trim();

        return Double.parseDouble(resultPrice);
    }
}

