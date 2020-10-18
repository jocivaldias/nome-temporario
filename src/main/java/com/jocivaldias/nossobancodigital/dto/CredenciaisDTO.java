package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.services.validation.Senha;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class CredenciaisDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String agencia;
    @NotBlank(message = "Preenchimento obrigatório.")
    private String conta;
    @Senha
    private String senha;

    public CredenciaisDTO() {
    }

    public CredenciaisDTO(@NotBlank(message = "Preenchimento obrigatório.") String agencia, @NotBlank(message = "Preenchimento obrigatório.") String conta, String senha) {
        this.agencia = agencia;
        this.conta = conta;
        this.senha = senha;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
