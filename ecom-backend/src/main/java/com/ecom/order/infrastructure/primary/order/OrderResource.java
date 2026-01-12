package com.ecom.order.infrastructure.primary.order;

import com.ecom.order.application.OrderApplicationService;
import com.ecom.order.domain.order.aggregate.*;
import com.ecom.order.domain.order.exception.CartPaymentException;
import com.ecom.order.domain.order.vo.StripeSessionId;
import com.ecom.order.domain.user.vo.*;
import com.ecom.product.domain.vo.PublicId;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Address;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderResource {

    private final OrderApplicationService orderApplicationService;

    @Value("${application.stripe.webhook-secret}")
    private String webhookSecret;

    @GetMapping("/get-cart-details")
    public ResponseEntity<RestDetailCartResponse> getDetails(
            @RequestParam List<UUID> productIds) {
        List<DetailCartItemRequest> cartItemRequests = productIds.stream()
                .map(uuid -> new DetailCartItemRequest(
                        new PublicId(uuid), 1
                )).toList();

        DetailCartRequest detailCartRequest = DetailCartRequestBuilder.detailCartRequest().items(cartItemRequests).build();
        DetailCartResponse cartDetails = orderApplicationService.getCartDetails(detailCartRequest);
        return ResponseEntity.ok(RestDetailCartResponse.from(cartDetails));
    }

    @PostMapping("/init-payment")
    public ResponseEntity<RestStripeSession> initPayment(@RequestBody List<RestCartItemRequest> items) {
        List<DetailCartItemRequest> detailCartItemRequests = RestCartItemRequest.to(items);
        try {
            StripeSessionId stripeSessionInformation = orderApplicationService.createOrder(detailCartItemRequests);
            RestStripeSession restStripeSession = RestStripeSession.from(stripeSessionInformation);
            return ResponseEntity.ok(restStripeSession);
        } catch (CartPaymentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhookStripe(@RequestBody String paymentEvent,
                                              @RequestHeader("Stripe-Signature") String stripeSignature) {
        Event event = null;
        try {
            event = Webhook.constructEvent(
                    paymentEvent, stripeSignature, webhookSecret
            );
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
        Optional<StripeObject> rawStripeObjectOpt = event.getDataObjectDeserializer().getObject();

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(rawStripeObjectOpt.orElseThrow());
                break;
        }
        return ResponseEntity.ok().build();

    }

    private void handleCheckoutSessionCompleted(StripeObject rawStripeObject) {
        if (rawStripeObject instanceof Session session) {
            Address address = session.getCustomerDetails().getAddress();

            UserAddress userAddress = UserAddressBuilder.userAddress()
                    .city(address.getCity())
                    .country(address.getCountry())
                    .zipCode(address.getPostalCode())
                    .street(address.getLine1())
                    .build();

            UserAddressToUpdate userAddressToUpdate = UserAddressToUpdateBuilder.userAddressToUpdate()
                    .userAddress(userAddress)
                    .userPublicId(new UserPublicId(UUID.fromString(session.getMetadata().get("user_public_id"))))
                    .build();

            StripeSessionInformation sessionInformation = StripeSessionInformationBuilder.stripeSessionInformation()
                    .userAddress(userAddressToUpdate)
                    .stripeSessionId(new StripeSessionId(session.getId()))
                    .build();

            orderApplicationService.updateOrder(sessionInformation);

        }
    }
}
