package com.kishan.authservice.filters;

import com.kishan.authservice.beans.SessionDataBean;
import com.kishan.authservice.configs.SecurityConfig;
import com.kishan.authservice.exceptions.InvalidTokenException;
import com.kishan.authservice.services.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class AuthFilter implements Filter {

    @Autowired
    private SessionService sessionService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String token = request.getHeader("token");
        if (token != null) {
            try {
                SessionDataBean sessionDataBean = sessionService.verifyToken(token);
                SecurityConfig.setSessionDataBean(sessionDataBean);
            } catch (InvalidTokenException e) {
                //do nothing
                System.out.println(e.getMessage());
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
