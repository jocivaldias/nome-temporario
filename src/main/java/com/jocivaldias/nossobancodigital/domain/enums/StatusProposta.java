package com.jocivaldias.nossobancodigital.domain.enums;

public enum StatusProposta {

    PENDENTE_DADOS_CLIENTE(1, "Pendente Dados Cliente"),
    PENDENTE_ENDERECO_CLIENTE(2, "Pendente Endereço"),
    PENDENTE_DOCUMENTACAO_CLIENTE(3, "Pendente Documentação"),
    PENDENTE_CONFIRMACAO_CLIENTE(3, "Pendente Confirmação Cliente"),
    EFETIVADA(4, "Proposta Efetivada");

    public int cod;
    public String descricao;

    private StatusProposta(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusProposta toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (StatusProposta x : StatusProposta.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
