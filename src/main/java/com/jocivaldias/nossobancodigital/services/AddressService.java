package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Address;
import com.jocivaldias.nossobancodigital.dto.NewAddressDTO;
import com.jocivaldias.nossobancodigital.repositories.AddressRepository;
import com.jocivaldias.nossobancodigital.services.exception.DataIntegrityException;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address find(Integer id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.orElseThrow(() -> new ObjectNotFoundException(
                "Address not found! Id: " + id + ", Type: " + Address.class.getName()
        ));
    }

    public Address fromDTO(NewAddressDTO newAddressDTO) {
        return new Address(null, newAddressDTO.getZipCode(), newAddressDTO.getStreetName(),
                newAddressDTO.getNeighborhoodName(), newAddressDTO.getComplement(), newAddressDTO.getCity(),
                newAddressDTO.getState());
    }

    public Address insert(Address address) {
        try {
            address.setId(null);
            return addressRepository.save(address);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Customer already has address.");
        }
    }

    public Address update(Address address) {
        Address saveAddress = find(address.getId());
        updateAddressData(saveAddress, address);
        return addressRepository.save(saveAddress);
    }

    private void updateAddressData(Address toAddress, Address fromAddress) {
        toAddress.setZipCode(fromAddress.getZipCode());
        toAddress.setStreetName(fromAddress.getStreetName());
        toAddress.setNeighborhoodName(fromAddress.getNeighborhoodName());
        toAddress.setComplement(fromAddress.getComplement());
        toAddress.setCity(fromAddress.getCity());
        toAddress.setState(fromAddress.getState());
    }


}
