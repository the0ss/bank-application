package com.theos.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theos.bankapp.entity.User;
import java.util.List;



public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
