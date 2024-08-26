package com.naydenova.pharmacy_items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
public class PharmacyItemsController {

    private final SopharmacyService service;

    @Autowired
    public PharmacyItemsController(SopharmacyService service) {
        this.service = service;
    }

    @GetMapping("/items/{itemName}")
    private ResponseEntity<List<Item>> findAll(@PathVariable String itemName) {
        List<Item> items = service.findItems(itemName);
        return ResponseEntity.ok(items);
    }

}
