package com.jocivaldias.nossobancodigital.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jocivaldias.nossobancodigital.dto.TransferenciaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransferenciaResourceTest {

    @Autowired
    private MockMvc mvc;

    // write test cases here
    @Test
    public void givenValidTransferenciaList_whenPostTransferencia_thenStatus200()
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        List<TransferenciaDTO> lista = new ArrayList<>();

        for(int i=0; i <10; i++){
            TransferenciaDTO aux = new TransferenciaDTO();
            aux.setCodigoBancoOrigem("999");
            aux.setContaOrigem("12345678");
            aux.setContaDestino("00000000");
            aux.setAgenciaOrigem("1234");
            aux.setAgenciaDestino("0000");
            aux.setDataRealizacao(LocalDate.now());
            aux.setDocumentoIdentificador("12345678911");
            aux.setIdUnicoBancoTransferencia((long) i);
            aux.setValorTransferencia(10.00);
            lista.add(aux);
        }

        String requestJson = ow.writeValueAsString(lista);

        mvc.perform(post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}