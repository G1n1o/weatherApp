package org.example.model.exceptions;

public class LocationNotFoundException extends Exception {

    public LocationNotFoundException(String message) {
        super(message);
    }

    public LocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}