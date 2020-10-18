package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.dto.PropostaDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class PropostaService {

    private final PropostaRepository repo;
    private final StorageService storageService;

    @Value("${document.prefix}")
    private String prefix;

    @Autowired
    public PropostaService(PropostaRepository repo, StorageService storageService) {
        this.repo = repo;
        this.storageService = storageService;
    }

    public Proposta find(Integer id){
        Optional<Proposta> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Proposta não encontrada! Id: " + id + ", Tipo: " + Proposta.class.getName()
        ));
    }

    public Proposta insert(Proposta obj){
        obj.setId(null);
        return repo.save(obj);
    }

    public Proposta update(Proposta obj) {
        Proposta newObj = find(obj.getId());
        updateDadosBasicosCliente(newObj, obj);
        return repo.save(newObj);
    }

    public void updateStatus(Proposta proposta, StatusProposta status) {
        Proposta newObj = find(proposta.getId());
        newObj.setStatus(status);
        repo.save(newObj);
    }

    public void uploadDocumento(Proposta proposta, MultipartFile file) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = prefix + proposta.getId() + "." + ext;

        storageService.store(file, fileName);

        if(proposta.getStatus().getCod() == StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE.getCod()) {
            proposta.setStatus(StatusProposta.PENDENTE_CONFIRMACAO_CLIENTE);
        }
        proposta.setFilename(fileName);
        repo.save(proposta);
    }

    public Proposta finalizaProposta(Proposta proposta) {
        Proposta newObj = find(proposta.getId());
        newObj.setDataFechamento(LocalDate.now());
        return repo.save(newObj);
    }

    public Proposta fromDTO(PropostaNewDTO objDto) {
        Proposta proposta = new Proposta(null);

        Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getSobrenome(), objDto.getEmail(),
                objDto.getDataNascimento(), objDto.getCpf());

        proposta.setCliente(cliente);
        cliente.setProposta(proposta);

        return proposta;
    }

    public Proposta fromDTO(PropostaDTO objDto) {
        Proposta proposta = new Proposta(null);

        Cliente cliente = new Cliente();
        cliente.setNome(objDto.getNome());
        cliente.setSobrenome(objDto.getSobrenome());
        cliente.setEmail(objDto.getEmail());
        cliente.setDataNascimento(objDto.getDataNascimento());

        cliente.setProposta(proposta);
        proposta.setCliente(cliente);

        return proposta;
    }

    private void updateDadosBasicosCliente(Proposta newObj, Proposta obj) {
        newObj.getCliente().setNome(obj.getCliente().getNome());
        newObj.getCliente().setSobrenome(obj.getCliente().getSobrenome());
        newObj.getCliente().setEmail(obj.getCliente().getEmail());
        newObj.getCliente().setDataNascimento(obj.getCliente().getDataNascimento());
    }

}
