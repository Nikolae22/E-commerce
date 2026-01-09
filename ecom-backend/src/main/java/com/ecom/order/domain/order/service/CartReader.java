package com.ecom.order.domain.order.service;

import com.ecom.order.domain.order.aggregate.DetailCartResponse;
import com.ecom.order.domain.order.aggregate.DetailCartResponseBuilder;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.aggregate.ProductCart;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class CartReader {

    public DetailCartResponse getDetails(List<Product> products){
        List<ProductCart> cartProducts = products.stream().map(ProductCart::from)
                .toList();
        return DetailCartResponseBuilder
                .detailCartResponse()
                .products(cartProducts)
                .build();
    }
}
