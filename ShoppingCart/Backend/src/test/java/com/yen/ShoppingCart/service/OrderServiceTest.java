package com.yen.ShoppingCart.service;

import static org.junit.jupiter.api.Assertions.*;

//import static spark.Spark.post;
//import static spark.Spark.port;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.yen.ShoppingCart.model.dto.checkout.CheckoutItemDto;
import com.yen.ShoppingCart.repository.OrderItemsRepository;
import com.yen.ShoppingCart.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemsRepository orderItemsRepository;

    @InjectMocks
    CartService cartService;

    @InjectMocks
    OrderService orderService;

    private CheckoutItemDto checkoutItemDto_1;

    private CheckoutItemDto checkoutItemDto_2;

    @BeforeEach
    public void setUp(){

        System.out.println("setup ...");

        checkoutItemDto_1 = new CheckoutItemDto("prod_1", 1, 100, 1, 1);
        checkoutItemDto_2 = new CheckoutItemDto();
    }

    @Test
    public void testCreatePriceData(){

        SessionCreateParams.LineItem.PriceData priceData  = orderService.createPriceData(checkoutItemDto_1);
        assertEquals(priceData.getUnitAmount(), 10000);

        SessionCreateParams.LineItem.PriceData priceData2 = orderService.createPriceData(new CheckoutItemDto());
        assertEquals(priceData2.getUnitAmount(), 0);
    }

    // TODO : revise this test
    @Test
    public void testCreateSessionLineItem(){

        SessionCreateParams.LineItem item1  = orderService.createSessionLineItem(checkoutItemDto_1);
        assertEquals(item1.getCurrency(), null);

        SessionCreateParams.LineItem item2  = orderService.createSessionLineItem(checkoutItemDto_2);
        assertEquals(item1.getCurrency(), null);
    }



//    // https://dashboard.stripe.com/test/webhooks/create?endpoint_location=local
//    @Test
//    public void testSpriteApi(){
//
//            // The library needs to be configured with your account's secret key.
//            // Ensure the key is kept out of any version control system you might be using.
//            Stripe.apiKey = "sk_test_...";
//
//            // This is your Stripe CLI webhook secret for testing your endpoint locally.
//            String endpointSecret = "whsec_56972f130fcc76bb0f3115bc01f3c28b9a3453caff18e0d0b71efd0729bb7615";
//
//            port(4242);
//
//            post("/webhook", (request, response) -> {
//                String payload = request.body();
//                String sigHeader = request.headers("Stripe-Signature");
//                Event event = null;
//
//                try {
//                    event = Webhook.constructEvent(
//                            payload, sigHeader, endpointSecret
//                    );
//                } catch (JsonSyntaxException e) {
//                    // Invalid payload
//                    response.status(400);
//                    return "";
//                } catch (SignatureVerificationException e) {
//                    // Invalid signature
//                    response.status(400);
//                    return "";
//                }
//
//                // Deserialize the nested object inside the event
//                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//                StripeObject stripeObject = null;
//                if (dataObjectDeserializer.getObject().isPresent()) {
//                    stripeObject = dataObjectDeserializer.getObject().get();
//                } else {
//                    // Deserialization failed, probably due to an API version mismatch.
//                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
//                    // instructions on how to handle this case, or return an error here.
//                }
//                // Handle the event
//                switch (event.getType()) {
//                    case "payment_intent.succeeded": {
//                        // Then define and call a function to handle the event payment_intent.succeeded
//                        break;
//                    }
//                    // ... handle other event types
//                    default:
//                        System.out.println("Unhandled event type: " + event.getType());
//                }
//
//                response.status(200);
//                return "";
//            });
//        }
//    }

}