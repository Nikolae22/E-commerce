package com.ecom.order.infrastructure.primary.order;

import com.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.ecom.order.domain.order.aggregate.DetailCartItemRequestBuilder;
import com.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestCartItemRequest(UUID publicId, long quantity) {

    public static DetailCartItemRequest to(RestCartItemRequest restCartItemRequest) {
        return DetailCartItemRequestBuilder.detailCartItemRequest()
                .publicId(new PublicId(restCartItemRequest.publicId()))
                .quantity(restCartItemRequest.quantity())
                .build();
    }

    public static RestCartItemRequest from(DetailCartItemRequest detailCartItemRequest) {
        return RestCartItemRequestBuilder.restCartItemRequest()
                .publicId(detailCartItemRequest.publicId().value())
                .quantity(detailCartItemRequest.quantity())
                .build();
    }

    public static List<DetailCartItemRequest> to(List<RestCartItemRequest> items) {
        return items.stream().map(RestCartItemRequest::to).toList();
    }

    public static List<RestCartItemRequest> from(List<DetailCartItemRequest> items) {
        return items.stream().map(RestCartItemRequest::from).toList();
    }
}
