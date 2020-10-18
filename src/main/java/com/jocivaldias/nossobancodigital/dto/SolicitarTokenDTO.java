package com.jocivaldias.nossobancodigital.dto;

import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class SolicitarTokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Preenchimento obrigatório.")
    @Email
    private String email;

    @NotBlank(message = "Preenchimento obrigatório.")
    @CPF
    private String cpf;

    public SolicitarTokenDTO() {
    }

    public SolicitarTokenDTO(String email, String cpf) {
        this.email = email;
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
