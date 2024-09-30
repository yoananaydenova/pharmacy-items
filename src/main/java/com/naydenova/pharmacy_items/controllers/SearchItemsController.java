package com.naydenova.pharmacy_items.controllers;

import com.naydenova.pharmacy_items.dtos.ItemDto;
import com.naydenova.pharmacy_items.entities.PharmacyName;
import com.naydenova.pharmacy_items.exceptions.AppException;
import com.naydenova.pharmacy_items.services.PharmacyService;
import com.naydenova.pharmacy_items.services.RemediumPharmacyService;
import com.naydenova.pharmacy_items.services.SopharmacyPharmacyService;
import com.naydenova.pharmacy_items.services.SubraPharmacyService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class SearchItemsController {

    private final SopharmacyPharmacyService sopharmacyPharmacyService;
    private final SubraPharmacyService subraPharmacyService;
    private final RemediumPharmacyService remediumPharmacyService;


    @Autowired
    public SearchItemsController(SopharmacyPharmacyService sopharmacyPharmacyService, SubraPharmacyService subraPharmacyService, RemediumPharmacyService remediumPharmacyService) {
        this.sopharmacyPharmacyService = sopharmacyPharmacyService;
        this.subraPharmacyService = subraPharmacyService;
        this.remediumPharmacyService = remediumPharmacyService;
    }

    @GetMapping("/search")
    private ResponseEntity<List<ItemDto>> findAll(@RequestParam List<String> pharms, @RequestParam @Min(1) int limit, @RequestParam @NotBlank String text) {
        if (pharms == null || pharms.isEmpty()) {
            throw new AppException("The pharmacy list must have at lest 1 item!", HttpStatus.BAD_REQUEST);
        }

        final List<ItemDto> items = pharms.parallelStream()
                .map(pharmKey -> getService(pharmKey)
                        .setLimit(limit).parseItems(text))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ItemDto::getPrice, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    private PharmacyService getService(String pharmKey) {

        if (pharmKey.isBlank()) {
            throw new AppException("The pharmacy name must be present!", HttpStatus.BAD_REQUEST);
        }

        final PharmacyName pharmacyName = PharmacyName.getPharmacyByName(pharmKey);

        return switch (pharmacyName) {
            case SOPHARMACY -> sopharmacyPharmacyService;
            case SUBRA -> subraPharmacyService;
            case REMEDIUM -> remediumPharmacyService;
        };
    }



}
