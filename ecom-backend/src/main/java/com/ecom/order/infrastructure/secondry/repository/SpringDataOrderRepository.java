package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.domain.order.aggregate.Order;
import com.ecom.order.domain.order.repository.OrderRepository;
import com.ecom.order.infrastructure.secondry.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
