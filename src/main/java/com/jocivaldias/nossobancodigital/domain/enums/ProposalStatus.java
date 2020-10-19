package com.jocivaldias.nossobancodigital.domain.enums;

public enum ProposalStatus {

    OPENED(1, "Proposal opened by the Client"),


    PENDING_CLIENT_ADDRESS(2, "Pending Address"),
    PENDING_CLIENT_DOCUMENTATION(3, "Pending Documentation"),
    PENDING_CLIENT_CONFIRMATION(4, "Pending Client Confirmation"),

    REFUSED(5, "Refused by Client"),

    PENDING_SYSTEM_ACCEPTANCE(6, "Pending System Acceptance"),
    RESEND_SYSTEM_ACCEPTANCE(7, "Resend System Acceptance"),

    CANCELED(8, "Proposal canceled by the validation system"),
    IMPLEMENTED(9, "Implemented Proposal");

    public int cod;
    public String descricao;

    private ProposalStatus(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static ProposalStatus toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (ProposalStatus x : ProposalStatus.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
