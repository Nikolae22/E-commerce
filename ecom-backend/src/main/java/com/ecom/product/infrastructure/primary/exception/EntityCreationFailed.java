package com.ecom.product.infrastructure.primary.exception;

public class EntityCreationFailed extends RuntimeException {
    public EntityCreationFailed(String message) {
        super(message);
    }
}
