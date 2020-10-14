package com.jocivaldias.nossobancodigital.domain.enums;

public enum StatusProposta {

    ABERTA(1, "Proposta Aberta"),
    EFETIVADA(2, "Proposta Efetivada"),
    CANCELADA(3, "Proposta Cancelada");

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

    public static StatusProposta toEnum(Integer cod){
        if(cod == null){
            return null;
        }

        for(StatusProposta x : StatusProposta.values()){
            if(cod.equals(x.getCod())){
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
