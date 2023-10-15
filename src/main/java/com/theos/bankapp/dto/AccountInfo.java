package com.theos.bankapp.dto;

import java.math.BigDecimal;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    
    private String accountName;
    private String accountNumber;
    private BigDecimal accountBalance;
}
