package com.jocivaldias.nossobancodigital.resources;

import com.jocivaldias.nossobancodigital.domain.Address;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.dto.NewAddressDTO;
import com.jocivaldias.nossobancodigital.dto.ProposalConfirmationDTO;
import com.jocivaldias.nossobancodigital.dto.ProposalDTO;
import com.jocivaldias.nossobancodigital.dto.NewProposalDTO;
import com.jocivaldias.nossobancodigital.services.*;
import com.jocivaldias.nossobancodigital.services.exception.RegistrationStepException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/proposals")
public class ProposalResource {

    private final ProposalService proposalService;

    private final AddressService addressService;

    private final StorageService storageService;

    private final AccountService accountService;

    private final EmailService emailService;

    @Autowired
    public ProposalResource(ProposalService proposalService, AddressService addressService,
                            StorageService storageService, AccountService accountService, EmailService emailService) {
        this.proposalService = proposalService;
        this.addressService = addressService;
        this.storageService = storageService;
        this.accountService = accountService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Returns the proposal by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Proposal returned successfully"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Proposal> findProposal(@PathVariable Integer id) {
        Proposal obj = proposalService.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Creates a proposal with basic customer data")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Proposal successfully created"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createProposal(@Valid @RequestBody NewProposalDTO newProposalDTO) {
        Proposal proposal = proposalService.fromDTO(newProposalDTO);
        proposal.setStatus(ProposalStatus.OPENED);
        proposal = proposalService.insert(proposal);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/proposals/{id}/address")
                .buildAndExpand(proposal.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Edit the basic data of a proposal's Client")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Proposal successfully modified"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> editProposal(@PathVariable Integer id, @Valid @RequestBody ProposalDTO proposalDTO) {
        Proposal proposal = proposalService.fromDTO(proposalDTO);
        proposal.setId(id);
        proposalService.update(proposal);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Insert Client Address in a proposal")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Address added to Proposal"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/address", method = RequestMethod.POST)
    public ResponseEntity<Void> insertProposalClientAddress(@PathVariable Integer id, @Valid @RequestBody NewAddressDTO newAddressDTO) {
        Address address = addressService.fromDTO(newAddressDTO);
        Proposal proposal = proposalService.find(id);
        proposal.getClient().setAddress(address);
        address.setClient(proposal.getClient());

        addressService.insert(address);

        proposalService.updateStatus(proposal, ProposalStatus.PENDING_CLIENT_DOCUMENTATION);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/proposals/{id}/document")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Changing the Client Address in a proposal")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Address changed"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/address", method = RequestMethod.PUT)
    public ResponseEntity<Void> editProposalClientAddress(@PathVariable Integer id, @Valid @RequestBody NewAddressDTO newAddressDTO) {
        Address address = addressService.fromDTO(newAddressDTO);
        Proposal proposal = proposalService.find(id);
        address.setId(proposal.getClient().getAddress().getId());

        addressService.update(address);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Insertion of the front of the CPF (file) in the Proposal")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Document inserted in the Proposal"),
            @ApiResponse(code = 400, message = "Error in file validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 422, message = "Violation of registration step"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/document", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadClientDocument(@PathVariable Integer id, @RequestParam MultipartFile file) {
        Proposal proposal = proposalService.find(id);

        if (proposal.getStatus().getCod() < ProposalStatus.PENDING_CLIENT_DOCUMENTATION.getCod()) {
            throw new RegistrationStepException("Step Violation. Proposal status: " + ProposalStatus.toEnum(proposal.getStatus().getCod()));
        }

        proposalService.uploadDocument(proposal, file);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/proposals/{id}/confirm")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Download the file inserted in the Proposal")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Document returned in the request body"),
            @ApiResponse(code = 400, message = "File system error"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/document", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        Proposal proposal = proposalService.find(id);

        Resource file = storageService.loadAsResource(proposal.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ApiOperation(value = "Client confirmation for account creation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registered customer confirmation"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 422, message = "Violation in the registration step"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/confirm", method = RequestMethod.POST)
    public ResponseEntity<Void> confirmProposal(@PathVariable Integer id, @Valid @RequestBody ProposalConfirmationDTO confirmationDTO) {
        Proposal proposal = proposalService.find(id);

        if (proposal.getStatus().getCod() != ProposalStatus.PENDING_CLIENT_CONFIRMATION.getCod()) {
            throw new RegistrationStepException("Step Violation. Proposal status: " + ProposalStatus.toEnum(proposal.getStatus().getCod()));
        }

        proposalService.updateStatus(proposal, ProposalStatus.PENDING_SYSTEM_ACCEPTANCE);

        if (confirmationDTO.getConfirmation()) { // Customer confirmed the proposal
            accountService.requestProposalApproval(proposal);
        } else { // Client denied the proposal
            emailService.insistCustomerConfirmation(proposal.getClient());
        }

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Confirmation of the Validation API for account creation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registered API confirmation"),
            @ApiResponse(code = 400, message = "Error in data validation"),
            @ApiResponse(code = 404, message = "Proposal not found"),
            @ApiResponse(code = 422, message = "Violation in the registration step"),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/{id}/confirm-api", method = RequestMethod.POST)
    public ResponseEntity<Void> confirmProposalApi(@PathVariable Integer id, @RequestBody ProposalConfirmationDTO proposalConfirmationDTO) {
        Proposal proposal = proposalService.find(id);

        if (proposal.getStatus().getCod() != ProposalStatus.PENDING_SYSTEM_ACCEPTANCE.getCod()) {
            throw new RegistrationStepException("Step Violation. Proposal status: " + ProposalStatus.toEnum(proposal.getStatus().getCod()));
        }

        if(proposalConfirmationDTO.getConfirmation()) { // API accepted documentation
            accountService.createAccount(proposal);
            emailService.welcomeNewClient(proposal.getClient());
            proposalService.updateStatus(proposal, ProposalStatus.IMPLEMENTED);
        } else { // API denied documentation
            proposalService.updateStatus(proposal, ProposalStatus.CANCELED);
        }
        proposalService.closeProposal(proposal);

        return ResponseEntity.ok().build();
    }
}
