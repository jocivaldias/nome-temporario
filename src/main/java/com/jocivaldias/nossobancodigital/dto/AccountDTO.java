package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.domain.Proposal;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Can't be blank.")
    private String branchNumber;

    @NotBlank(message = "Can't be blank.")
    private String accountNumber;

    @NotBlank(message = "Can't be blank.")
    private String bankNumber;

    @NotBlank(message = "Can't be blank.")
    private Proposal proposal;

    @NotBlank(message = "Can't be blank.")
    private BigDecimal balance;

    public AccountDTO() {
    }

    public AccountDTO(String branchNumber, String accountNumber, String bankNumber,
                      Proposal proposal, BigDecimal balance) {
        this.branchNumber = branchNumber;
        this.accountNumber = accountNumber;
        this.bankNumber = bankNumber;
        this.proposal = proposal;
        this.balance = balance;
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
}
