package com.yen.ShoppingCart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.yen.ShoppingCart.model.Cart;
import com.yen.ShoppingCart.model.Product;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.cart.AddToCartDto;
import com.yen.ShoppingCart.repository.CartRepository;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * Verifies the distributed lock contract for CartService.addToCart (Approach 4).
 *
 * Key invariants:
 *  - Lock key is scoped per user ("cart:user:<id>"), so different users never contend.
 *  - tryLock() is called before cartRepository.save().
 *  - unlock() is always called in finally (verified via isHeldByCurrentThread guard).
 *  - tryLock failure throws RuntimeException immediately (fail-fast, no thread exhaustion).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartServiceLockTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    RedissonClient redissonClient;

    @Mock
    RLock rLock;

    @InjectMocks
    CartService cartService;

    private User user;
    private Product product;
    private AddToCartDto dto;

    @BeforeEach
    void setUp() throws InterruptedException {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(42);

        product = new Product();
        dto = new AddToCartDto();
        dto.setQuantity(2);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
    }

    @Test
    void addToCart_shouldAcquireLockWithUserScopedKey() {
        cartService.addToCart(dto, product, user);

        verify(redissonClient).getLock("cart:user:42");
    }

    @Test
    void addToCart_shouldCallTryLockBeforeSave() throws InterruptedException {
        InOrder order = inOrder(rLock, cartRepository);

        cartService.addToCart(dto, product, user);

        order.verify(rLock).tryLock(anyLong(), anyLong(), eq(TimeUnit.SECONDS));
        order.verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCart_shouldUnlockAfterSave() {
        cartService.addToCart(dto, product, user);

        verify(rLock).unlock();
    }

    @Test
    void addToCart_shouldThrow_whenTryLockFails() throws InterruptedException {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        assertThrows(RuntimeException.class, () -> cartService.addToCart(dto, product, user));
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addToCart_differentUsers_shouldAcquireDifferentLocks() throws InterruptedException {
        User user2 = new User();
        user2.setId(99);

        RLock lock2 = mock(RLock.class);
        when(redissonClient.getLock("cart:user:42")).thenReturn(rLock);
        when(redissonClient.getLock("cart:user:99")).thenReturn(lock2);
        when(lock2.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(lock2.isHeldByCurrentThread()).thenReturn(true);

        cartService.addToCart(dto, product, user);
        cartService.addToCart(dto, product, user2);

        verify(redissonClient).getLock("cart:user:42");
        verify(redissonClient).getLock("cart:user:99");
        verify(rLock).unlock();
        verify(lock2).unlock();
    }

    @Test
    void addToCart_shouldNotUnlock_whenLockNotHeldByCurrentThread() throws InterruptedException {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        cartService.addToCart(dto, product, user);

        verify(rLock, never()).unlock();
    }
}
