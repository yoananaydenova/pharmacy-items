package com.naydenova.pharmacy_items;

import com.naydenova.pharmacy_items.service.PharmacyService;
import com.naydenova.pharmacy_items.service.SopharmacyPharmacyService;
import com.naydenova.pharmacy_items.service.SubraPharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:5173")
public class PharmacyItemsController {

    private final SopharmacyPharmacyService sopharmacyPharmacyService;
    private final SubraPharmacyService subraPharmacyService;


    @Autowired
    public PharmacyItemsController(SopharmacyPharmacyService sopharmacyPharmacyService, SubraPharmacyService subraPharmacyService) {
        this.sopharmacyPharmacyService = sopharmacyPharmacyService;
        this.subraPharmacyService = subraPharmacyService;
    }


    @GetMapping("/items")
    private ResponseEntity<List<Item>> findAll(@RequestParam List<Integer> pharms, @RequestParam String text) {
        final List<Item> items = pharms.stream()
                .map(pharmKey -> getService(pharmKey).parseItems(text)).flatMap(Collection::stream).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }


    private  PharmacyService getService(Integer pharmKey) {
        return switch (pharmKey){
            case 1 ->sopharmacyPharmacyService;
            case 2 -> subraPharmacyService;
            default -> throw new RuntimeException("This pharmacy is no supported!");
        };

    }


}
