package com.theos.bankapp.service.impl;

import com.theos.bankapp.dto.EmailDetails;

public interface EmailService {
    
    void sendEmailAlerts(EmailDetails emailDetails);
    void sendEmailWithAttachments(EmailDetails emailDetails);
}
