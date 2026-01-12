package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.infrastructure.secondry.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<OrderEntity,Long> {
}
