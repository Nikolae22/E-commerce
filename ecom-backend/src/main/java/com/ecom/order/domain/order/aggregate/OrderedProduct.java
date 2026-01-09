package com.ecom.order.domain.order.aggregate;

import com.ecom.order.domain.order.vo.OrderPrice;
import com.ecom.order.domain.order.vo.OrderQuantity;
import com.ecom.order.domain.order.vo.ProductPublicId;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.ProductName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jilt.Builder;

@Builder
@AllArgsConstructor
@Getter
public class OrderedProduct {

    private final ProductPublicId productPublicId;

    private final OrderPrice price;
    private final OrderQuantity quantity;
    private final ProductName productName;

    public static OrderedProduct create(long quantity, Product product){
        return OrderedProductBuilder.orderedProduct()
                .price(new OrderPrice(product.getProductPrice().value()))
                .quantity(new OrderQuantity(quantity))
                .productName(product.getProductName())
                .productPublicId(new ProductPublicId(product.getPublicId().value()))
                .build();
    }
}
