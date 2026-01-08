package com.ecom.product.domain.vo;

import com.ecom.shared.error.domain.Assert;

import java.util.UUID;

public record PublicId(UUID value) {

    public PublicId {
        Assert.notNull("value",value);
    }
}
