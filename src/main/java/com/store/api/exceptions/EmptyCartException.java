package com.store.api.exceptions;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}

