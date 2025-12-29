package com.ecom.order.domain.user.aggregate;

import com.ecom.order.domain.user.vo.AuthorityName;
import com.ecom.shared.error.domain.Assert;
import lombok.Getter;
import org.jilt.Builder;

@Getter
@Builder
public class Authority {

    private AuthorityName name;

    public Authority(AuthorityName authorityName) {
        Assert.notNull("name", authorityName);
        this.name = authorityName;
    }

}