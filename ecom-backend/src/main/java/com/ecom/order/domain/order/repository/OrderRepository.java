package com.ecom.order.domain.order.repository;

import com.ecom.order.domain.order.aggregate.Order;
import com.ecom.order.domain.order.aggregate.StripeSessionInformation;
import com.ecom.order.domain.order.vo.OrderStatus;
import com.ecom.order.domain.user.vo.UserPublicId;
import com.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    void updateStatusByPublicId(OrderStatus orderStatus,
                                PublicId ordrPublicId);

    Optional<Order> findByStripeSessionId(StripeSessionInformation stripeSessionInformation);

    Page<Order> findAllByUserPublicId(UserPublicId userPublicId,
                                      Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
