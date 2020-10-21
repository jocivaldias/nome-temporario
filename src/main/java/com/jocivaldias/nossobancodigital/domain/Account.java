package com.jocivaldias.nossobancodigital.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 4)
    private String branchNumber;

    @Column(nullable = false, length = 8)
    private String accountNumber;

    @Column(nullable = false, length = 3)
    private String bankNumber;

    @OneToOne(mappedBy = "account")
    private Proposal proposal;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @JsonIgnore
    private String password;

    public Account() {
    }

    public Account(Integer id, String branchNumber, String accountNumber, String bankNumber, Proposal proposal,
                   BigDecimal balance, String password) {
        this.id = id;
        this.branchNumber = branchNumber;
        this.accountNumber = accountNumber;
        this.bankNumber = bankNumber;
        this.proposal = proposal;
        this.balance = balance;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("Branch Number='").append(branchNumber).append('\'');
        sb.append(", Account Number='").append(accountNumber).append('\'');
        sb.append(", Bank number='").append(bankNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
