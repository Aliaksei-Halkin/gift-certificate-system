package com.epam.esm.controller;

import com.epam.esm.controller.assembler.OrderAssembler;
import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private OrderAssembler orderAssembler;

    @Autowired
    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Order>>> findAll(@RequestBody Map<String, String> pagingParameters) {
        List<Order> orders = orderService.findAll(pagingParameters);
        return new ResponseEntity<>(orderAssembler.toCollectionModel(orders), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Order>> findOrderById(@PathVariable("id") long orderId) {
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orderAssembler.toModel(order), HttpStatus.OK);
    }
}
