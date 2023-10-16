package com.theos.bankapp.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    
    @Schema(
        name = "Account name"
    )
    private String accountName;
    @Schema(
        name = "Account number"
    )
    private String accountNumber;
    @Schema(
        name = "Account Balance"
    )
    private BigDecimal accountBalance;
}
