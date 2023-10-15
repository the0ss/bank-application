package com.theos.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theos.bankapp.entity.user;

public interface UserRepository extends JpaRepository<user,Long>{
    boolean existsByEmail(String email);
}
