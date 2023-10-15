package com.theos.bankapp.utils;

import java.time.Year;
public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="The user already exists";
    
    public static final String ACCOUNT_CREATION_SUCCESS="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account Created successfully";
    public static String generateAccountNumber(){
        /*
        *  2023 + randomSixDigit
        */
        Year currYear=Year.now();
        int min=100000;
        int max=999999;

        int randNumber=(int) Math.floor(Math.random()*(max-min+1)+min);
        String year=String.valueOf(currYear);
        String randomNumber=String.valueOf(randNumber);
        StringBuilder accountNumber= new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }
}
