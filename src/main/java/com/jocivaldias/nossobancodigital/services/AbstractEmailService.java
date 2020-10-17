package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService{

    @Value("${default.sender}")
    private String sender;

    @Override
    public void bemVindoNovoCliente(Cliente obj) {
        SimpleMailMessage smm = prepareBemVindoSimpleMailMessage(obj);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareBemVindoSimpleMailMessage(Cliente obj){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(obj.getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nPedido confirmado! Código: " + obj.getId());
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Prezado(a) Cliente, bem vindo ao banco digital\n\n"
                + "Dados do Cliente:\n"
                + obj.toString() + "\n\n"
                + "Dados da Conta:\n"
                + obj.getProposta().getConta().toString() + "\n\n"
                + "Atenciosamente, nosso banco digital."
        );
        return smm;
    }

    @Override
    public void insistirConfirmacaoCliente(Cliente obj) {
        SimpleMailMessage smm = prepareInsistirSimpleMailMessage(obj);
        simpleEmail(smm);
    }


    protected SimpleMailMessage prepareInsistirSimpleMailMessage(Cliente obj){
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(obj.getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nPedido confirmado! Código: " + obj.getId());
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Prezado(a) Cliente, percebemos que o senhor(a) não aceitou a proposta.\n\n"
                        + "Confire seus dados abaixo e aceite nossa proposta: \n"
                        + obj.toString() + "\n\n"
                        + "Não perca mais tempo!!! Venha para o banco digital"
        );
        return smm;
    }

}
