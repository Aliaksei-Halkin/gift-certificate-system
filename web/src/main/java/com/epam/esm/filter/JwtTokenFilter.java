package com.epam.esm.filter;

import com.epam.esm.handler.ErrorCode;
import com.epam.esm.handler.ErrorHandler;
import com.epam.esm.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private static final String ACCESS_DENIED = "access.denied";
    private static final String ENCODING = "UTF-8";
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, MessageSource messageSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.messageSource = messageSource;
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(ENCODING);
            response.getWriter().write(String.valueOf(new ObjectMapper()
                    .writeValueAsString(new ErrorHandler(messageSource.getMessage(ACCESS_DENIED, null, request.getLocale()),
                            ErrorCode.ACCESS_DENIED))));
            return;
        }
        chain.doFilter(request, response);
    }
}