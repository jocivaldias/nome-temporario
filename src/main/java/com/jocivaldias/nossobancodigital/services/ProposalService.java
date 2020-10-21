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

    private final ProposalRepository proposalRepository;
    private final StorageService storageService;

    @Value("${document.prefix}")
    private String prefix;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository, StorageService storageService) {
        this.proposalRepository = proposalRepository;
        this.storageService = storageService;
    }

    public Proposal find(Integer id){
        Optional<Proposal> proposal = proposalRepository.findById(id);
        return proposal.orElseThrow(() -> new ObjectNotFoundException(
                "Proposal not found! Id: " + id + ", Type: " + Proposal.class.getName()
        ));
    }

    public Proposal insert(Proposal proposal){
        proposal.setId(null);
        return proposalRepository.save(proposal);
    }

    public Proposal update(Proposal proposal) {
        Proposal saveProposal = find(proposal.getId());
        updateClientData(saveProposal, proposal);
        return proposalRepository.save(saveProposal);
    }

    public void updateStatus(Proposal proposal, ProposalStatus status) {
        Proposal newObj = find(proposal.getId());
        newObj.setStatus(status);
        proposalRepository.save(newObj);
    }

    public void uploadDocument(Proposal proposal, MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = prefix + proposal.getId() + "." + extension;

        storageService.store(file, fileName);

        if(proposal.getStatus().getCod() == ProposalStatus.PENDING_CLIENT_DOCUMENTATION.getCod()) {
            proposal.setStatus(ProposalStatus.PENDING_CLIENT_CONFIRMATION);
        }
        proposal.setFilename(fileName);
        proposalRepository.save(proposal);
    }

    public Proposal closeProposal(Proposal proposal) {
        Proposal saveProposal = find(proposal.getId());
        saveProposal.setProposalClosingDate(LocalDate.now());
        return proposalRepository.save(saveProposal);
    }

    public Proposal fromDTO(NewProposalDTO newProposalDTO) {
        Proposal proposal = new Proposal(null);

        Client client = new Client(null, newProposalDTO.getName(), newProposalDTO.getLastName(), newProposalDTO.getEmail(),
                newProposalDTO.getBirthdate(), newProposalDTO.getCpf());

        proposal.setClient(client);
        client.setProposal(proposal);

        return proposal;
    }

    public Proposal fromDTO(ProposalDTO proposalDTO) {
        Proposal proposal = new Proposal(null);

        Client client = new Client();
        client.setName(proposalDTO.getName());
        client.setLastName(proposalDTO.getLastName());
        client.setEmail(proposalDTO.getEmail());
        client.setBirthdate(proposalDTO.getBirthdate());

        client.setProposal(proposal);
        proposal.setClient(client);

        return proposal;
    }

    private void updateClientData(Proposal toProposal, Proposal fromProposal) {
        toProposal.getClient().setName(fromProposal.getClient().getName());
        toProposal.getClient().setLastName(fromProposal.getClient().getLastName());
        toProposal.getClient().setEmail(fromProposal.getClient().getEmail());
        toProposal.getClient().setBirthdate(fromProposal.getClient().getBirthdate());
    }

}
