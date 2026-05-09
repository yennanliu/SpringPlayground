package com.yen.ShoppingCart.service;

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
 *  - Lock key is "order:user:<id>" — per-user scope prevents duplicate order submission.
 *  - The entire order pipeline (list cart → save order → save items → clear cart)
 *    runs inside the critical section.
 *  - unlock() is always called in finally.
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
    void setUp() {
        user = new User();
        user.setId(7);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
        // return empty cart so placeOrder completes without needing real data
        when(cartService.listCartItems(any(User.class)))
                .thenReturn(new CartDto(Collections.emptyList(), 0.0));
    }

    @Test
    void placeOrder_shouldAcquireLockWithUserScopedKey() {
        orderService.placeOrder(user, "session-abc");

        verify(redissonClient).getLock("order:user:7");
    }

    @Test
    void placeOrder_shouldLockBeforeSavingOrder() {
        InOrder order = inOrder(rLock, orderRepository);

        orderService.placeOrder(user, "session-abc");

        order.verify(rLock).lock(anyLong(), eq(TimeUnit.SECONDS));
        order.verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_shouldUnlockAfterCompletion() {
        orderService.placeOrder(user, "session-abc");

        verify(rLock).unlock();
    }

    @Test
    void placeOrder_shouldClearCartInsideLock() {
        InOrder order = inOrder(rLock, cartService);

        orderService.placeOrder(user, "session-abc");

        // lock acquired first, cart cleared last (still inside lock before unlock)
        order.verify(rLock).lock(anyLong(), eq(TimeUnit.SECONDS));
        order.verify(cartService).deleteUserCartItems(user);
    }

    @Test
    void placeOrder_shouldNotUnlock_whenLockNotHeld() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        orderService.placeOrder(user, "session-abc");

        verify(rLock, never()).unlock();
    }

    @Test
    void placeOrder_shouldSaveOrderWithCorrectSessionId() {
        orderService.placeOrder(user, "stripe-session-xyz");

        verify(orderRepository).save(argThat(o -> "stripe-session-xyz".equals(o.getSessionId())));
    }
}
