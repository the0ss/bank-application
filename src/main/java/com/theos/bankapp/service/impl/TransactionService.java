package com.theos.bankapp.service.impl;

import com.theos.bankapp.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
