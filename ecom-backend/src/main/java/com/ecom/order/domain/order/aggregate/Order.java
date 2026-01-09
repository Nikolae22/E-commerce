package com.ecom.order.domain.order.aggregate;

import com.ecom.order.domain.order.vo.OrderStatus;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.aggregate.User;
import com.ecom.product.domain.vo.PublicId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class Order {

    private OrderStatus status;
    private User user;
    private String stripeId;
    private PublicId publicId;
    private List<OrderedProduct> orderedProducts;

    public static Order create(User connectedUser, List<OrderedProduct> orderedProducts,
                               StripeSessionId stripeSessionId) {
        return OrderBuilder.order()
                .publicId(new PublicId(UUID.randomUUID()))
                .user(connectedUser)
                .status(OrderStatus.PENDING)
                .orderedProducts(orderedProducts)
                .stripeId(stripeSessionId.value())
                .build();
    }


    public void validatePayment() {
        this.status = OrderStatus.PAID;
    }
}
