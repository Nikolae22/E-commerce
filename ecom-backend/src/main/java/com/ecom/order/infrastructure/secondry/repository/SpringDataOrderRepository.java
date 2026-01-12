package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.domain.order.aggregate.Order;
import com.ecom.order.domain.order.aggregate.StripeSessionInformation;
import com.ecom.order.domain.order.repository.OrderRepository;
import com.ecom.order.domain.order.vo.OrderStatus;
import com.ecom.order.infrastructure.secondry.entity.OrderEntity;
import com.ecom.product.domain.vo.PublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataOrderRepository implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderedProductRepository jpaOrderedProductRepository;

    @Override
    public void save(Order order) {
        OrderEntity orderEntity = OrderEntity.from(order);
        OrderEntity orderSaved = jpaOrderRepository.save(orderEntity);
        orderSaved.getOrderedProducts()
                .forEach(orderedProductEntity ->
                        orderedProductEntity.getId()
                                .setOrder(orderSaved));
        jpaOrderedProductRepository.saveAll(orderEntity.getOrderedProducts());
    }

    @Override
    public void updateStatusByPublicId(OrderStatus orderStatus, PublicId ordrPublicId) {
        jpaOrderRepository.updateStatusByPublicId(orderStatus,ordrPublicId.value());
    }

    @Override
    public Optional<Order> findByStripeSessionId(StripeSessionInformation stripeSessionInformation) {
        return jpaOrderRepository.findByStripeSessionId(stripeSessionInformation.stripeSessionId().value())
                .map(OrderEntity::toDomain);
    }
}
