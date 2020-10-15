package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;
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
    @JoinColumn(unique = true)
    private Cliente cliente;

    private String filename;

    public Proposta() {
    }

    public Proposta(Integer id) {
        this.id = id;
        this.dataAbertura = new Date();
        this.status = StatusProposta.PENDENTE_DADOS_CLIENTE.getCod();
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
