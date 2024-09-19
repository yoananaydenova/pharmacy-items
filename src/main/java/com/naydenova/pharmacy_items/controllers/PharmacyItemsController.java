package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.services.PharmacyService;
import com.naydenova.pharmacy_items.services.RemediumPharmacyService;
import com.naydenova.pharmacy_items.services.SopharmacyPharmacyService;
import com.naydenova.pharmacy_items.services.SubraPharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class PharmacyItemsController {

    private final SopharmacyPharmacyService sopharmacyPharmacyService;
    private final SubraPharmacyService subraPharmacyService;
    private final RemediumPharmacyService remediumPharmacyService;


    @Autowired
    public PharmacyItemsController(SopharmacyPharmacyService sopharmacyPharmacyService, SubraPharmacyService subraPharmacyService, RemediumPharmacyService remediumPharmacyService) {
        this.sopharmacyPharmacyService = sopharmacyPharmacyService;
        this.subraPharmacyService = subraPharmacyService;
        this.remediumPharmacyService = remediumPharmacyService;
    }

    @GetMapping("/search")
    private ResponseEntity<List<ItemDto>> findAll(@RequestParam List<Integer> pharms, @RequestParam long limit, @RequestParam String text) {
        final List<ItemDto> items = pharms.parallelStream()
                .map(pharmKey -> getService(pharmKey)
                        .setLimit(limit).parseItems(text))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ItemDto::getPrice, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    private PharmacyService getService(Integer pharmKey) {
        return switch (pharmKey) {
            case 1 -> sopharmacyPharmacyService;
            case 2 -> subraPharmacyService;
            case 3 -> remediumPharmacyService;
            default -> throw new RuntimeException("This pharmacy is no supported!");
        };

    }


}
