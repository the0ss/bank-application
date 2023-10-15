package com.theos.bankapp.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.theos.bankapp.dto.AccountInfo;
import com.theos.bankapp.dto.BankResponse;
import com.theos.bankapp.dto.UserRequest;
import com.theos.bankapp.entity.user;
import com.theos.bankapp.repository.UserRepository;
import com.theos.bankapp.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankResponse createAcount(UserRequest userRequest) {
       /*
        * Creating an account - saving the new user to db
          Check if user already exists
        */
        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                            .responceCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                            .responceMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                            .build();
                            
        }
        user newUser = user.builder()
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .otherName(userRequest.getOtherName())
                        .gender(userRequest.getGender())
                        .address(userRequest.getAddress())
                        .stateofOrigin(userRequest.getStateofOrigin())
                        .accountNumber(AccountUtils.generateAccountNumber())
                        .email(userRequest.getEmail())
                        .accountBalance(BigDecimal.ZERO)
                        .phoneNumber(userRequest.getPhoneNumber())
                        .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                        .status("ACTIVE")
                        .build();

            user savedUser=userRepository.save(newUser);
            return BankResponse.builder()
                            .responceCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                            .responceMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                            .accountInfo(AccountInfo.builder()
                                        .accountBalance(savedUser.getAccountBalance())
                                        .accountNumber(savedUser.getAccountNumber())
                                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+ " "+ savedUser.getOtherName())
                                        .build())   
                            .build();
    }
}
