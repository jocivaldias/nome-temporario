package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.dto.EnderecoNewDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.services.EnderecoService;
import com.jocivaldias.nossobancodigital.services.PropostaService;
import com.jocivaldias.nossobancodigital.services.StorageService;
import com.jocivaldias.nossobancodigital.services.exception.DataIntegrityException;
import com.jocivaldias.nossobancodigital.services.exception.RegistrationStepException;
import com.jocivaldias.nossobancodigital.services.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value="/propostas")
public class PropostaResource {

    private final PropostaService service;

    private final EnderecoService enderecoService;

    private final PropostaService propostaService;

    private final StorageService storageService;

    @Value("${document.prefix}")
    private String prefix;

    @Autowired
    public PropostaResource(PropostaService service, EnderecoService enderecoService, PropostaService propostaService, StorageService storageService) {
        this.service = service;
        this.enderecoService = enderecoService;
        this.propostaService = propostaService;
        this.storageService = storageService;
    }

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
                .replacePath("")
                .path("/{id}/endereco")
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

    @RequestMapping(value = "/{id}/endereco",method=RequestMethod.POST)
    public ResponseEntity<Void> insereEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto){
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        proposta.getCliente().setEndereco(obj);
        obj.setCliente(proposta.getCliente());

        try {
            enderecoService.insert(obj);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Cliente já possui endereço.");
        }

        propostaService.updateStatus(proposta, StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("")
                .path("/{id}/documento")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}/endereco",method=RequestMethod.PUT)
    public ResponseEntity<Void> editaEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto){
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        obj.setId(proposta.getCliente().getEndereco().getId());

        enderecoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/{id}/documento", method=RequestMethod.POST)
    public ResponseEntity<Void> uploadClienteDocumento(@PathVariable Integer id, @RequestParam(name="file") MultipartFile file){
        Proposta proposta = propostaService.find(id);
        if(proposta.getStatus().getCod() < StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE.getCod()){
            throw new RegistrationStepException("Violação de etapa. Status da Proposta: " + StatusProposta.toEnum(proposta.getStatus().getCod()+1));
        }

        service.uploadDocumento(proposta, file);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .replacePath("")
                .path("/{id}/confirmacao")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value="/{id}/documento", method=RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        Proposta proposta = service.find(id);

        Resource file = storageService.loadAsResource(proposta.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
