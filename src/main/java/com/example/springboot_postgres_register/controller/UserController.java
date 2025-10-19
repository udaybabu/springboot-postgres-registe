package com.example.springboot_postgres_register.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.springboot_postgres_register.model.User;
import com.example.springboot_postgres_register.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ CREATE USER
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // ✅ LOGIN (sets token in cookie)
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData,
                                         HttpServletResponse response) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        Map<String, Object> result = userService.loginUser(email, password);

        if ("success".equals(result.get("status"))) {
            String token = (String) result.get("token");

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);  // set to true if HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(10 * 60); // 10 minutes

            response.addCookie(cookie);
        }
        result.remove("token");

        return result;
    }

    // ✅ READ ALL USERS (with pagination)
    @GetMapping
    public Map<String, Object> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return userService.getAllUsers(page, size);
    }

    // ✅ READ SINGLE USER BY ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ UPDATE USER
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
