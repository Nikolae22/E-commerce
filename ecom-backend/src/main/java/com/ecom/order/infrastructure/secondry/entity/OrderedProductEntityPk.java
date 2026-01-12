package com.ecom.order.infrastructure.secondry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jilt.Builder;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedProductEntityPk implements Serializable {

    @ManyToOne
    @JoinColumn(name = "fk_order",nullable = false)
    private OrderEntity order;

    @Column(name = "fk_product",nullable = false)
    private UUID productPublicId;
}
