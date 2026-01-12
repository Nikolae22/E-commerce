package com.ecom.order.domain.order.service;

import com.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.ecom.order.domain.order.aggregate.Order;
import com.ecom.order.domain.order.aggregate.OrderedProduct;
import com.ecom.order.domain.order.repository.OrderRepository;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.infrastructure.secondry.service.stripe.StripeService;
import com.ecom.product.domain.aggregate.Product;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderCreator {

    private final OrderRepository orderRepository;
    private final StripeService stripeService;

    public StripeSessionId create(List<Product> productsInformation,
                                  List<DetailCartItemRequest> items,
                                  User connectedUser){
        StripeSessionId stripeSessionId=stripeService.createPayment(
                connectedUser,productsInformation,items
        );
        List<OrderedProduct> orderedProducts=new ArrayList<>();
        for (DetailCartItemRequest itemRequest:items){
            Product productDetails = productsInformation.stream()
                    .filter(product -> product.getPublicId()
                            .value().equals(itemRequest.publicId().value()))
                    .findFirst().orElseThrow();

            OrderedProduct orderedProduct = OrderedProduct.create(itemRequest.quantity(), productDetails);
            orderedProducts.add(orderedProduct);
        }
        Order order = Order.create(connectedUser, orderedProducts, stripeSessionId);
        orderRepository.save(order);
    return stripeSessionId;
    }
}
