package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.dto.EnderecoNewDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaConfirmacaoDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.services.*;
import com.jocivaldias.nossobancodigital.services.exception.DataIntegrityException;
import com.jocivaldias.nossobancodigital.services.exception.RegistrationStepException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/propostas")
public class PropostaResource {

    private final PropostaService service;

    private final EnderecoService enderecoService;

    private final PropostaService propostaService;

    private final StorageService storageService;

    private final ContaService contaService;

    private final EmailService emailService;

    @Autowired
    public PropostaResource(PropostaService service, EnderecoService enderecoService, PropostaService propostaService,
                            StorageService storageService, ContaService contaService, EmailService emailService) {
        this.service = service;
        this.enderecoService = enderecoService;
        this.propostaService = propostaService;
        this.storageService = storageService;
        this.contaService = contaService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Retorna a proposta pelo id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Proposta retornada com sucesso"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Proposta> buscarPorId(@PathVariable Integer id) {
        Proposta obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Cria uma proposta com dados básicos do cliente")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Proposta criada com sucesso"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insereProposta(@Valid @RequestBody PropostaNewDTO objDto) {
        Proposta obj = service.fromDTO(objDto);
        obj.setStatus(StatusProposta.ABERTA);
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/propostas/{id}/endereco")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Edita os dados básicos do Cliente de uma proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Proposta alterada"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> editaProposta(@PathVariable Integer id, @Valid @RequestBody PropostaDTO objDto) {
        Proposta obj = service.fromDTO(objDto);
        obj.setId(id);
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Insere Endereço do Cliente em uma proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Endereço adicionado a Proposta"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/endereco", method = RequestMethod.POST)
    public ResponseEntity<Void> insereEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto) {
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        proposta.getCliente().setEndereco(obj);
        obj.setCliente(proposta.getCliente());

        enderecoService.insert(obj);

        propostaService.updateStatus(proposta, StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/propostas/{id}/documento")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Alteração do Endereço do Cliente em uma proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Endereço alterado"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/endereco", method = RequestMethod.PUT)
    public ResponseEntity<Void> editaEnderecoClienteProposta(@PathVariable Integer id, @Valid @RequestBody EnderecoNewDTO objDto) {
        Endereco obj = enderecoService.fromDTO(objDto);
        Proposta proposta = propostaService.find(id);
        obj.setId(proposta.getCliente().getEndereco().getId());

        enderecoService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Inserção da frente do CPF (arquivo) na Proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Documento inserido na Proposta"),
            @ApiResponse(code = 400, message = "Erro na validação do arquivo"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 422, message = "Violação na etapa de cadastro"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/documento", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadClienteDocumento(@PathVariable Integer id, @RequestParam MultipartFile file) {
        Proposta proposta = propostaService.find(id);

        if (proposta.getStatus().getCod() < StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE.getCod()) {
            throw new RegistrationStepException("Violação de etapa. Status da Proposta: " + StatusProposta.toEnum(proposta.getStatus().getCod()));
        }

        service.uploadDocumento(proposta, file);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/propostas/{id}/confirmacao")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Download do arquivo inserido na Proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Documento retornado no corpo da requisição"),
            @ApiResponse(code = 400, message = "Erro no sistema de arquivos"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/documento", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        Proposta proposta = service.find(id);

        Resource file = storageService.loadAsResource(proposta.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ApiOperation(value = "Confirmação/Desconfirmação do Cliente para efetivação da Proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Confirmação/Desconfirmação do cliente registrada"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 422, message = "Violação na etapa de cadastro"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/confirmacao", method = RequestMethod.POST)
    public ResponseEntity<Void> confirmaProposta(@PathVariable Integer id, @RequestBody PropostaConfirmacaoDTO objConfirmado) {
        Proposta proposta = service.find(id);

        if (proposta.getStatus().getCod() != StatusProposta.PENDENTE_CONFIRMACAO_CLIENTE.getCod()) {
            throw new RegistrationStepException("Violação de etapa. Status da Proposta: " + StatusProposta.toEnum(proposta.getStatus().getCod()));
        }

        service.updateStatus(proposta, StatusProposta.PENDENTE_LIBERACAO_SISTEMA);

        if (objConfirmado.getConfirmado()) { // Cliente confirmou a proposta
            contaService.liberarConta(proposta);
        } else { // Cliente negou a proposta
            emailService.insistirConfirmacaoCliente(proposta.getCliente());
        }

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Confirmação/Desconfirmação da API de Validação para efetivação da Proposta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Confirmação/Desconfirmação da API registrada"),
            @ApiResponse(code = 400, message = "Erro na validação dos dados"),
            @ApiResponse(code = 404, message = "Proposta não encontrada"),
            @ApiResponse(code = 422, message = "Violação na etapa de cadastro"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}/confirmacao-api", method = RequestMethod.POST)
    public ResponseEntity<Void> confirmaPropostaApi(@PathVariable Integer id, @RequestBody PropostaConfirmacaoDTO objConfirmado) {
        Proposta proposta = service.find(id);

        if (proposta.getStatus().getCod() != StatusProposta.PENDENTE_LIBERACAO_SISTEMA.getCod()) {
            throw new RegistrationStepException("Violação de etapa. Status da Proposta: " + StatusProposta.toEnum(proposta.getStatus().getCod()));
        }

        if (objConfirmado.getConfirmado()) { // API  confirmou a documentação
            contaService.criaConta(proposta);
            emailService.bemVindoNovoCliente(proposta.getCliente());
            service.updateStatus(proposta, StatusProposta.LIBERADA);
            service.finalizaProposta(proposta);
        } else { // API negou a documentação
            service.updateStatus(proposta, StatusProposta.CANCELADA);
            service.finalizaProposta(proposta);
        }

        return ResponseEntity.ok().build();
    }
}
