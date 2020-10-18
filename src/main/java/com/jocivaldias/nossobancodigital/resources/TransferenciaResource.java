package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.dto.TransferenciaDTO;
import com.jocivaldias.nossobancodigital.services.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/transferencias")
public class TransferenciaResource {

    private final TransferenciaService service;

    @Autowired
    public TransferenciaResource(TransferenciaService transferenciaService) {
        this.service = transferenciaService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> registrarTransferencias(@Valid @RequestBody List<TransferenciaDTO> listObjDto) {
        service.processaTransferencias(listObjDto);
        return ResponseEntity.ok().build();
    }

}
