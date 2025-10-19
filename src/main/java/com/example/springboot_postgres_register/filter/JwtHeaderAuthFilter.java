package com.example.springboot_postgres_register.filter;
import com.example.springboot_postgres_register.util.JwtUtil;
import jakarta.servlet.*;
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

        // Allow login & registration without token
        if (path.equals("/v1/api/users/login") ||
                (path.equals("/v1/api/users") && req.getMethod().equals("POST"))) {
            chain.doFilter(request, response);
            return;
        }

        // Read token from custom header
        String token = req.getHeader("token");

        if (token == null || token.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing token header");
            return;
        }

        // Validate token
        if (!JwtUtil.validateToken(token)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired token");
            return;
        }

        // If valid, continue
        chain.doFilter(request, response);
    }
}
