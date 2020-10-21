package com.jocivaldias.nossobancodigital.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jocivaldias.nossobancodigital.dto.TransferDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TransferResourceTest {

    @Autowired
    private MockMvc mvc;

    // write test cases here
    @Test
    public void givenValidTransfersList_whenPostTransfers_thenStatus200()
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        List<TransferDTO> lista = new ArrayList<>();

        for(int i=0; i <10; i++){
            TransferDTO aux = new TransferDTO();
            aux.setOriginBankNumber("999");
            aux.setOriginAccountNumber("12345678");
            aux.setTargetAccountNumber("00000000");
            aux.setOriginBranchNumber("1234");
            aux.setTargetBranchNumber("0000");
            aux.setTransferDate(LocalDate.now());
            aux.setIdentifierDocument("12345678911");
            aux.setTransferIdOriginBank((long) i);
            aux.setTransferValue(new BigDecimal(10.00));
            lista.add(aux);
        }

        String requestJson = ow.writeValueAsString(lista);

        mvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}