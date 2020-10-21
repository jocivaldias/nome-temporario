package com.jocivaldias.nossobancodigital.dto;

import com.jocivaldias.nossobancodigital.services.validation.ZipCode;

import javax.validation.constraints.NotBlank;

public class NewAddressDTO {

    @NotBlank(message = "Can't be blank.")
    @ZipCode(message = "Invalid CEP.")
    private String zipCode;

    @NotBlank(message = "Can't be blank.")
    private String streetName;

    @NotBlank(message = "Can't be blank.")
    private String neighborhoodName;

    @NotBlank(message = "Can't be blank.")
    private String complement;

    @NotBlank(message = "Can't be blank.")
    private String city;

    @NotBlank(message = "Can't be blank.")
    private String state;

    public NewAddressDTO() {
    }

    public NewAddressDTO(String zipCode, String streetName, String neighborhoodName, String complement, String city, String state) {
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.neighborhoodName = neighborhoodName;
        this.complement = complement;
        this.city = city;
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
