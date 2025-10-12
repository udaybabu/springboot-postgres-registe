package com.example.springboot_postgres_register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot_postgres_register.model.User;
import com.example.springboot_postgres_register.repository.UserRepository;
import com.example.springboot_postgres_register.util.JwtUtil;
import  java.util.*;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // LOGIN (returns token if successful)
    public Map<String, Object> loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        Map<String, Object> response = new HashMap<>();

        if (user.isPresent()) {
            String token = JwtUtil.generateToken(email); // 10 min expiry handled in Util
            response.put("status", "success");
            response.put("message", "Login successful!");
            response.put("cookie", token);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password!");
        }

        return response;
    }

    // READ (All Users)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ (Single User)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // UPDATE
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    // DELETE
    public String deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted successfully!";
        } else {
            return "User not found with ID: " + id;
        }
    }
}
