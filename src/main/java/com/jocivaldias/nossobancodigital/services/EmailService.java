package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.ActivationToken;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void welcomeNewClient(Client obj);
    void insistCustomerConfirmation(Client obj);
    void simpleEmail(SimpleMailMessage msg);
    void registerNewPassword(ActivationToken activationToken);
    void updatedPassword(Account account);
}
