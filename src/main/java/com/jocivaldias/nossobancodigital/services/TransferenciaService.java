package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.Transferencia;
import com.jocivaldias.nossobancodigital.dto.TransferenciaDTO;
import com.jocivaldias.nossobancodigital.repositories.ContaRepository;
import com.jocivaldias.nossobancodigital.repositories.TransferenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferenciaService {
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository repo;
    private final ContaRepository contaRepository;
    private final ContaService contaService;

    @Autowired
    public TransferenciaService(TransferenciaRepository repo, ContaRepository contaRepository,
                                ContaService contaService) {
        this.repo = repo;
        this.contaRepository = contaRepository;
        this.contaService = contaService;
    }

    public Transferencia insert(Transferencia obj){
        obj.setId(null);
        return repo.save(obj);
    }

    public Transferencia fromDTO(TransferenciaDTO objDto) {
        Transferencia transferencia = new Transferencia();

        transferencia.setId(null);
        transferencia.setAgenciaOrigem(objDto.getAgenciaOrigem());
        transferencia.setCodigoBancoOrigem(objDto.getCodigoBancoOrigem());
        transferencia.setContaOrigem(objDto.getContaOrigem());
        transferencia.setDataRealizacao(objDto.getDataRealizacao());
        transferencia.setDocumentoIdentificador(objDto.getDocumentoIdentificador());
        transferencia.setIdUnicoBancoTransferencia(objDto.getIdUnicoBancoTransferencia());
        transferencia.setValorTransferencia(objDto.getValorTransferencia());

        Conta conta = contaRepository.findByAgenciaAndConta(objDto.getAgenciaDestino(), objDto.getContaDestino());

        // Conta não existe no nosso banco
        if( conta == null){
            logger.info("Conta não encontrada no nosso banco: {"
                    +objDto.getAgenciaDestino()
                    + ", "
                    + objDto.getContaDestino()
                    + "}.");
            return null;
        }

        transferencia.setContaDestino(conta);
        return transferencia;
    }

    @Async
    @Transactional
    public void processaTransferencias(List<TransferenciaDTO> listObjDto) {
        for(TransferenciaDTO x: listObjDto){
            Transferencia transferencia = fromDTO(x);
            if(transferencia != null) {
                try {
                    insert(transferencia);
                    contaService.atualizaSaldo(transferencia.getContaDestino(), transferencia.getValorTransferencia());
                } catch (Exception ex){
                    logger.info("Transferência já registrada, error: " + ex.getMessage());
                }
            }
        }
    }
}
