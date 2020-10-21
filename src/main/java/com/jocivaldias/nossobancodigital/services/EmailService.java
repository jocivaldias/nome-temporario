package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.ActivationToken;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void welcomeNewClient(Client client);
    void insistCustomerConfirmation(Client client);
    void simpleEmail(SimpleMailMessage simpleMailMessage);
    void registerNewPassword(ActivationToken activationToken);
    void updatedPassword(Account account);
}
