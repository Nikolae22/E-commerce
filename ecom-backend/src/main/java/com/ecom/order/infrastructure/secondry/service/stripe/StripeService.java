package com.ecom.order.infrastructure.secondry.service.stripe;

import com.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.ecom.order.domain.order.exception.CartPaymentException;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.aggregate.User;
import com.ecom.product.domain.aggregate.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@NoArgsConstructor
public class StripeService {

    @Value("${application.stripe.api-key}")
    private String apiKey;

    @Value("${application.client-base-url}")
    private String clientBaseUrl;

    @PostConstruct
    public void setApiKey(){
        Stripe.apiKey=apiKey;
    }

    public StripeSessionId createPayment(User connectedUser,
                                         List<Product> productsInformation,
                                         List<DetailCartItemRequest> items){
        SessionCreateParams.Builder sessionCreation = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("user_public_id", connectedUser.getUserPublicId().value().toString())
                .setCustomerEmail(connectedUser.getEmail().value())
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setSuccessUrl(this.clientBaseUrl + "/cart/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(this.clientBaseUrl + "/cart/failure");

        for (DetailCartItemRequest itemRequest: items){
            Product productDetails = productsInformation.stream()
                    .filter(product -> product.getPublicId().value()
                            .equals(itemRequest.publicId().value()))
                    .findFirst().orElseThrow();

            SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .putMetadata("product_id", productDetails.getPublicId().value().toString())
                    .setName(productDetails.getProductName().value())
                    .build();
            SessionCreateParams.LineItem.PriceData priceProduct = SessionCreateParams.LineItem.PriceData.builder()
                    .setUnitAmountDecimal(BigDecimal.valueOf(Double.valueOf(productDetails.getProductPrice().value()).longValue() * 100))
                    .setProductData(productData)
                    .setCurrency("EUR")
                    .build();
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(itemRequest.quantity())
                    .setPriceData(priceProduct)
                    .build();

            sessionCreation.addLineItem(lineItem);
        }

        return createSession(sessionCreation.build());
    }

    private StripeSessionId createSession(SessionCreateParams sessionInformation) {
        try {
            Session session = Session.create(sessionInformation);
            return new StripeSessionId(session.getId());
        }catch (StripeException e){
            throw new CartPaymentException("Error while creating Session");
        }
    }
}
