package com.naydenova.pharmacy_items.service;

import com.naydenova.pharmacy_items.Item;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public interface PharmacyService {

    String getSearchDomainUrl();

    String getItemXpath();

    String getNextPageXpath();

    Item createItem(HtmlElement divItem);


    default List<Item> parseResults(HtmlPage resultPage, String itemXpath) {

        final List<HtmlDivision> pageItems = resultPage.getByXPath(itemXpath);
        return pageItems.stream().map(this::createItem).collect(Collectors.toList());
    }


    default String getNextPageUrl(HtmlPage resultPage) {

        final String nextPageXpath = getNextPageXpath();
        final List<Object> nextPageAnchors = resultPage.
                getByXPath(nextPageXpath);

        if (nextPageAnchors.isEmpty()) {
            return null;
        }

        final HtmlAnchor anchor = (HtmlAnchor) resultPage.
                getByXPath(nextPageXpath).get(0);
        return anchor.getHrefAttribute();
    }


    default List<Item> parseItems(String itemName) {

        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);

            final HtmlPage resultPage = webClient.getPage(getSearchDomainUrl() + itemName);

            return extractAllItems(webClient, resultPage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default List<Item> extractAllItems(WebClient client, HtmlPage resultPage) throws IOException {

        String nextPageUrl = getNextPageUrl(resultPage);

        final List<Item> allItems = new ArrayList<>();

        do {
            allItems.addAll(parseResults(resultPage, getItemXpath()));

            if (nextPageUrl == null) {
                break;
            }

            resultPage = client.getPage(nextPageUrl);
            nextPageUrl = getNextPageUrl(resultPage);

        } while (nextPageUrl != null);

        return allItems;
    }


}
