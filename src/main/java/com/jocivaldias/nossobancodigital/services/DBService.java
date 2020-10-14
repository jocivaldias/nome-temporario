package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void instantiateTestDatabase() throws ParseException {
        Proposta p1 = new Proposta(null);
        Proposta p2 = new Proposta(null);

        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"), "50451229088");
        Cliente cli2 = new Cliente(null, "Exemplo", "De Cliente 002", "exemplo002@email.com", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"), "90619206047");

        p1.setCliente(cli1);
        p2.setCliente(cli2);
        cli1.setProposta(p1);
        cli2.setProposta(p2);

        propostaRepository.saveAll(Arrays.asList(p1, p2));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2));
    }
}
