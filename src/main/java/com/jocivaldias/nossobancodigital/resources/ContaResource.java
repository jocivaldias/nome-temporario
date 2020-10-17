package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.services.ContaService;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Conta> buscarPorId(@PathVariable Integer id) {
        Conta obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

}
