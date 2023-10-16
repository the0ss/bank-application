package com.theos.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theos.bankapp.entity.User;


public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
