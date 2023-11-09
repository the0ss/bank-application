package com.theos.bankapp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.theos.bankapp.config.JwtTokenProvider;
import com.theos.bankapp.dto.AccountInfo;
import com.theos.bankapp.dto.BankResponse;
import com.theos.bankapp.dto.CreditDebitRequest;
import com.theos.bankapp.dto.EmailDetails;
import com.theos.bankapp.dto.EnquiryRequest;
import com.theos.bankapp.dto.LoginDto;
import com.theos.bankapp.dto.TransactionDto;
import com.theos.bankapp.dto.TransferRequest;
import com.theos.bankapp.dto.UserRequest;
import com.theos.bankapp.entity.Role;
import com.theos.bankapp.entity.User;
import com.theos.bankapp.repository.UserRepository;
import com.theos.bankapp.utils.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
        User newUser = User.builder()
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .otherName(userRequest.getOtherName())
                        .gender(userRequest.getGender())
                        .address(userRequest.getAddress())
                        .stateofOrigin(userRequest.getStateofOrigin())
                        .accountNumber(AccountUtils.generateAccountNumber())
                        .email(userRequest.getEmail())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .accountBalance(BigDecimal.ZERO)
                        .phoneNumber(userRequest.getPhoneNumber())
                        .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                        .status("ACTIVE")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();

            User savedUser=userRepository.save(newUser);
            EmailDetails emailDetails=EmailDetails.builder()
                                        .recipient(savedUser.getEmail())
                                        .subject("ACCOUNT CREATION")
                                        .messageBody("Congrats mf ur account opened.\n"+ savedUser.getFirstName())
                                        .build();
            emailService.sendEmailAlerts(emailDetails);                

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

    public BankResponse login(LoginDto loginDto){
      Authentication authentication=null;
      authentication=authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
      );
      EmailDetails loginAlert=EmailDetails.builder()
                              .subject("You're logged in!")
                              .recipient(loginDto.getEmail())
                              .messageBody("You logged into your Account. If you didn't initiate the request, Please contact bank.")
                              .build();
        emailService.sendEmailAlerts(loginAlert);
      return BankResponse.builder()
                      .responceCode("Login Success")
                      .responceMessage(jwtTokenProvider.generateToken(authentication))
                      .build();
      
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
      //check if provided acc no is present in db
      boolean isAccountExist=userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
      if(!isAccountExist){
        return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                        .responceMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      User foundUser= userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
      return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_FOUND_CODE)
                        .responceMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                          .accountName(foundUser.getFirstName())
                                          .accountBalance(foundUser.getAccountBalance())
                                          .accountNumber(foundUser.getAccountNumber())
                                          .build())
                        .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
      boolean isAccountExist=userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
      if(!isAccountExist){
        return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
      }
      User foundUser= userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
      return foundUser.getFirstName()+" "+foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
      //check if account exist
      boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
      if(!isAccountExist){
        return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                        .responceMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      User userToCredit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
      userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
      userRepository.save(userToCredit);
      return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
                        .responceMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                          .accountName(userToCredit.getFirstName())
                                          .accountBalance(userToCredit.getAccountBalance())
                                          .accountNumber(creditDebitRequest.getAccountNumber())
                                          .build())
                        .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
      //check if account exist
      boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
      if(!isAccountExist){
        return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                        .responceMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      //check if account to be debited is not greater than account balance
      User userToDebit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
      BigInteger availableBalance=userToDebit.getAccountBalance().toBigInteger();
      BigInteger debitAmount=creditDebitRequest.getAmount().toBigInteger();
      if(availableBalance.intValue()<debitAmount.intValue()){
        return BankResponse.builder()
                        .responceCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                        .responceMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
      userRepository.save(userToDebit);
      return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
                        .responceMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                          .accountName(userToDebit.getFirstName())
                                          .accountBalance(userToDebit.getAccountBalance())
                                          .accountNumber(creditDebitRequest.getAccountNumber())
                                          .build())
                        .build();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
      // we are logged in from the transferring account
      // check if the debit amount is not greater than the account balance
      // get the account of the credit (check if exist)
      // credit and debit the amount
      boolean isDestinationAccountExists=userRepository.existsByAccountNumber(transferRequest.getAccountNumberTo());
      if(!isDestinationAccountExists){
        return BankResponse.builder()
                        .responceCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                        .responceMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      //DEBIT
      User sourceAccountUser=userRepository.findByAccountNumber(transferRequest.getAccountNumberFrom());
      if(transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
          return BankResponse.builder()
                        .responceCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                        .responceMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                        .accountInfo(null)
                        .build();
      }
      sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
      userRepository.save(sourceAccountUser);

      TransactionDto debitDto=TransactionDto.builder()
                                    .accountNumber(sourceAccountUser.getAccountNumber())
                                    .transactionType("DEBIT")
                                    .amount(transferRequest.getAmount())
                                    .build();
      transactionService.saveTransaction(debitDto);

      EmailDetails debitAlert=EmailDetails.builder()
                                  .subject("DEBIT ALERT")
                                  .recipient(sourceAccountUser.getEmail())
                                  .messageBody("The sum of "+transferRequest.getAmount()+"has been deducted from your account.")
                                  .build();
      emailService.sendEmailAlerts(debitAlert);
  // Credit
      User destinationAccountUser=userRepository.findByAccountNumber(transferRequest.getAccountNumberTo());
      destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
      userRepository.save(destinationAccountUser);

      TransactionDto transactionDto=TransactionDto.builder()
                                    .accountNumber(destinationAccountUser.getAccountNumber())
                                    .transactionType("CREDIT")
                                    .amount(transferRequest.getAmount())
                                    .build();
      transactionService.saveTransaction(transactionDto);
      //      send mail to receiver
      EmailDetails creditAlert=EmailDetails.builder()
                                  .subject("CREDIT ALERT")
                                  .recipient(destinationAccountUser.getEmail())
                                  .messageBody("The sum of "+transferRequest.getAmount()+"has been credited to your account.")
                                  .build();
      emailService.sendEmailAlerts(creditAlert);

      return BankResponse.builder()
                        .responceCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                        .responceMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                        .accountInfo(null)
                        .build();
    }

}
