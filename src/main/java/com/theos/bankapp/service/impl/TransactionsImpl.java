package com.theos.bankapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.theos.bankapp.dto.TransactionDto;
import com.theos.bankapp.entity.Transaction;
import com.theos.bankapp.repository.TransactionRepository;

@Service
public class TransactionsImpl  implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction=Transaction.builder()
                                    .transactionType(transactionDto.getTransactionType())
                                    .accountNumber(transactionDto.getAccountNumber())
                                    .amount(transactionDto.getAmount())
                                    .status("Success!")   
                                    .build();
        transactionRepository.save(transaction);
        System.out.println("Transction Saved Successfully!");
    }
    
}
