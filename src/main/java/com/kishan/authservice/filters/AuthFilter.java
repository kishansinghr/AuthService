package com.kishan.authservice.filters;

import com.kishan.authservice.models.SessionStatus;
import com.kishan.authservice.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Order(1)
public class AuthFilter implements Filter {

    @Autowired
    private SessionService sessionService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Set<String> publicUrls = new HashSet<>();
        publicUrls.add("/auth/register");
        publicUrls.add("/auth/login");

        String pathInfo = request.getRequestURI();
        if (!publicUrls.contains(pathInfo)) {
            String token = request.getHeader("token");
            SessionStatus sessionStatus = null;
            if (token != null)
                sessionStatus = sessionService.verifyToken(token);

            if (sessionStatus != SessionStatus.ACTIVE) {
                response.setStatus(UNAUTHORIZED.value());
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
