package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.services.validation.Senha;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class CadastrarSenhaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Preenchimento obrigatório.")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Preenchimento obrigatório.")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String token;

    @NotBlank(message = "Preenchimento obrigatório.")
    @Senha
    private String senha;

    public CadastrarSenhaDTO() {
    }

    public CadastrarSenhaDTO(String email, String cpf, String token, String senha) {
        this.email = email;
        this.cpf = cpf;
        this.token = token;
        this.senha = senha;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
