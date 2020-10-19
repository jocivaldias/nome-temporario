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
    public void welcomeNewClient(Client obj) {
        SimpleMailMessage smm = prepareWelcomeNewClientSimpleMailMessage(obj);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareWelcomeNewClientSimpleMailMessage(Client obj){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(obj.getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nWelcome to Our Digital Bank!");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Dear Customer, welcome to the digital bank\n\n"
                + "Client's data:\n"
                + obj.toString() + "\n\n"
                + "Account Information:\n"
                + obj.getProposal().getAccount().toString() + "\n\n"
                + "Sincerely, our digital bank."
        );
        return smm;
    }

    @Override
    public void insistCustomerConfirmation(Client obj) {
        SimpleMailMessage smm = prepareInsistCustomerConfirmationSimpleMailMessage(obj);
        simpleEmail(smm);
    }


    protected SimpleMailMessage prepareInsistCustomerConfirmationSimpleMailMessage(Client obj){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(obj.getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nDon't miss this opportunity - Come to Our Digital Bank!");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Dear Customer, we understand that you did not accept the proposal.\n\n"
                        + "Check your data below and accept our proposal: \n"
                        + obj.toString() + "\n\n"
                        + "Don't waste any more time !!! Come to the digital bank"
        );
        return smm;
    }

    public void registerNewPassword(ActivationToken activationToken){
        SimpleMailMessage smm = prepareRegisterNewPasswordSimpleMailMessage(activationToken);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareRegisterNewPasswordSimpleMailMessage(ActivationToken activationToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(activationToken.getAccount().getProposal().getClient().getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nPassword registration instructions - Our Digital Bank: ");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Dear Customer, the token for registering your password is: " + activationToken.getToken() + "\n\n"
                        + "Sincerely, Our Digital Bank"
        );
        return smm;
    }

    public void updatedPassword(Account account){
        SimpleMailMessage smm = prepareUpdatedPasswordSimpleMailMessage(account);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareUpdatedPasswordSimpleMailMessage(Account account) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(account.getProposal().getClient().getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nPassword Updated in the System - Our Digital Bank: ");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Dear Customer, your password has been updated in the system" + "\n\n"
                        + "Sincerely, Our Digital Bank"
        );
        return smm;
    }

}
