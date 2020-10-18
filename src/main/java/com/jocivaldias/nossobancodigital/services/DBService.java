package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class DBService {

    private final PropostaRepository propostaRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DBService(PropostaRepository propostaRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.propostaRepository = propostaRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void instantiateTestDatabase(){
        //Proposta com Cliente porém sem Endereço
        Proposta p1 = new Proposta(null);
        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");

        //Proposta com Cliente e Endereço porém sem Documento
        Proposta p2 = new Proposta(null);
        Cliente cli2 = new Cliente(null, "Exemplo", "De Cliente 002", "exemplo002@email.com", LocalDate.of(2001,01,01), "90619206047");
        Endereco e2 = new Endereco(null, "38500000", "Rua Exemplo 002", "Bairo exemplo 002", "complemento 002", "Cidade 002", "Estado 002");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Cliente
        Proposta p3 = new Proposta(null);
        Cliente cli3 = new Cliente(null, "Exemplo", "De Cliente 002", "exemplo003@email.com", LocalDate.of(2001,01,01), "56451738050");
        Endereco e3 = new Endereco(null, "38500000", "Rua Exemplo 003", "Bairo exemplo 003", "complemento 003", "Cidade 003", "Estado 003");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Sistema
        Proposta p4 = new Proposta(null);
        Cliente cli4 = new Cliente(null, "Exemplo", "De Cliente 00$", "exemplo004@email.com", LocalDate.of(2001,01,01), "72179695063");
        Endereco e4 = new Endereco(null, "38500000", "Rua Exemplo 004", "Bairo exemplo 004", "complemento 004", "Cidade 004", "Estado 004");

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposta p5 = new Proposta(null);
        Cliente cli5 = new Cliente(null, "Exemplo", "De Cliente 00$", "exemplo005@email.com", LocalDate.of(2001,01,01), "26901012039");
        Endereco e5 = new Endereco(null, "38500000", "Rua Exemplo 005", "Bairo exemplo 005", "complemento 005", "Cidade 005", "Estado 005");
        Conta con5 = new Conta(null, "0000", "00000000", "000", p5, 0.00, bCryptPasswordEncoder.encode("senha"));

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposta p6 = new Proposta(null);
        Cliente cli6 = new Cliente(null, "Exemplo", "De Cliente 00$", "exemplo006@email.com", LocalDate.of(2001,01,01), "35981980001");
        Endereco e6 = new Endereco(null, "38500000", "Rua Exemplo 006", "Bairo exemplo 006", "complemento 006", "Cidade 006", "Estado 006");
        Conta con6 = new Conta(null, "0001", "00000001", "000", p6, 0.00, bCryptPasswordEncoder.encode("senha"));

        p1.setCliente(cli1);
        p2.setCliente(cli2);
        p3.setCliente(cli3);
        p4.setCliente(cli4);
        p5.setCliente(cli5);
        p6.setCliente(cli6);

        cli1.setProposta(p1);
        cli2.setProposta(p2);
        cli3.setProposta(p3);
        cli4.setProposta(p4);
        cli5.setProposta(p5);
        cli6.setProposta(p6);

        cli2.setEndereco(e2);
        cli3.setEndereco(e3);
        cli4.setEndereco(e4);
        cli5.setEndereco(e5);
        cli6.setEndereco(e6);

        e2.setCliente(cli2);
        e3.setCliente(cli3);
        e4.setCliente(cli4);
        e5.setCliente(cli5);
        e6.setCliente(cli6);

        p3.setFilename("arquivo.pdf");
        p4.setFilename("teste.pdf");
        p5.setFilename("outro.pdf");
        p6.setFilename("final.pdf");

        p5.setConta(con5);
        p6.setConta(con6);
        con5.setProposta(p5);
        con6.setProposta(p6);


        p1.setStatus(StatusProposta.PENDENTE_ENDERECO_CLIENTE);
        p2.setStatus(StatusProposta.PENDENTE_DOCUMENTACAO_CLIENTE);
        p3.setStatus(StatusProposta.PENDENTE_CONFIRMACAO_CLIENTE);
        p4.setStatus(StatusProposta.PENDENTE_LIBERACAO_SISTEMA);
        p5.setStatus(StatusProposta.LIBERADA);
        p6.setStatus(StatusProposta.LIBERADA);

        propostaRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6));
    }
}
