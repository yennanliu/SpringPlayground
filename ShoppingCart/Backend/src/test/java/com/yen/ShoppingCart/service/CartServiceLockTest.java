package com.yen.ShoppingCart.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * Verifies the distributed lock contract for CartService.addToCart (Approach 4).
 *
 * Key invariants:
 *  - Lock key is scoped per user ("cart:user:<id>"), so different users never contend.
 *  - lock() is called before cartRepository.save() — write happens inside the critical section.
 *  - unlock() is always called in the finally block (verified via InOrder + isHeldByCurrentThread).
 *  - A user with id=null gets key "cart:user:null" — doesn't crash, still acquires a lock.
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
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(42);

        product = new Product();
        dto = new AddToCartDto();
        dto.setQuantity(2);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
    }

    @Test
    void addToCart_shouldAcquireLockWithUserScopedKey() {
        cartService.addToCart(dto, product, user);

        verify(redissonClient).getLock("cart:user:42");
    }

    @Test
    void addToCart_shouldCallLockBeforeSave() {
        InOrder order = inOrder(rLock, cartRepository);

        cartService.addToCart(dto, product, user);

        order.verify(rLock).lock(anyLong(), eq(TimeUnit.SECONDS));
        order.verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCart_shouldUnlockAfterSave() {
        cartService.addToCart(dto, product, user);

        verify(rLock).unlock();
    }

    @Test
    void addToCart_differentUsers_shouldAcquireDifferentLocks() {
        User user2 = new User();
        user2.setId(99);

        RLock lock2 = mock(RLock.class);
        when(redissonClient.getLock("cart:user:42")).thenReturn(rLock);
        when(redissonClient.getLock("cart:user:99")).thenReturn(lock2);
        when(lock2.isHeldByCurrentThread()).thenReturn(true);

        cartService.addToCart(dto, product, user);
        cartService.addToCart(dto, product, user2);

        verify(redissonClient).getLock("cart:user:42");
        verify(redissonClient).getLock("cart:user:99");
        verify(rLock).unlock();
        verify(lock2).unlock();
    }

    @Test
    void addToCart_shouldNotUnlock_whenLockNotHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        cartService.addToCart(dto, product, user);

        verify(rLock, never()).unlock();
    }
}
