package com.example.springboot_postgres_register.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import  com.example.springboot_postgres_register.model.User;
import  com.example.springboot_postgres_register.service.UserService;
import java.util.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.example.springboot_postgres_register.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    // LOGIN (sets  in cookie)
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData,
                                         HttpServletResponse response) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        Map<String, Object> result = userService.loginUser(email, password);

        if ("success".equals(result.get("status"))) {
            String token = (String) result.get("token");

            // Create cookie for  token
            Cookie cookie = new Cookie("cookie", token);
            cookie.setHttpOnly(true); // Prevent JavaScript access (secure)
            cookie.setSecure(false);  // Set true if using HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(10 * 60); // 10 minutes in seconds

            // Add cookie to response
            response.addCookie(cookie);
        }

        return result;
    }
    // READ ALL
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
