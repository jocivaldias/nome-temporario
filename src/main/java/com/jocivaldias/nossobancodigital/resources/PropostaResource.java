package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.dto.EnderecoNewDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.services.EnderecoService;
import com.jocivaldias.nossobancodigital.services.PropostaService;
import com.jocivaldias.nossobancodigital.services.exception.RegistrationStepException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value="/propostas")
public class PropostaResource {

    @Autowired
    private PropostaService service;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private PropostaService propostaService;

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<Proposta> buscarPorId(@PathVariable Integer id){
        Proposta obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> insereProposta(@Valid @RequestBody PropostaNewDTO objDto){
        Proposta obj = service.fromDTO(objDto);
        obj.setStatus(StatusProposta.PENDENTE_DADOS_CLIENTE);
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}/cliente-endereco")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Void> editaProposta(@PathVariable Integer id, @Valid @RequestBody PropostaDTO objDto){
        Proposta obj = service.fromDTO(objDto);
        obj.setId(id);
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/cliente-endereco",method=RequestMethod.POST)
    public ResponseEntity<Void> insereEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto){
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        proposta.getCliente().setEndereco(obj);
        obj.setCliente(proposta.getCliente());

        enderecoService.insert(obj);

        propostaService.updateStatus(proposta, StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}/cliente-documento")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}/cliente-endereco",method=RequestMethod.PUT)
    public ResponseEntity<Void> editaEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto){
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        obj.setId(proposta.getCliente().getEndereco().getId());

        enderecoService.update(obj);
        return ResponseEntity.noContent().build();
    }


}
