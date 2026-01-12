package com.ecom.order.domain.order.exception;

public class CartPaymentException extends RuntimeException {
    public CartPaymentException(String message) {
        super(message);
    }
}
