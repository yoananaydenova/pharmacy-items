package com.naydenova.pharmacy_items.service;

import com.naydenova.pharmacy_items.Item;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.*;


public interface PharmacyService {

    abstract HtmlPage typeSearchedWord(String itemName, HtmlPage searchPage) throws IOException;

    abstract String getNextPageUrl(HtmlPage resultPage);

    abstract String getDomainUrl();

    abstract List<Item> parseResults(HtmlPage resultPage);

    public default List<Item> parseItems(String itemName) {

        try (WebClient client = new WebClient()) {
            client.getOptions().setJavaScriptEnabled(false);
            client.getOptions().setCssEnabled(false);

            final HtmlPage searchPage = client.getPage(getDomainUrl());

            final HtmlPage resultPage = typeSearchedWord(itemName, searchPage);

            return extractAllItems(client, resultPage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public default List<Item> extractAllItems(WebClient client, HtmlPage resultPage) throws IOException {

        String nextPageUrl = getNextPageUrl(resultPage);

        final List<Item> allItems = new ArrayList<>();

        do {
            allItems.addAll(parseResults(resultPage));

            if (nextPageUrl == null) {
                break;
            }

            resultPage = client.getPage(nextPageUrl);
            nextPageUrl = getNextPageUrl(resultPage);

        } while (nextPageUrl != null);

        return allItems;
    }
}
