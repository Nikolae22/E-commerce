package com.ecom.product.domain.vo;

import com.ecom.shared.error.domain.Assert;

public record ProductName(String value) {

    public ProductName {
        Assert.notNull("value",value);
        Assert.field("value",value).notNull().maxLength(256);

    }
}
