package com.epam.esm.controller;

import com.epam.esm.controller.assembler.GiftCertificateAssembler;
import com.epam.esm.controller.assembler.OrderAssembler;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The {@code OrderController} class is an endpoint of the API
 * which allows its users to perform read operations on orders.
 * <p>
 * {@code OrderController} is accessed by sending request to /orders
 * and the response produced by {@code OrderController} carries application/json type of content.
 * <p>
 * {@code OrderController} provides the user with methods to find order's gift certificates by id
 * ({@link #findOrderById(long)}), find order by id ({@link #findOrderById}).
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private OrderAssembler orderAssembler;

    /**
     * Injects an object of a class implementing {@link GiftCertificateService}, gift certificate assembler
     * {@link GiftCertificateAssembler} and order assembler {@link OrderAssembler}.
     *
     * @param orderService   An object of a class implementing {@link OrderService}.
     * @param orderAssembler {@link OrderAssembler} using for create HATEOAS links.
     */
    @Autowired
    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }
    /**
     * Returns   all orders with the specified identifier from the storage.
     * <p>
     * Annotated by {@link RequestBody} with pagingParameters.
     * <p>
        * The default response status is 200 - OK.
     *
     * @param pagingParameters with keys "page", "per_page" describe number of page and quantity values on page
     * @return {@link ResponseEntity} with found order.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<OrderEntity>>> findAll(@RequestBody Map<String, String> pagingParameters) {
        List<OrderEntity> orders = orderService.findAll(pagingParameters);
        return new ResponseEntity<>(orderAssembler.toCollectionModel(orders), HttpStatus.OK);
    }
    /**
     * Returns the order with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * /orders/{id}, where id is the identifier of the requested order represented by a natural number.
     * <p>
     * If there is no order with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param orderId Identifier of the requested order. Inferred from the request URI.
     * @return {@link ResponseEntity} with found order.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrderEntity>> findOrderById(@PathVariable("id") long orderId) {
        OrderEntity order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orderAssembler.toModel(order), HttpStatus.OK);
    }
}
