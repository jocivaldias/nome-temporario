package com.jocivaldias.nossobancodigital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ProposalConfirmationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Can't be blank.")
    private Boolean confirmation;

    public ProposalConfirmationDTO() {
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }
}
