package com.yen.ShoppingCart.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.yen.ShoppingCart.exception.OrderNotFoundException;
import com.yen.ShoppingCart.model.Order;
import com.yen.ShoppingCart.model.OrderItem;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.model.dto.cart.CartItemDto;
import com.yen.ShoppingCart.model.dto.checkout.CheckoutItemDto;
import com.yen.ShoppingCart.repository.OrderItemsRepository;
import com.yen.ShoppingCart.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    private String DEFAULT_CURRENCY = "usd"; // TODO : move to enums

    // get total price : package com.stripe.param.checkout;
    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(DEFAULT_CURRENCY)
                        .setUnitAmount((long)(checkoutItemDto.getPrice() * 100)) // TODO : double check ?
                        .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData
                                .builder()
                                .setName(checkoutItemDto.getProductName())
                                .build()
                        )
                        .build();

        log.info("currency = " + priceData.getCurrency() + ", product = " + priceData.getProduct());
        return priceData;
    }

    // build each product in the stripe checkout page
    // package com.stripe.param.checkout;
    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {

        return SessionCreateParams.LineItem.builder()
                // set price for each product
                .setPriceData(createPriceData(checkoutItemDto))
                // set quantity for each product
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    // create session from list of checkout items
    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        // supply success and failure url for stripe
        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";
        log.info("successURL = " + successURL + ", failedURL = " + failedURL);

        // set the private key
        Stripe.apiKey = apiKey;
        log.info("apiKey = " + apiKey + " Stripe.apiKey = " + Stripe.apiKey);

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
            sessionItemsList.add(createSessionLineItem(checkoutItemDto));
        }
        //log.info("sessionItemsList = " + sessionItemsList);
        //sessionItemsList.forEach(x -> {System.out.println(x.toString());});
        // build the session param
        log.info("build Stripe session start");
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();
        log.info("build Stripe session end");
        return Session.create(params);
    }

    public void placeOrder(User user, String sessionId) {

        // first get user's cart items
        CartDto cartDto = cartService.listCartItems(user);
        List<CartItemDto> cartItemDtoList = cartDto.getcartItems();

        // TODO : need to wrap with try-catch ?
        // create order and save
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);
            // add to order item list
            orderItemsRepository.save(orderItem);
        }

        // delete items in cart, since they are already transformed to orders
        cartService.deleteUserCartItems(user);
    }

    public List<Order> listOrders(User user) {

        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Order getOrder(Integer orderId) throws OrderNotFoundException {

        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException("Order not found");
    }

}
