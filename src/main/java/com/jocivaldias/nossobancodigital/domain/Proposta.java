package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Proposta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate dataAbertura;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate dataFechamento; // Seja com falha ou sucesso

    @Column(nullable = false)
    private Integer status;

    @OneToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    @OneToOne(cascade = CascadeType.ALL)
    private Conta conta;

    @JsonIgnore
    private String filename;

    public Proposta() {
    }

    public Proposta(Integer id) {
        this.id = id;
        this.dataAbertura = LocalDate.now();
        this.status = StatusProposta.ABERTA.getCod();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public StatusProposta getStatus() {
        return StatusProposta.toEnum(this.status);
    }

    public void setStatus(StatusProposta status) {
        this.status = status.getCod();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proposta)) return false;
        Proposta proposta = (Proposta) o;
        return id.equals(proposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
