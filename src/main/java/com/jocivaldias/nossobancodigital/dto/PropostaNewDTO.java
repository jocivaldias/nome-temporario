package com.jocivaldias.nossobancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jocivaldias.nossobancodigital.services.validation.PropostaInsert;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

@PropostaInsert
public class PropostaNewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    //Dados cliente
    @NotBlank(message = "Preenchimento obrigatório.")
    private String nome;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String sobrenome;

    @NotBlank(message = "Preenchimento obrigatório.")
    @Email(message = "E-mail inválido")
    private String email;

    @NotNull(message = "Preenchimento obrigatório.")
    @Past(message = "Data de nascimento inválida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="America/Sao_Paulo")
    private Date dataNascimento;

    @NotBlank(message = "Preenchimento obrigatório.")
    @CPF
    private String cpf;

    public PropostaNewDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
