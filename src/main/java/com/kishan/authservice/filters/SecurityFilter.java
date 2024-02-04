package com.kishan.authservice.filters;

import com.kishan.authservice.beans.SessionDataBean;
import com.kishan.authservice.constants.UserRole;
import com.kishan.authservice.models.SessionStatus;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Order(2)
public class SecurityFilter implements Filter {

    @Autowired
    private SessionDataBean sessionDataBean;
    private final Set<String> publicUrls = new HashSet<>();
    private final Map<String, Set<String>> urlAccessRoles = new HashMap<>();

    public void init(FilterConfig filterConfig) {
        publicUrls.add("auth");

        urlAccessRoles.put("users", Set.of(UserRole.ADMIN.getName()));
        urlAccessRoles.put("roles", Set.of(UserRole.ADMIN.getName()));
        urlAccessRoles.put("username", Set.of(UserRole.ADMIN.getName(),
                UserRole.CUSTOMER.getName(),
                UserRole.SELLER.getName()));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        String pathInfo = requestURI.split("/")[1];

        if (!publicUrls.contains(pathInfo)) {
            if (sessionDataBean == null || sessionDataBean.getStatus() != SessionStatus.ACTIVE) {
                response.setStatus(UNAUTHORIZED.value());
                response.getWriter().write("Please login to access this resource.");
                return;
            }

            if (!this.isAuthorized(pathInfo)) {
                response.setStatus(UNAUTHORIZED.value());
                response.getWriter().write("You are not authorized to access this resource.");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isAuthorized(String path) {
        Set<String> accessRoles = urlAccessRoles.get(path);
        List<String> userRoles = sessionDataBean.getRoles();

        for (String userRole : userRoles) {
            if (accessRoles.contains(userRole)) {
                return true;
            }
        }
        return false;
    }
}
