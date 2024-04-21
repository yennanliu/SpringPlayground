package com.yen.ShoppingCart.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import com.yen.ShoppingCart.common.ApiResponse;
import com.yen.ShoppingCart.exception.AuthenticationFailException;
import com.yen.ShoppingCart.exception.OrderNotFoundException;
import com.yen.ShoppingCart.model.Order;
import com.yen.ShoppingCart.model.User;
import com.yen.ShoppingCart.model.dto.checkout.CheckoutItemDto;
import com.yen.ShoppingCart.model.dto.checkout.StripeResponse;
import com.yen.ShoppingCart.service.AuthenticationService;
import com.yen.ShoppingCart.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckoutList() throws StripeException {
        // Given
        List<CheckoutItemDto> checkoutItemDtoList = new ArrayList<>();

        Session session = new Session();
        session.setId("session_id");

        when(orderService.createSession(checkoutItemDtoList)).thenReturn(session);

        // When
        ResponseEntity<StripeResponse> response = orderController.checkoutList(checkoutItemDtoList);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session.getId(), response.getBody().getSessionId());
    }

    @Test
    public void testPlaceOrder() throws AuthenticationFailException {
        // Given
        String token = "token";
        String sessionId = "session_id";

        when(authenticationService.getUser(token)).thenReturn(new User());
        doNothing().when(orderService).placeOrder(any(User.class), eq(sessionId));

        // When
        ResponseEntity<ApiResponse> response = orderController.placeOrder(token, sessionId);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(orderService, times(1)).placeOrder(any(User.class), eq(sessionId));
    }

    @Test
    public void testGetAllOrders() throws AuthenticationFailException {
        // Given
        String token = "token";
        List<Order> orders = new ArrayList<>();

        when(authenticationService.getUser(token)).thenReturn(new User());
        when(orderService.listOrders(any(User.class))).thenReturn(orders);

        // When
        ResponseEntity<List<Order>> response = orderController.getAllOrders(token);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    public void testGetOrderById() throws AuthenticationFailException, OrderNotFoundException {
        // Given
        String token = "token";
        int orderId = 1;

        Order order = new Order();

        when(authenticationService.getUser(token)).thenReturn(new User());
        when(orderService.getOrder(orderId)).thenReturn(order);

        // When
        ResponseEntity<Object> response = orderController.getOrderById(orderId, token);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    public void testGetOrderByIdOrderNotFoundException() throws AuthenticationFailException, OrderNotFoundException {
        // Given
        String token = "token";
        int orderId = 1;
        String errorMessage = "Order not found";

        when(authenticationService.getUser(token)).thenReturn(new User());
        when(orderService.getOrder(orderId)).thenThrow(new OrderNotFoundException(errorMessage));

        // When
        ResponseEntity<Object> response = orderController.getOrderById(orderId, token);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

}
