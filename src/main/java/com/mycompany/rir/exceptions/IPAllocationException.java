package com.mycompany.rir.exceptions;

public class IPAllocationException extends Exception {

    public IPAllocationException(String message) {
        super(message);
    }

    public IPAllocationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}