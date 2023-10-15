package com.theos.bankapp.service.impl;

import com.theos.bankapp.dto.BankResponse;
import com.theos.bankapp.dto.EnquiryRequest;
import com.theos.bankapp.dto.UserRequest;

public interface UserService {
    BankResponse createAcount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
}
