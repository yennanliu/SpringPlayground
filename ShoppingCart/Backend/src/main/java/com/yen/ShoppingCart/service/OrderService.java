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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    private String DEFAULT_CURRENCY = "usd";

    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(DEFAULT_CURRENCY)
                        .setUnitAmount((long)(checkoutItemDto.getPrice() * 100))
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

    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {

        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";
        log.info("successURL = " + successURL + ", failedURL = " + failedURL);

        Stripe.apiKey = apiKey;
        log.info("apiKey = " + apiKey + " Stripe.apiKey = " + Stripe.apiKey);

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();
        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
            sessionItemsList.add(createSessionLineItem(checkoutItemDto));
        }

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

    /**
     * Places an order for the user atomically and idempotently.
     *
     * Lock ordering: lock is acquired BEFORE the transaction opens so that the lock
     * is held across the full transaction commit — preventing another request from
     * reading a half-committed state.
     *
     * tryLock(waitTime=3s, leaseTime=30s): fails fast if a concurrent request is
     * already placing an order for this user, avoiding thread-pool exhaustion.
     *
     * Empty-cart guard: re-checks the cart after lock acquisition. If a concurrent
     * request already processed the cart, this request exits cleanly instead of
     * persisting an empty order.
     */
    public void placeOrder(User user, String sessionId) {

        RLock lock = redissonClient.getLock("order:user:" + user.getId());
        try {
            if (!lock.tryLock(3, 30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Could not acquire order lock for user " + user.getId()
                        + " — a concurrent checkout is already in progress");
            }
            transactionTemplate.executeWithoutResult(status -> doPlaceOrder(user, sessionId));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while acquiring order lock for user " + user.getId(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * The actual transactional work — called inside a TransactionTemplate in placeOrder so
     * the transaction is committed before the lock is released in the finally block.
     * TransactionTemplate is used instead of @Transactional to avoid the Spring proxy
     * self-invocation limitation (internal calls bypass the AOP proxy).
     */
    private void doPlaceOrder(User user, String sessionId) {

        CartDto cartDto = cartService.listCartItems(user);
        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // Idempotency guard: if a concurrent request already placed the order and
        // cleared the cart, skip to avoid persisting an empty order.
        if (cartItemDtoList.isEmpty()) {
            log.warn("placeOrder called with empty cart for user {} — skipping (possible duplicate request)",
                    user.getId());
            return;
        }

        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);
            orderItemsRepository.save(orderItem);
        }

        cartService.deleteUserCartItems(user);
    }

    @Transactional(readOnly = true)
    public List<Order> listOrders(User user) {
        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Integer orderId) throws OrderNotFoundException {

        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException("Order not found");
    }
}
