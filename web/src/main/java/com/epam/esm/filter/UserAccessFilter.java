package com.epam.esm.filter;

import com.epam.esm.handler.ErrorCode;
import com.epam.esm.handler.ErrorHandler;
import com.epam.esm.security.AuthenticationDetails;
import com.epam.esm.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAccessFilter extends GenericFilterBean {
    private static final String REGEX_URL = "(/users/[1-9]\\d*)(/orders(/[1-9]\\d*)?)?";
    private static final String REGEX_DELIMITER = "/";
    private static final String ACCESS_DENIED = "access.denied";
    private static final String ENCODING = "UTF-8";
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    public UserAccessFilter(JwtTokenProvider jwtTokenProvider, MessageSource messageSource) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.messageSource = messageSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null && uri.matches(REGEX_URL)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();
            if (Long.parseLong(uri.split(REGEX_DELIMITER)[2]) != details.getId() && !details.isAdmin()) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(ENCODING);
                response.getWriter().write(String.valueOf(new ObjectMapper()
                        .writeValueAsString(new ErrorHandler(messageSource.getMessage(ACCESS_DENIED, null, request.getLocale()),
                                ErrorCode.ACCESS_DENIED))));
                return;
            }
        }
        chain.doFilter(request, response);
    }
}