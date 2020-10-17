package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void bemVindoNovoCliente(Cliente obj);
    void insistirConfirmacaoCliente(Cliente obj);
    void simpleEmail(SimpleMailMessage msg);
}
