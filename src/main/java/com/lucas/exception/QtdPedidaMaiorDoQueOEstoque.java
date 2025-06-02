package com.lucas.exception;

public class QtdPedidaMaiorDoQueOEstoque extends RuntimeException {
    public QtdPedidaMaiorDoQueOEstoque(String message) {
        super(message);
    }
}
