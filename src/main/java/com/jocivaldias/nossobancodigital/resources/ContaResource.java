package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.services.ContaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contas")
public class ContaResource {

    private final ContaService service;

    @Autowired
    public ContaResource(ContaService service) {
        this.service = service;
    }

    @ApiOperation(value = "Retorna a conta do cliente autenticado no sistema")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Conta retornada na resposta"),
            @ApiResponse(code = 403, message = "Cliente não autenticado"),
            @ApiResponse(code = 404, message = "Conta não encontrada"),
            @ApiResponse(code = 500, message = "Erro inesperado")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Conta> buscarPorId(@PathVariable Integer id) {
        Conta obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

}
