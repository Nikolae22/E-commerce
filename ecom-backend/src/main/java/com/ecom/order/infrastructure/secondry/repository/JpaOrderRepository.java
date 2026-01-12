package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.domain.order.vo.OrderStatus;
import com.ecom.order.infrastructure.secondry.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity,Long> {

    @Modifying
    @Query("update OrderEntity order set order.status = :orderStatus where order.publicId =:orderPublicId")
    void updateStatusByPublicId(OrderStatus orderStatus,
                                UUID orderPublicId);

    Optional<OrderEntity> findByStripeSessionId(String stripeSessionId);

}
