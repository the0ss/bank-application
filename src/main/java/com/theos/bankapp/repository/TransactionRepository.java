package com.theos.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theos.bankapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
    
}
