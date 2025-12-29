package com.ecom.order.domain.user.vo;


import com.ecom.shared.error.domain.Assert;

public record UserEmail(String value) {

    public UserEmail {
        Assert.field("value", value).maxLength(255);
    }
}