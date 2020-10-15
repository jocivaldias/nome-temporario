package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.services.validation.Cep;

import javax.validation.constraints.NotBlank;

public class EnderecoNewDTO {

    @NotBlank(message = "Preenchimento obrigatório.")
    @Cep(message = "CEP inválido.")
    private String cep;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String rua;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String bairro;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String complemento;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String cidade;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String estado;

    public EnderecoNewDTO() {
    }

    public EnderecoNewDTO(String cep, String rua, String bairro, String complemento, String cidade, String estado) {
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
