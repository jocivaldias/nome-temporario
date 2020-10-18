package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.TokenAtivacao;
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
        smm.setSubject("\nBem vindo ao Nosso Banco Digital!");
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
        smm.setSubject("\nNão perca essa oportunidade - Venha para o Nosso Banco Digital!");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Prezado(a) Cliente, percebemos que o senhor(a) não aceitou a proposta.\n\n"
                        + "Confire seus dados abaixo e aceite nossa proposta: \n"
                        + obj.toString() + "\n\n"
                        + "Não perca mais tempo!!! Venha para o banco digital"
        );
        return smm;
    }

    public void registrarNovaSenha(TokenAtivacao tokenAtivacao){
        SimpleMailMessage smm = prepareRegistrarNovaSenha(tokenAtivacao);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareRegistrarNovaSenha(TokenAtivacao tokenAtivacao) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(tokenAtivacao.getConta().getProposta().getCliente().getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nInstruções para cadastro de senha - Nosso Banco Digital: ");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Prezado(a) Cliente, o token para cadastro de sua senha é: " + tokenAtivacao.getToken() + "\n\n"
                        + "Atenciosamente, Nosso Banco Digital"
        );
        return smm;
    }

    public void senhaAtualizada(Conta conta){
        SimpleMailMessage smm = prepareSenhaAtualizada(conta);
        simpleEmail(smm);
    }

    protected SimpleMailMessage prepareSenhaAtualizada(Conta conta) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(conta.getProposta().getCliente().getEmail());
        smm.setFrom(sender);
        smm.setSubject("\nSenha Atualizada no Sistema - Nosso Banco Digital: ");
        smm.setSentDate(new Date(System.currentTimeMillis()));
        smm.setText(
                "Prezado(a) Cliente, sua senha foi atualizada no sistema" + "\n\n"
                        + "Atenciosamente, Nosso Banco Digital"
        );
        return smm;
    }

}
