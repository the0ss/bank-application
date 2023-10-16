package com.theos.bankapp.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.theos.bankapp.entity.Transaction;
import com.theos.bankapp.service.impl.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/statement")
@AllArgsConstructor
public class TransactionController {

    private BankStatement bankStatement;

    @GetMapping()
    public List<Transaction> generateBankStatement(@RequestParam String accountNo,
                                                    @RequestParam String startDate,
                                                    @RequestParam String endDate) throws FileNotFoundException, DocumentException{
            return bankStatement.gererateSatement(accountNo, startDate, endDate);
    }
}
