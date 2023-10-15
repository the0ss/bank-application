package com.theos.bankapp.service.impl;

import com.theos.bankapp.dto.BankResponse;
import com.theos.bankapp.dto.UserRequest;

public interface UserService {
    BankResponse createAcount(UserRequest userRequest);

}
