package com.ecom.order.domain.order.aggregate;

import com.ecom.product.domain.aggregate.ProductCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jilt.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class DetailCartResponse {

    List<ProductCart> products;
}
