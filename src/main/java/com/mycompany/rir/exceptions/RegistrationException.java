package com.mycompany.rir.exceptions;

public class RegistrationException extends Exception {

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
