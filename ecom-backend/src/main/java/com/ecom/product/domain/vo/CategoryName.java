package com.ecom.product.domain.vo;

import com.ecom.shared.error.domain.Assert;

public record CategoryName(
        String value
) {

    public CategoryName {
        Assert.field("value",value).notNull().minLength(3);
    }
}
