package com.jocivaldias.nossobancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jocivaldias.nossobancodigital.services.validation.PropostaUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;

@PropostaUpdate
public class PropostaDTO implements Serializable {
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
    private LocalDate dataNascimento;

    public PropostaDTO() {
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
