package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.infrastructure.secondry.entity.OrderedProductEntity;
import com.ecom.order.infrastructure.secondry.entity.OrderedProductEntityPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderedProductRepository extends JpaRepository<OrderedProductEntity , OrderedProductEntityPk> {
}
