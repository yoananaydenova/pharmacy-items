package com.naydenova.pharmacy_items.exceptions;

public class UnknownUserException extends RuntimeException{
    public UnknownUserException(){
        super("Unknown user!");
    }
}