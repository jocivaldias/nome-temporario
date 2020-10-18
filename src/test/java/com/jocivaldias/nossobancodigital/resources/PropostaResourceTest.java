package com.jocivaldias.nossobancodigital.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.dto.EnderecoNewDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaConfirmacaoDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import com.jocivaldias.nossobancodigital.services.PropostaService;
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
public class PropostaResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropostaService propostaService;

    // write test cases here
    @Test
    public void givenValidPropostaNewDTO_whenPostProposta_thenStatus201()
            throws Exception {

        PropostaNewDTO propostaNewDTO = new PropostaNewDTO();
        propostaNewDTO.setCpf("41949564002");
        propostaNewDTO.setDataNascimento(LocalDate.of(2000,01,01));
        propostaNewDTO.setEmail("test@email.com");
        propostaNewDTO.setNome("Nome");
        propostaNewDTO.setSobrenome("sobrenome");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(propostaNewDTO);

        mvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/propostas/*/endereco"))
                .andReturn();
    }

    @Test
    public void givenInvalidPropostaNewDTO_whenPostProposta_thenStatus400()
            throws Exception {

        PropostaNewDTO propostaNewDTO = new PropostaNewDTO();
        propostaNewDTO.setCpf("");
        propostaNewDTO.setDataNascimento(LocalDate.of(2000,01,01));
        propostaNewDTO.setEmail("test@email.com");
        propostaNewDTO.setNome("Nome");
        propostaNewDTO.setSobrenome("sobrenome");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(propostaNewDTO);

        mvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidEnderecoNewDTO_whenPostPropostaEndereco_thenStatus201()
            throws Exception {

        EnderecoNewDTO enderecoNewDTO = new EnderecoNewDTO();
        enderecoNewDTO.setBairro("Bairro");
        enderecoNewDTO.setCep("38400000");
        enderecoNewDTO.setCidade("Uberlândia");
        enderecoNewDTO.setComplemento("Complemento");
        enderecoNewDTO.setRua("Rua");
        enderecoNewDTO.setEstado("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(enderecoNewDTO);

        mvc.perform(post("/propostas/1/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/propostas/*/documento"))
                .andReturn();
    }

    @Test
    public void givenInvalidEnderecoNewDTO_whenPostPropostaEndereco_thenStatus400()
            throws Exception {

        EnderecoNewDTO enderecoNewDTO = new EnderecoNewDTO();
        enderecoNewDTO.setBairro("Bairro");
        enderecoNewDTO.setCep("123");
        enderecoNewDTO.setCidade("Uberlândia");
        enderecoNewDTO.setComplemento("Complemento");
        enderecoNewDTO.setRua("Rua");
        enderecoNewDTO.setEstado("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(enderecoNewDTO);

        mvc.perform(post("/propostas/1/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidEnderecoNewDTO_whenPropostaHasEndereco_whenPostPropostaEndereco_thenStatus400()
            throws Exception {

        EnderecoNewDTO enderecoNewDTO = new EnderecoNewDTO();
        enderecoNewDTO.setBairro("Bairro");
        enderecoNewDTO.setCep("123");
        enderecoNewDTO.setCidade("Uberlândia");
        enderecoNewDTO.setComplemento("Complemento");
        enderecoNewDTO.setRua("Rua");
        enderecoNewDTO.setEstado("Estado");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(enderecoNewDTO);

        mvc.perform(post("/propostas/2/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidDocumento_whenPostPropostaDocumento_thenStatus201()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        mvc.perform(multipart("/propostas/2/documento")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/propostas/2/confirmacao"))
                .andReturn();
    }

    @Test
    public void givenInvalidDocumento_whenPostPropostaDocumento_thenStatus400()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "exemplo".getBytes());

        mvc.perform(multipart("/propostas/2/documento")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenValidDocumento_givenInvalidProposta_whenPostPropostaDocumento_thenStatus404()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        mvc.perform(multipart("/propostas/9999/documento")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenValidDocumento_givenValidProposta_givenInvalidStep_whenPostPropostaDocumento_thenStatus422()
            throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "exemplo".getBytes());

        mvc.perform(multipart("/propostas/1/documento")
                .file(file)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    public void givenValidPropostaConfirmacaoDTO_whenPostPropostaConfirmacao_thenStatus200()
            throws Exception {

        PropostaConfirmacaoDTO propostaConfirmacaoDTO = new PropostaConfirmacaoDTO();
        propostaConfirmacaoDTO.setConfirmado(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(propostaConfirmacaoDTO);

        mvc.perform(post("/propostas/3/confirmacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenValidProposta_whenGetProposta_thenStatus200AndReturnProposta()
            throws Exception {

        Proposta proposta = propostaService.find(3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(proposta);

        mvc.perform(get("/propostas/3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(requestJson))
                .andReturn();
    }

    @Test
    public void givenValidPropostaConfirmacaoDTO_whenPostPropostaConfirmacaoApi_thenStatus200()
            throws Exception {

        PropostaConfirmacaoDTO propostaConfirmacaoDTO = new PropostaConfirmacaoDTO();
        propostaConfirmacaoDTO.setConfirmado(true);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(propostaConfirmacaoDTO);

        mvc.perform(post("/propostas/4/confirmacao-api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}