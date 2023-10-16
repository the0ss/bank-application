package com.theos.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.theos.bankapp.dto.BankResponse;
import com.theos.bankapp.dto.CreditDebitRequest;
import com.theos.bankapp.dto.EnquiryRequest;
import com.theos.bankapp.dto.LoginDto;
import com.theos.bankapp.dto.TransferRequest;
import com.theos.bankapp.dto.UserRequest;
import com.theos.bankapp.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name="User Management APIs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
        summary = "Create new User Account",
        description= "Creating a new user and assiging an account ID"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Http status 201 CREATED"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAcount(userRequest);
    }
    @Operation(
        summary = "Balance Enquiry",
        description= "Given a Account no , check how much the user has"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http status 200 CREATED"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest); 
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }
    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

}

