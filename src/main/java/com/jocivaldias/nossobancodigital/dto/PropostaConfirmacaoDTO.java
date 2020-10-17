package com.jocivaldias.nossobancodigital.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PropostaConfirmacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Preenchimento obrigatório.")
    private Boolean confirmado;

    public PropostaConfirmacaoDTO() {
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }
}
