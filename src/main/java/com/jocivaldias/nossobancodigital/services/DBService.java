package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.EnderecoRepository;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

    private final PropostaRepository propostaRepository;

    private final ClienteRepository clienteRepository;

    private final EnderecoRepository enderecoRepository;

    @Autowired
    public DBService(PropostaRepository propostaRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.propostaRepository = propostaRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public void instantiateTestDatabase() throws ParseException {
        Proposta p1 = new Proposta(null);
        Proposta p2 = new Proposta(null);

        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"), "50451229088");
        Cliente cli2 = new Cliente(null, "Exemplo", "De Cliente 002", "exemplo002@email.com", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2001"), "90619206047");

        Endereco e1 = new Endereco(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Endereco e2 = new Endereco(null, "38500000", "Rua Exemplo 002", "Bairo exemplo 002", "complemento 002", "Cidade 002", "Estado 002");

        p1.setCliente(cli1);
        p2.setCliente(cli2);
        cli1.setProposta(p1);
        cli2.setProposta(p2);
        cli1.setEndereco(e1);
        cli2.setEndereco(e2);
        e1.setCliente(cli1);
        e2.setCliente(cli2);

        p1.setStatus(StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);
        p2.setStatus(StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);

        propostaRepository.saveAll(Arrays.asList(p1, p2));
        clienteRepository.saveAll(Arrays.asList(cli1, cli2));
        enderecoRepository.saveAll(Arrays.asList(e1, e2));
    }
}
