package com.ecom.product.domain.service;

import com.ecom.order.domain.order.aggregate.OrderProductQuantity;
import com.ecom.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductUpdater {

    private final ProductRepository productRepository;

    public void updateProductQuantity(List<OrderProductQuantity> productQuantities){
        for (OrderProductQuantity orderProductQuantity : productQuantities){
            productRepository.updateQuantity(orderProductQuantity.productPublicId(),
                    orderProductQuantity.quantity().value());
        }
    }

}
