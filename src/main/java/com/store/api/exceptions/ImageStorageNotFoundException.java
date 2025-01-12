package com.store.api.exceptions;

public class ImageStorageNotFoundException extends RuntimeException{
    public ImageStorageNotFoundException(String message) {
        super(message);
    }

    public ImageStorageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
