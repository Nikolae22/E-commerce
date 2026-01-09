package com.ecom.order.domain.order.aggregate;

import com.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

@Builder
public record DetailCartItemRequest(PublicId publicId,long quantity) {


}
