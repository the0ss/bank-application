package com.theos.bankapp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    
    private String responceCode;
    private String responceMessage;
    private AccountInfo accountInfo;
}
