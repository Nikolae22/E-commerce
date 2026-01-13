package com.ecom.order.application;

import com.ecom.order.domain.order.aggregate.*;
import com.ecom.order.domain.order.repository.OrderRepository;
import com.ecom.order.domain.order.service.CartReader;
import com.ecom.order.domain.order.service.OrderCreator;
import com.ecom.order.domain.order.service.OrderReader;
import com.ecom.order.domain.order.service.OrderUpdater;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.infrastructure.secondry.service.stripe.StripeService;
import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApplicationService {
    private final ProductApplicationService productApplicationService;
    private final CartReader cartReader;
    private final UsersApplicationService usersApplicationService;
    private final OrderCreator orderCreator;
    private final OrderUpdater orderUpdater;
    private final OrderReader orderReader;

    public OrderApplicationService(ProductApplicationService productApplicationService, UsersApplicationService usersApplicationService, OrderRepository orderRepository, StripeService stripeService) {
        this.productApplicationService = productApplicationService;
        this.usersApplicationService = usersApplicationService;
        this.orderUpdater = new OrderUpdater(orderRepository);
        this.orderCreator = new OrderCreator(orderRepository, stripeService);
        this.cartReader = new CartReader();
        this.orderReader=new OrderReader(orderRepository);
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
        return orderCreator.create(productInformation, items, autheniticatedUser);
    }

    @Transactional
    public void updateOrder(StripeSessionInformation stripeSessionInformation) {
        List<OrderedProduct> orderedProducts = this.orderUpdater.updateOrderFromStripe(stripeSessionInformation);
        List<OrderProductQuantity> orderProductQuantities = this.orderUpdater.computeQuantity(orderedProducts);
        this.productApplicationService.updateProductQuantity(orderProductQuantities);
        this.usersApplicationService.updateAddress(stripeSessionInformation.userAddress());
    }

    @Transactional(readOnly = true)
    public Page<Order> findOrdersForConnectedUser(Pageable pageable){
        User autheniticatedUser = usersApplicationService.getAutheniticatedUser();
        return orderReader.findAllByUserPublicId(autheniticatedUser.getUserPublicId(),pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findOrdersForAdmin(Pageable pageable){
        return orderReader.findAll(pageable);
    }
}
