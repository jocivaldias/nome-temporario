package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Proposta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone="America/Sao_Paulo")
    private Date dataAbertura;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone="America/Sao_Paulo")
    private Date dataFechamento; // Seja com falha ou sucesso

    @Column(nullable = false)
    private Integer status;

    @OneToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    public Proposta() {
    }

    public Proposta(Integer id) {
        this.id = id;
        this.dataAbertura = new Date();
        this.status = StatusProposta.ABERTA.getCod();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
