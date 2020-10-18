package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.TokenAtivacao;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void bemVindoNovoCliente(Cliente obj);
    void insistirConfirmacaoCliente(Cliente obj);
    void simpleEmail(SimpleMailMessage msg);
    void registrarNovaSenha(TokenAtivacao tokenAtivacao);
    void senhaAtualizada(Conta conta);
}
