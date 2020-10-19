package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"id_unico_banco_transferencia", "codigo_banco_origem"})
})
public class Transferencia implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorTransferencia;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate dataRealizacao;

    @Column(nullable = false)
    private String documentoIdentificador;

    @Column(nullable = false, name = "codigo_banco_origem")
    private String codigoBancoOrigem;

    @Column(nullable = false)
    private String contaOrigem;

    @Column(nullable = false)
    private String agenciaOrigem;

    @Column(nullable = false, name = "id_unico_banco_transferencia")
    private Long idUnicoBancoTransferencia;

    @ManyToOne
    private Conta contaDestino;

    public Transferencia() {
    }

    public Transferencia(Long id, Double valorTransferencia, LocalDate dataRealizacao, String documentoIdentificador,
                         String codigoBancoOrigem, String contaOrigem, String agenciaOrigem,
                         Long idUnicoBancoTransferencia, Conta contaDestino) {
        this.id = id;
        this.valorTransferencia = valorTransferencia;
        this.dataRealizacao = dataRealizacao;
        this.documentoIdentificador = documentoIdentificador;
        this.codigoBancoOrigem = codigoBancoOrigem;
        this.contaOrigem = contaOrigem;
        this.agenciaOrigem = agenciaOrigem;
        this.idUnicoBancoTransferencia = idUnicoBancoTransferencia;
        this.contaDestino = contaDestino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transferencia)) return false;
        Transferencia conta = (Transferencia) o;
        return id.equals(conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
