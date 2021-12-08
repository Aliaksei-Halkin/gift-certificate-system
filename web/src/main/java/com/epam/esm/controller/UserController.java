package com.epam.esm.controller;

import com.epam.esm.controller.assembler.OrderAssembler;
import com.epam.esm.controller.assembler.TagAssembler;
import com.epam.esm.controller.assembler.UserAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The {@code UserController} class is an endpoint of the API
 * which allows its users to perform read and create operations on user.
 * <p>
 * {@code UserController} is accessed by sending request to /users
 * and the response produced by {@code UserController} carries application/json type of content .
 * <p>
 * {@code UserController} provides the user with methods to find user by id ({@link #findById(long)}),
 * find all users   ({@link #findAll }), find user's orders ({@link #findOrdersByUserId(Long)}),
 * find user's order ({@link #findOrder}), make order ({@link #makeOrder}) and find most widely used
 * tag ({@link #findMostWidelyUsedTag()}).
 **/
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;
    private final UserAssembler userAssembler;
    private final TagAssembler tagAssembler;
    private final OrderAssembler orderAssembler;

    /**
     * Inject objects of classes implementing {@link UserService} and {@link OrderService}.
     * User assembler {@link UserAssembler}, tag assembler {@link TagAssembler} and
     * order assembler {@link OrderAssembler}.
     *
     * @param userService    An object of a class implementing {@link TagService}.
     * @param orderService   An object of a class implementing {@link OrderService}.
     * @param userAssembler  {@link UserAssembler} using for create HATEOAS links.
     * @param tagAssembler   {@link TagAssembler} using for create HATEOAS links.
     * @param orderAssembler {@link OrderAssembler} using for create HATEOAS links.
     */
    @Autowired
    public UserController(UserService userService, OrderService orderService, UserAssembler userAssembler, TagAssembler tagAssembler, OrderAssembler orderAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userAssembler = userAssembler;
        this.tagAssembler = tagAssembler;
        this.orderAssembler = orderAssembler;
    }

    /**
     * Find users in the storage by various parameter passed as a parameter in the request URI.
     * If there is no parameters method returns all users in the storage.
     * <p>
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at /certificates.
     * <p>
     * Accepts optional request parameters {@code first_name}, {@code last_name}, {@code login},
     * {@code order}, {@code page}, {@code per_page}. All parameters can be used in conjunction.
     * <p>
     * The {@code order} might contain one the following values:
     * {@code first_name} or {@code -first_name} and {@code last_name} or {@code -last_name} and {@code login}
     * or {@code -login}.
     * Minus sign indicates descending order. Default order is ascending without any signs.
     * <p>
     * The {@code page} contains number of the page. The {@code per_page} show how many elements will be displayed on the page.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param queryParameters The parameters used to find users.
     * @return {@link ResponseEntity} with the list of the users.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> findAll(@RequestBody Map<String, String> queryParameters) {
        List<User> allUsers = userService.findAll(queryParameters);
        return new ResponseEntity<>(userAssembler.toCollectionModel(allUsers), HttpStatus.OK);
    }

    /**
     * Returns the user with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * /users/{id}, where id is the identifier of the requested tag represented by a natural number.
     * <p>
     * If there is no user with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param id Identifier of the requested user. Inferred from the request URI.
     * @return {@link ResponseEntity} with found user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findById(@PathVariable long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(userAssembler.toModel(user), HttpStatus.OK);
    }

    /**
     * Inserts the gift certificate's ids passed in the request body into the storage.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at /users/{id}/orders where id is the identifier of the
     * user where gift certificate's ids to be inserted. Information about the new order must be carried in request body
     * in JSON format.
     * <p>
     * The default response status is 201 - CREATED.
     *
     * @param userId          Identifier of the requested user. Inferred from the request URI.
     * @param certificatesIDs The list of gift certificate's ids which to be inserted into storage. Inferred from the request body.
     * @return {@link ResponseEntity} with the made order and its location included.
     */
    @PostMapping("/{id}/order")
    public ResponseEntity<EntityModel<Order>> makeOrder(@PathVariable("id") Long userId,
                                                        @RequestBody List<Long> certificatesIDs) {
        Order order = orderService.makeOrder(userId, certificatesIDs);
        return new ResponseEntity<>(orderAssembler.toModel(order), HttpStatus.CREATED);
    }

    /**
     * Returns the user's orders with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}/orders". Therefore, processes GET requests at
     * /users/{id}/orders, where id is the identifier of the user which have orders with orderId.
     * <p>
     * If there is no user or order with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param userId Identifier of the requested user. Inferred from the request URI.
     * @return {@link ResponseEntity} with found orders.
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> findOrdersByUserId(@PathVariable("id") Long userId) {
        List<Order> orders = orderService.findUserOrders(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Returns the user's order with the specified identifier from the storage.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/{id}/order/{orderId}". Therefore, processes GET requests at
     * /users/{id}/order/{orderId}, where id is the identifier of the user which have order with orderId.
     * <p>
     * If there is no user or order with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param userId  Identifier of the requested user. Inferred from the request URI.
     * @param orderId Identifier of the requested order. Inferred from the request URI.
     * @return {@link ResponseEntity} with found order.
     */
    @GetMapping("/{id}/order/{orderId}")
    public ResponseEntity<EntityModel<Order>> findOrder(@PathVariable("id") Long userId,
                                                        @PathVariable("orderId") Long orderId) {
        OrderDto orderDto = userService.findUserOrder(userId, orderId);
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setCreateDate(orderDto.getPurchaseDate());
        order.setTotalCost(orderDto.getTotalCost());
        return new ResponseEntity<>(orderAssembler.toModel(order), HttpStatus.OK);
    }

    /**
     * Returns the most widely used tag of a user with the highest cost of all orders.
     * <p>
     * Annotated by {@link GetMapping} with parameter value = "/tag".
     * <p>
     * If there is no tag response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @return {@link ResponseEntity} with the most widely used tags of a user with the highest cost of all orders.
     */
    @GetMapping("/tag")
    public ResponseEntity<CollectionModel<EntityModel<Tag>>> findMostWidelyUsedTag() {
        List<Tag> tags = orderService.findMostWidelyUsedTag();
        return new ResponseEntity<>(tagAssembler.toCollectionModel(tags), HttpStatus.OK);
    }
}
