package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Proposal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate proposalOpeningDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate proposalClosingDate;

    @Column(nullable = false)
    private Integer status;

    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @JsonIgnore
    private String filename;

    public Proposal() {
    }

    public Proposal(Integer id) {
        this.id = id;
        this.proposalOpeningDate = LocalDate.now();
        this.status = ProposalStatus.OPENED.getCod();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getProposalOpeningDate() {
        return proposalOpeningDate;
    }

    public void setProposalOpeningDate(LocalDate proposalOpeningDate) {
        this.proposalOpeningDate = proposalOpeningDate;
    }

    public LocalDate getProposalClosingDate() {
        return proposalClosingDate;
    }

    public void setProposalClosingDate(LocalDate proposalClosingDate) {
        this.proposalClosingDate = proposalClosingDate;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ProposalStatus getStatus() {
        return ProposalStatus.toEnum(this.status);
    }

    public void setStatus(ProposalStatus status) {
        this.status = status.getCod();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        if (!(o instanceof Proposal)) return false;
        Proposal proposal = (Proposal) o;
        return id.equals(proposal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
