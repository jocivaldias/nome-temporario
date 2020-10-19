package com.jocivaldias.nossobancodigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDate;

public class TransferDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Can't be blank.")
    @Min(0)
    private Double transferValue;

    @NotNull(message = "Can't be blank.")
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="America/Sao_Paulo")
    private LocalDate transferDate;

    @NotBlank(message = "Can't be blank.")
    private String identifierDocument;

    @NotBlank(message = "Can't be blank.")
    private String originBankNumber;

    @NotBlank(message = "Can't be blank.")
    private String originAccountNumber;

    @NotBlank(message = "Can't be blank.")
    private String originBranchNumber;

    @NotNull(message = "Can't be blank.")
    private Long transferIdOriginBank;

    @NotBlank(message = "Can't be blank.")
    private String targetAccountNumber;

    @NotBlank(message = "Can't be blank.")
    private String targetBranchNumber;

    public TransferDTO() {
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

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getTargetBranchNumber() {
        return targetBranchNumber;
    }

    public void setTargetBranchNumber(String targetBranchNumber) {
        this.targetBranchNumber = targetBranchNumber;
    }
}
