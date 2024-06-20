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
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticationService authenticationService;

    // stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        log.info("(checkoutList) checkoutItemDtoList = ");
        checkoutItemDtoList.forEach(x -> {System.out.println(x.toString());});
        // create the stripe session
        Session session = orderService.createSession(checkoutItemDtoList);
        log.info("Stripe session = " + session.toString());
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe session id in response
        return new ResponseEntity<StripeResponse>(stripeResponse, HttpStatus.OK);
    }

    // place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {

        log.info("(placeOrder) token = " + token);
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        log.info("(placeOrder) user = " + user);
        // place the order
        orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }

    // get all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {

        log.info("(getAllOrders) token = " + token);
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        // get orders
        List<Order> orderDtoList = orderService.listOrders(user);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // get order items for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
            throws AuthenticationFailException {

        log.info("(getOrderById) token = " + token + " id = " + id);
        // validate token
        authenticationService.authenticate(token);
        try {
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order,HttpStatus.OK);
        }
        catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

}
