package com.example.springboot_postgres_register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springboot_postgres_register.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
}
