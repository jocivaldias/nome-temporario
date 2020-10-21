package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.ActivationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService{

    @Value("${default.sender}")
    private String sender;

    @Override
    public void welcomeNewClient(Client client) {
        SimpleMailMessage simpleMailMessage = prepareWelcomeNewClientSimpleMailMessage(client);
        simpleEmail(simpleMailMessage);
    }

    protected SimpleMailMessage prepareWelcomeNewClientSimpleMailMessage(Client client){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("\nWelcome to Our Digital Bank!");
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(
                "Dear Customer, welcome to the digital bank\n\n"
                + "Client's data:\n"
                + client.toString() + "\n\n"
                + "Account Information:\n"
                + client.getProposal().getAccount().toString() + "\n\n"
                + "Sincerely, our digital bank."
        );
        return simpleMailMessage;
    }

    @Override
    public void insistCustomerConfirmation(Client client) {
        SimpleMailMessage smm = prepareInsistCustomerConfirmationSimpleMailMessage(client);
        simpleEmail(smm);
    }


    protected SimpleMailMessage prepareInsistCustomerConfirmationSimpleMailMessage(Client client){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(client.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("\nDon't miss this opportunity - Come to Our Digital Bank!");
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(
                "Dear Customer, we understand that you did not accept the proposal.\n\n"
                        + "Check your data below and accept our proposal: \n"
                        + client.toString() + "\n\n"
                        + "Don't waste any more time !!! Come to the digital bank"
        );
        return simpleMailMessage;
    }

    public void registerNewPassword(ActivationToken activationToken){
        SimpleMailMessage simpleMailMessage = prepareRegisterNewPasswordSimpleMailMessage(activationToken);
        simpleEmail(simpleMailMessage);
    }

    protected SimpleMailMessage prepareRegisterNewPasswordSimpleMailMessage(ActivationToken activationToken) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(activationToken.getAccount().getProposal().getClient().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("\nPassword registration instructions - Our Digital Bank: ");
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(
                "Dear Customer, the token for registering your password is: " + activationToken.getToken() + "\n\n"
                        + "Sincerely, Our Digital Bank"
        );
        return simpleMailMessage;
    }

    public void updatedPassword(Account account){
        SimpleMailMessage simpleMailMessage = prepareUpdatedPasswordSimpleMailMessage(account);
        simpleEmail(simpleMailMessage);
    }

    protected SimpleMailMessage prepareUpdatedPasswordSimpleMailMessage(Account account) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getProposal().getClient().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("\nPassword Updated in the System - Our Digital Bank: ");
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(
                "Dear Customer, your password has been updated in the system" + "\n\n"
                        + "Sincerely, Our Digital Bank"
        );
        return simpleMailMessage;
    }

}
