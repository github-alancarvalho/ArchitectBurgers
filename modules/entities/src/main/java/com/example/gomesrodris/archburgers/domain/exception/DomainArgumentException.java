package com.example.gomesrodris.archburgers.domain.exception;

public class DomainArgumentException extends IllegalArgumentException {
    public DomainArgumentException(String message) {
        super(message);
    }
}
