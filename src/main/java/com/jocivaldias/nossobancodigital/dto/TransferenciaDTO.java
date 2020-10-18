package com.jocivaldias.nossobancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDate;

public class TransferenciaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Preenchimento obrigatório.")
    @Min(0)
    private Double valorTransferencia;

    @NotNull(message = "Preenchimento obrigatório.")
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="America/Sao_Paulo")
    private LocalDate dataRealizacao;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String documentoIdentificador;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String codigoBancoOrigem;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String contaOrigem;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String agenciaOrigem;

    @NotNull(message = "Preenchimento obrigatório.")
    private Long idUnicoBancoTransferencia;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String contaDestino;

    @NotBlank(message = "Preenchimento obrigatório.")
    private String agenciaDestino;

    public TransferenciaDTO() {
    }

    public Double getValorTransferencia() {
        return valorTransferencia;
    }

    public void setValorTransferencia(Double valorTransferencia) {
        this.valorTransferencia = valorTransferencia;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public String getDocumentoIdentificador() {
        return documentoIdentificador;
    }

    public void setDocumentoIdentificador(String documentoIdentificador) {
        this.documentoIdentificador = documentoIdentificador;
    }

    public String getCodigoBancoOrigem() {
        return codigoBancoOrigem;
    }

    public void setCodigoBancoOrigem(String codigoBancoOrigem) {
        this.codigoBancoOrigem = codigoBancoOrigem;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getAgenciaOrigem() {
        return agenciaOrigem;
    }

    public void setAgenciaOrigem(String agenciaOrigem) {
        this.agenciaOrigem = agenciaOrigem;
    }

    public Long getIdUnicoBancoTransferencia() {
        return idUnicoBancoTransferencia;
    }

    public void setIdUnicoBancoTransferencia(Long idUnicoBancoTransferencia) {
        this.idUnicoBancoTransferencia = idUnicoBancoTransferencia;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public String getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(String agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }
}
