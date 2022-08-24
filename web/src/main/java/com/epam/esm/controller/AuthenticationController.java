package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * The {@code AuthenticationController} class is an endpoint of the API
 * which allows to authenticate and register in gift certificate system.
 * <p>
 * {@code AuthenticationController} is accessed by sending request to base url /
 * and the response produced by {@code AuthenticationController} carries application/json
 * type of content.
 * <p>
 * {@code AuthenticationController} provides the user with methods to authenticate ({@link #authenticate}),
 * and registration ({@link #registration}).
 *
 */

@RestController
 @RequestMapping("/")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticate the user passed in the request body.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at /login and that the
     * information about login and password must be carried in request body in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param request Dto class which contains user login and password.
     * @return {@link ResponseEntity} with the user login and generated token.
     */
    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<AuthenticateResponseDto> authenticate(@RequestBody @Valid AuthenticateRequestDto request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        UserCredentialInformation user = (UserCredentialInformation) authenticate.getPrincipal();
        String token = jwtTokenProvider.createToken(request.getLogin());
        return new ResponseEntity<>(new AuthenticateResponseDto(user.getUserId(), request.getLogin(), token), HttpStatus.OK);
    }

    /**
     * Registration the user passed in the request body.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at /registration and that the
     * information about user, such as login, first name, last name, password and repeated password
     * must be carried in request body in JSON format.
     * <p>
     * The default response status is 200 - OK.
     *
     * @param userDto Dto class which contains user personal data.
     * @return {@link ResponseEntity} with the user login and generated token.
     */
    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<AuthenticateResponseDto> registration(@RequestBody @Valid RegistrationUserDto userDto) {
        UserDto user = userService.add(userDto);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), userDto.getPassword()));
        String token = jwtTokenProvider.createToken(user.getLogin());
        return new ResponseEntity<>(new AuthenticateResponseDto(user.getUserId(), user.getLogin(), token), HttpStatus.OK);
    }
}