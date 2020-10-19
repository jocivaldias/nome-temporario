package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.services.validation.Senha;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class CredentialsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Can't be blank.")
    private String branchNumber;
    @NotBlank(message = "Can't be blank.")
    private String accountNumber;
    @Senha
    private String password;

    public CredentialsDTO() {
    }

    public CredentialsDTO(String branchNumber, String accountNumber, String password) {
        this.branchNumber = branchNumber;
        this.accountNumber = accountNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
