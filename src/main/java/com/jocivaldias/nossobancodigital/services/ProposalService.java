package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.dto.ProposalDTO;
import com.jocivaldias.nossobancodigital.dto.NewProposalDTO;
import com.jocivaldias.nossobancodigital.repositories.ProposalRepository;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProposalService {

    private final ProposalRepository repo;
    private final StorageService storageService;

    @Value("${document.prefix}")
    private String prefix;

    @Autowired
    public ProposalService(ProposalRepository repo, StorageService storageService) {
        this.repo = repo;
        this.storageService = storageService;
    }

    public Proposal find(Integer id){
        Optional<Proposal> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Proposal not found! Id: " + id + ", Type: " + Proposal.class.getName()
        ));
    }

    public Proposal insert(Proposal obj){
        obj.setId(null);
        return repo.save(obj);
    }

    public Proposal update(Proposal obj) {
        Proposal newObj = find(obj.getId());
        updateClientData(newObj, obj);
        return repo.save(newObj);
    }

    public void updateStatus(Proposal proposal, ProposalStatus status) {
        Proposal newObj = find(proposal.getId());
        newObj.setStatus(status);
        repo.save(newObj);
    }

    public void uploadDocument(Proposal proposal, MultipartFile file) {
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = prefix + proposal.getId() + "." + ext;

        storageService.store(file, fileName);

        if(proposal.getStatus().getCod() == ProposalStatus.PENDING_CLIENT_DOCUMENTATION.getCod()) {
            proposal.setStatus(ProposalStatus.PENDING_CLIENT_CONFIRMATION);
        }
        proposal.setFilename(fileName);
        repo.save(proposal);
    }

    public Proposal closeProposal(Proposal proposal) {
        Proposal newObj = find(proposal.getId());
        newObj.setProposalClosingDate(LocalDate.now());
        return repo.save(newObj);
    }

    public Proposal fromDTO(NewProposalDTO objDto) {
        Proposal proposal = new Proposal(null);

        Client client = new Client(null, objDto.getName(), objDto.getLastName(), objDto.getEmail(),
                objDto.getBirthdate(), objDto.getCpf());

        proposal.setClient(client);
        client.setProposal(proposal);

        return proposal;
    }

    public Proposal fromDTO(ProposalDTO objDto) {
        Proposal proposal = new Proposal(null);

        Client client = new Client();
        client.setName(objDto.getName());
        client.setLastName(objDto.getLastName());
        client.setEmail(objDto.getEmail());
        client.setBirthdate(objDto.getBirthdate());

        client.setProposal(proposal);
        proposal.setClient(client);

        return proposal;
    }

    private void updateClientData(Proposal newObj, Proposal obj) {
        newObj.getClient().setName(obj.getClient().getName());
        newObj.getClient().setLastName(obj.getClient().getLastName());
        newObj.getClient().setEmail(obj.getClient().getEmail());
        newObj.getClient().setBirthdate(obj.getClient().getBirthdate());
    }

}
