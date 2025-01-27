package com.example.fssapi.exception;

public class ClientInformingException extends RuntimeException {
    public ClientInformingException(String message) {
        super(message);
    }

    public ClientInformingException(String message, Throwable cause) {

    }
}
