package com.naydenova.pharmacy_items.entities;

import com.naydenova.pharmacy_items.exceptions.AppException;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;

public enum PharmacyName {
    SOPHARMACY, SUBRA, REMEDIUM;

    public static PharmacyName getPharmacyByName(String pharmKey) {
        try {
            return PharmacyName.valueOf(pharmKey.trim().toUpperCase());
        } catch (NoSuchElementException ex) {
            throw new AppException("This pharmacy name is not supported!", HttpStatus.BAD_REQUEST);
        }
    }
}
