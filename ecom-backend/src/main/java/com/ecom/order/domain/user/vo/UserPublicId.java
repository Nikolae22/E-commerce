package com.ecom.order.domain.user.vo;

import com.ecom.shared.error.domain.Assert;

import java.util.UUID;

public record UserPublicId(UUID value) {

    public UserPublicId {
        Assert.notNull("value", value);
    }
}