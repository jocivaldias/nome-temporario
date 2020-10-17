package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.dto.EnderecoNewDTO;
import com.jocivaldias.nossobancodigital.repositories.EnderecoRepository;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository repo;

    @Autowired
    public EnderecoService(EnderecoRepository repo) {
        this.repo = repo;
    }

    public Endereco find(Integer id) {
        Optional<Endereco> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Endereço não encontrado! Id: " + id + ", Tipo: " + Endereco.class.getName()
        ));
    }

    public Endereco fromDTO(EnderecoNewDTO objDto) {
        Endereco endereco = new Endereco(null, objDto.getCep(), objDto.getRua(), objDto.getBairro(),
                objDto.getComplemento(), objDto.getCidade(), objDto.getEstado());
        return endereco;
    }

    public Endereco insert(Endereco obj) {
        obj.setId(null);
        return repo.save(obj);
    }

    public Endereco update(Endereco obj) {
        Endereco newObj = find(obj.getId());
        updateDadosBasicos(newObj, obj);
        return repo.save(newObj);
    }

    private void updateDadosBasicos(Endereco newObj, Endereco obj) {
        newObj.setCep(obj.getCep());
        newObj.setRua(obj.getRua());
        newObj.setBairro(obj.getBairro());
        newObj.setComplemento(obj.getComplemento());
        newObj.setCidade(obj.getCidade());
        newObj.setEstado(obj.getEstado());
    }


}
