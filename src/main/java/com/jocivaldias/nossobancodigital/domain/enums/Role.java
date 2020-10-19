package com.jocivaldias.nossobancodigital.domain.enums;

public enum Role {

    CLIENT(1, "ROLE_CLIENT");

    public int cod;
    public String description;

    private Role(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static Role toEnum(Integer cod){
        if(cod == null){
            return null;
        }

        for(Role x : Role.values()){
            if(cod.equals(x.getCod())){
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid ID: " + cod);
    }



}
