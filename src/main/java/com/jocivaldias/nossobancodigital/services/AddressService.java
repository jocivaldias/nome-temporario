package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Address;
import com.jocivaldias.nossobancodigital.dto.newAddressDTO;
import com.jocivaldias.nossobancodigital.repositories.AddressRepository;
import com.jocivaldias.nossobancodigital.services.exception.DataIntegrityException;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository repo;

    @Autowired
    public AddressService(AddressRepository repo) {
        this.repo = repo;
    }

    public Address find(Integer id) {
        Optional<Address> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Address not found! Id: " + id + ", Type: " + Address.class.getName()
        ));
    }

    public Address fromDTO(newAddressDTO objDto) {
        return new Address(null, objDto.getZipCode(), objDto.getStreetName(), objDto.getNeighborhoodName(),
                objDto.getComplement(), objDto.getCity(), objDto.getState());
    }

    public Address insert(Address obj) {
        try {
            obj.setId(null);
            return repo.save(obj);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Customer already has address.");
        }
    }

    public Address update(Address obj) {
        Address newObj = find(obj.getId());
        updateAddressData(newObj, obj);
        return repo.save(newObj);
    }

    private void updateAddressData(Address newObj, Address obj) {
        newObj.setZipCode(obj.getZipCode());
        newObj.setStreetName(obj.getStreetName());
        newObj.setNeighborhoodName(obj.getNeighborhoodName());
        newObj.setComplement(obj.getComplement());
        newObj.setCity(obj.getCity());
        newObj.setState(obj.getState());
    }


}
