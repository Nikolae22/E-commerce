package com.ecom.order.infrastructure.secondry.entity;


import com.ecom.order.domain.order.aggregate.OrderedProduct;
import com.ecom.order.domain.order.aggregate.OrderedProductBuilder;
import com.ecom.order.domain.order.vo.OrderPrice;
import com.ecom.order.domain.order.vo.OrderQuantity;
import com.ecom.order.domain.order.vo.ProductPublicId;
import com.ecom.product.domain.vo.ProductName;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jilt.Builder;

import java.util.List;

@Entity
@Table(name = "ordered_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderedProductEntity {

    @EmbeddedId
    private OrderedProductEntityPk id;

    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private String productId;
    @Column(nullable = false)
    private String productName;

    public static OrderedProductEntity from(OrderedProduct orderedProduct){
        OrderedProductEntityPk entityPk = OrderedProductEntityPkBuilder.orderedProductEntityPk()
                .productPublicId(orderedProduct.getProductPublicId().value())
                .build();
        return OrderedProductEntityBuilder.orderedProductEntity()
                .price(orderedProduct.getPrice().value())
                .quantity(orderedProduct.getQuantity().value())
                .productId(orderedProduct.getProductName().value())
                .id(entityPk)
                .build();
    }

    public static List<OrderedProductEntity> from(List<OrderedProduct> orderedProducts){
        return orderedProducts.stream().map(
                OrderedProductEntity::from
        ).toList();
    }

    public static OrderedProduct toDomain(OrderedProductEntity orderedProductEntity){
        return OrderedProductBuilder
                .orderedProduct()
                .productPublicId(new ProductPublicId(orderedProductEntity.getId().getProductPublicId()))
                .quantity(new OrderQuantity(orderedProductEntity.getQuantity()))
                .price(new OrderPrice(orderedProductEntity.getPrice()))
                .productName(new ProductName(orderedProductEntity.getProductName()))
                .build();
    }
}
