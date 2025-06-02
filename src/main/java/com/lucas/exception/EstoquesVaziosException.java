package com.lucas.exception;

public class EstoquesVaziosException extends RuntimeException {
    public EstoquesVaziosException(String message) {
        super(message);
    }
}
