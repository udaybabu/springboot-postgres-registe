package com.example.springboot_postgres_register.filter;

import com.example.springboot_postgres_register.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtHeaderAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String method = req.getMethod();

        // ✅ Skip login and register endpoints
        if (path.equals("/v1/api/users/login") ||
                (path.equals("/v1/api/users") && method.equals("POST"))) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ Extract token from cookie
        String token = null;
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || token.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing token cookie");
            return;
        }

        // ✅ Validate token using JwtUtil
//        String email = JwtUtil.validateToken(token);
//        if (email == null) {
//            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            res.getWriter().write("Invalid or expired token");
//            return;
//        }

        String email = JwtUtil.getEmailFromToken(token);
        if (email == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired token");
            return;
        }

        // ✅ Attach user email (subject) to request for controller use
        req.setAttribute("email", email);

        // ✅ Continue to next filter / controller
        chain.doFilter(request, response);
    }
}
