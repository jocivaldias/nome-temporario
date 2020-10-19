package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
//@Table(uniqueConstraints={
//        @UniqueConstraint(columnNames = {"id_unico_banco_transferencia", "codigo_banco_origem"})
//})
public class Transfer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double transferValue;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private LocalDate transferDate;

    @Column(nullable = false)
    private String identifierDocument;

    @Column(nullable = false)
    private String originBankNumber;

    @Column(nullable = false)
    private String originAccountNumber;

    @Column(nullable = false)
    private String originBranchNumber;

    @Column(nullable = false)
    private Long transferIdOriginBank;

    @ManyToOne
    private Account targetAccount;

    public Transfer() {
    }

    public Transfer(Long id, Double transferValue, LocalDate transferDate, String identifierDocument,
                    String originBankNumber, String originAccountNumber, String originBranchNumber,
                    Long transferIdOriginBank, Account accountDestino) {
        this.id = id;
        this.transferValue = transferValue;
        this.transferDate = transferDate;
        this.identifierDocument = identifierDocument;
        this.originBankNumber = originBankNumber;
        this.originAccountNumber = originAccountNumber;
        this.originBranchNumber = originBranchNumber;
        this.transferIdOriginBank = transferIdOriginBank;
        this.targetAccount = accountDestino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTransferValue() {
        return transferValue;
    }

    public void setTransferValue(Double transferValue) {
        this.transferValue = transferValue;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public String getIdentifierDocument() {
        return identifierDocument;
    }

    public void setIdentifierDocument(String identifierDocument) {
        this.identifierDocument = identifierDocument;
    }

    public String getOriginBankNumber() {
        return originBankNumber;
    }

    public void setOriginBankNumber(String originBankNumber) {
        this.originBankNumber = originBankNumber;
    }

    public String getOriginAccountNumber() {
        return originAccountNumber;
    }

    public void setOriginAccountNumber(String originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }

    public String getOriginBranchNumber() {
        return originBranchNumber;
    }

    public void setOriginBranchNumber(String originBranchNumber) {
        this.originBranchNumber = originBranchNumber;
    }

    public Long getTransferIdOriginBank() {
        return transferIdOriginBank;
    }

    public void setTransferIdOriginBank(Long transferIdOriginBank) {
        this.transferIdOriginBank = transferIdOriginBank;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        Transfer conta = (Transfer) o;
        return id.equals(conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
