package com.ecom.order.application;

import com.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.ecom.order.domain.order.aggregate.DetailCartRequest;
import com.ecom.order.domain.order.aggregate.DetailCartResponse;
import com.ecom.order.domain.order.repository.OrderRepository;
import com.ecom.order.domain.order.service.CartReader;
import com.ecom.order.domain.order.service.OrderCreator;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.infrastructure.secondry.service.stripe.StripeService;
import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.PublicId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApplicationService {
    private final ProductApplicationService productApplicationService;
    private final CartReader cartReader;
    private final UsersApplicationService usersApplicationService;
    private final OrderCreator orderCreator;

    public OrderApplicationService(ProductApplicationService productApplicationService, UsersApplicationService usersApplicationService,OrderRepository orderRepository, StripeService stripeService) {
        this.productApplicationService = productApplicationService;
        this.usersApplicationService = usersApplicationService;
        this.orderCreator = new OrderCreator(orderRepository, stripeService);
        this.cartReader = new CartReader();
    }

    @Transactional(readOnly = true)
    public DetailCartResponse getCartDetails(DetailCartRequest detailCartRequest) {
        List<PublicId> publicIds = detailCartRequest.items().stream().map(DetailCartItemRequest::publicId).toList();
        List<Product> productInformation = productApplicationService.getProductsByPublicIdsIn(publicIds);
        return cartReader.getDetails(productInformation);
    }

    @Transactional
    public StripeSessionId createOrder(List<DetailCartItemRequest> items) {
        User autheniticatedUser = usersApplicationService.getAutheniticatedUser();
        List<PublicId> publicIdS = items.stream().map(DetailCartItemRequest::publicId).toList();
        List<Product> productInformation = productApplicationService.getProductsByPublicIdsIn(publicIdS);
        return orderCreator.create(productInformation,items,autheniticatedUser);
    }
}
