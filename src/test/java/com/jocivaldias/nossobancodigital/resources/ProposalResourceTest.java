package com.jocivaldias.nossobancodigital.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.dto.newAddressDTO;
import com.jocivaldias.nossobancodigital.dto.ProposalConfirmationDTO;
import com.jocivaldias.nossobancodigital.dto.NewProposalDTO;
import com.jocivaldias.nossobancodigital.services.DBService;
import com.jocivaldias.nossobancodigital.services.ProposalService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProposalResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private DBService dbService;

    // write test cases here
    @Test
    public void givenValidPropostaNewDTO_whenPostProposta_thenStatus201()
            throws Exception {

        NewProposalDTO newProposalDTO = new NewProposalDTO();
        newProposalDTO.setCpf("41949564002");
        newProposalDTO.setBirthdate(LocalDate.of(2000,01,01));
        newProposalDTO.setEmail("test@email.com");
        newProposalDTO.setName("Nome");
        newProposalDTO.setLastName("sobrenome");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(newProposalDTO);

        mvc.perform(post("/proposals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/proposals/*/address"))
                .andReturn();
    }

    @Test
    public void givenInvalidNewProposalDTO_whenPostProposal_thenStatus400()
            throws Exception {

        NewProposalDTO newProposalDTO = new NewProposalDTO();
        newProposalDTO.setCpf("");
        newProposalDTO.setBirthdate(LocalDate.of(2000,01,01));
        newProposalDTO.setEmail("test@email.com");
        newProposalDTO.setName("Nome");
        newProposalDTO.setLastName("sobrenome");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(newProposalDTO);

        mvc.perform(post("/proposals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidAddressNewDTO_whenPostProposalAddress_thenStatus201()
            throws Exception {

        newAddressDTO newAddressDTO = new newAddressDTO();
        newAddressDTO.setNeighborhoodName("Bairro");
        newAddressDTO.setZipCode("38400000");
        newAddressDTO.setCity("Uberlândia");
        newAddressDTO.setComplement("Complemento");
        newAddressDTO.setStreetName("Rua");
        newAddressDTO.setState("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(newAddressDTO);

        mvc.perform(post("/proposals/1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/proposals/*/document"))
                .andReturn();
    }

    @Test
    public void givenInvalidAddressNewDTO_whenPostProposalAddress_thenStatus400()
            throws Exception {

        newAddressDTO newAddressDTO = new newAddressDTO();
        newAddressDTO.setNeighborhoodName("Bairro");
        newAddressDTO.setZipCode("123");
        newAddressDTO.setCity("Uberlândia");
        newAddressDTO.setComplement("Complemento");
        newAddressDTO.setStreetName("Rua");
        newAddressDTO.setState("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(newAddressDTO);

        mvc.perform(post("/proposals/1/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidAddressNewDTO_whenProposalHasAddress_whenPostProposalAddress_thenStatus400()
            throws Exception {

        newAddressDTO newAddressDTO = new newAddressDTO();
        newAddressDTO.setNeighborhoodName("Bairro");
        newAddressDTO.setZipCode("123");
        newAddressDTO.setCity("Uberlândia");
        newAddressDTO.setComplement("Complemento");
        newAddressDTO.setStreetName("Rua");
        newAddressDTO.setState("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(newAddressDTO);

        mvc.perform(post("/proposals/2/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidDocument_whenPostProposalDocument_thenStatus201()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        mvc.perform(multipart("/proposals/2/document")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/proposals/2/confirm"))
                .andReturn();
    }

    @Test
    public void givenInvalidDocument_whenPostProposalDocument_thenStatus400()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "exemplo".getBytes());

        mvc.perform(multipart("/proposals/2/document")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidDocument_givenInvalidProposal_whenPostProposalDocument_thenStatus404()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        mvc.perform(multipart("/proposals/9999/document")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenValidDocument_givenValidProposal_givenInvalidStep_whenPostProposalDocument_thenStatus422()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        Proposal proposal = proposalService.find(1);

        proposalService.updateStatus(proposal, ProposalStatus.PENDING_CLIENT_ADDRESS);

        mvc.perform(multipart("/proposals/1/document")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    public void givenValidProposalConfirmationDTO_whenPostProposalConfirm_thenStatus200()
            throws Exception {

        ProposalConfirmationDTO proposalConfirmationDTO = new ProposalConfirmationDTO();
        proposalConfirmationDTO.setConfirmation(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(proposalConfirmationDTO);

        mvc.perform(post("/proposals/3/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenValidProposal_whenGetProposal_thenStatus200AndReturnProposal()
            throws Exception {

        Proposal proposal = proposalService.find(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(proposal);

        mvc.perform(get("/proposals/3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestJson))
                .andReturn();
    }

    @Test
    public void givenValidProposalConfirmationDTO_whenPostProposalConfirmApi_thenStatus200()
            throws Exception {

        ProposalConfirmationDTO proposalConfirmationDTO = new ProposalConfirmationDTO();
        proposalConfirmationDTO.setConfirmation(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(proposalConfirmationDTO);

        mvc.perform(post("/proposals/4/confirm-api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}