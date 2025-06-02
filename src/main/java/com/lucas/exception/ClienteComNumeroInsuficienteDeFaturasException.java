package com.lucas.exception;

public class ClienteComNumeroInsuficienteDeFaturasException extends RuntimeException {
    public ClienteComNumeroInsuficienteDeFaturasException(String message) {
        super(message);
    }
}
