package com.yen.ShoppingCart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.model.Order;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.cart.CartDto;
import com.yen.ShoppingCart.repository.OrderItemsRepository;
import com.yen.ShoppingCart.repository.OrderRepository;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * Verifies the distributed lock contract for OrderService.placeOrder (Approach 4).
 *
 * Key invariants:
 *  - Lock key is "order:user:<id>" — per-user scope prevents duplicate submissions.
 *  - tryLock() is used (fail-fast, no thread exhaustion).
 *  - tryLock failure throws RuntimeException without touching the DB.
 *  - Empty-cart guard: if cart is empty after lock, doPlaceOrder exits cleanly.
 *  - unlock() always called in finally.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceLockTest {

    @Mock
    CartService cartService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemsRepository orderItemsRepository;

    @Mock
    RedissonClient redissonClient;

    @Mock
    RLock rLock;

    @InjectMocks
    OrderService orderService;

    private User user;

    @BeforeEach
    void setUp() throws InterruptedException {
        user = new User();
        user.setId(7);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
        when(cartService.listCartItems(any(User.class)))
                .thenReturn(new CartDto(Collections.emptyList(), 0.0));
    }

    @Test
    void placeOrder_shouldAcquireLockWithUserScopedKey() {
        orderService.placeOrder(user, "session-abc");

        verify(redissonClient).getLock("order:user:7");
    }

    @Test
    void placeOrder_shouldCallTryLockBeforeCartRead() throws InterruptedException {
        InOrder order = inOrder(rLock, cartService);

        orderService.placeOrder(user, "session-abc");

        order.verify(rLock).tryLock(anyLong(), anyLong(), eq(TimeUnit.SECONDS));
        order.verify(cartService).listCartItems(user);
    }

    @Test
    void placeOrder_shouldUnlockAfterCompletion() {
        orderService.placeOrder(user, "session-abc");

        verify(rLock).unlock();
    }

    @Test
    void placeOrder_shouldThrow_whenTryLockFails() throws InterruptedException {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        assertThrows(RuntimeException.class, () -> orderService.placeOrder(user, "session-abc"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_shouldNotUnlock_whenLockNotHeld() throws InterruptedException {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        orderService.placeOrder(user, "session-abc");

        verify(rLock, never()).unlock();
    }

    @Test
    void placeOrder_emptyCart_shouldNotSaveOrder() {
        // Empty cart returned after lock acquisition — idempotency guard should skip persistence
        when(cartService.listCartItems(any())).thenReturn(new CartDto(Collections.emptyList(), 0.0));

        orderService.placeOrder(user, "session-abc");

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void placeOrder_shouldSaveOrderWithCorrectSessionId() {
        var item = new com.yen.ShoppingCart.model.dto.cart.CartItemDto();
        var product = new com.yen.ShoppingCart.model.Product();
        product.setPrice(10.0);
        item.setProduct(product);
        item.setQuantity(1);
        when(cartService.listCartItems(any()))
                .thenReturn(new CartDto(java.util.List.of(item), 10.0));

        orderService.placeOrder(user, "stripe-session-xyz");

        verify(orderRepository).save(argThat(o -> "stripe-session-xyz".equals(o.getSessionId())));
    }
}
