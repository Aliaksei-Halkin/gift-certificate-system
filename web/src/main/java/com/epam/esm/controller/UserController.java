package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(@RequestBody Map<String, String> queryParameters) {
        List<User> allUsers = userService.findAll(queryParameters);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/{id}/order")
    public ResponseEntity<Order> makeOrder(@PathVariable("id") Long userId,
                                           @RequestBody List<Long> certificatesIDs) {
        Order order = orderService.makeOrder(userId, certificatesIDs);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> findUserOrders(@PathVariable("id") Long userId) {
        List<Order> orders = orderService.findUserOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/{id}/order/{orderId}")
    public ResponseEntity<OrderDto> findUserOrder(@PathVariable("id") Long userId,
                                                  @PathVariable("orderId") Long orderId){
        OrderDto order = userService.findUserOrder(userId, orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
