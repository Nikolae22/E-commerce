package com.ecom.order.infrastructure.primary.order;

import com.ecom.order.domain.order.vo.StripeSessionId;
import org.jilt.Builder;


@Builder
public record RestStripeSession(String id) {

    public static RestStripeSession from(StripeSessionId stripeSessionId) {
        return RestStripeSessionBuilder.restStripeSession()
                .id(stripeSessionId.value())
                .build();
    }
}
