package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.Address;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.repositories.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class DBService {

    private final ProposalRepository proposalRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DBService(ProposalRepository proposalRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.proposalRepository = proposalRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void instantiateTestDatabase(){

        //Proposta com Cliente porém sem Endereço
        Proposal p1 = new Proposal(null);
        Client cli1 = new Client(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");

        //Proposta com Cliente e Endereço porém sem Documento
        Proposal p2 = new Proposal(null);
        Client cli2 = new Client(null, "Exemplo", "De Cliente 002", "exemplo002@email.com", LocalDate.of(2001,01,01), "90619206047");
        Address e2 = new Address(null, "38500000", "Rua Exemplo 002", "Bairo exemplo 002", "complemento 002", "Cidade 002", "Estado 002");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Cliente
        Proposal p3 = new Proposal(null);
        Client cli3 = new Client(null, "Exemplo", "De Cliente 002", "exemplo003@email.com", LocalDate.of(2001,01,01), "56451738050");
        Address e3 = new Address(null, "38500000", "Rua Exemplo 003", "Bairo exemplo 003", "complemento 003", "Cidade 003", "Estado 003");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Sistema
        Proposal p4 = new Proposal(null);
        Client cli4 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo004@email.com", LocalDate.of(2001,01,01), "72179695063");
        Address e4 = new Address(null, "38500000", "Rua Exemplo 004", "Bairo exemplo 004", "complemento 004", "Cidade 004", "Estado 004");

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposal p5 = new Proposal(null);
        Client cli5 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo005@email.com", LocalDate.of(2001,01,01), "26901012039");
        Address e5 = new Address(null, "38500000", "Rua Exemplo 005", "Bairo exemplo 005", "complemento 005", "Cidade 005", "Estado 005");
        Account con5 = new Account(null, "0000", "00000000", "000", p5, 0.00, bCryptPasswordEncoder.encode("senha"));

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposal p6 = new Proposal(null);
        Client cli6 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo006@email.com", LocalDate.of(2001,01,01), "35981980001");
        Address e6 = new Address(null, "38500000", "Rua Exemplo 006", "Bairo exemplo 006", "complemento 006", "Cidade 006", "Estado 006");
        Account con6 = new Account(null, "0001", "00000001", "000", p6, 0.00, bCryptPasswordEncoder.encode("senha"));

        p1.setClient(cli1);
        p2.setClient(cli2);
        p3.setClient(cli3);
        p4.setClient(cli4);
        p5.setClient(cli5);
        p6.setClient(cli6);

        cli1.setProposal(p1);
        cli2.setProposal(p2);
        cli3.setProposal(p3);
        cli4.setProposal(p4);
        cli5.setProposal(p5);
        cli6.setProposal(p6);

        cli2.setAddress(e2);
        cli3.setAddress(e3);
        cli4.setAddress(e4);
        cli5.setAddress(e5);
        cli6.setAddress(e6);

        e2.setClient(cli2);
        e3.setClient(cli3);
        e4.setClient(cli4);
        e5.setClient(cli5);
        e6.setClient(cli6);

        p3.setFilename("arquivo.pdf");
        p4.setFilename("teste.pdf");
        p5.setFilename("outro.pdf");
        p6.setFilename("final.pdf");

        p5.setAccount(con5);
        p6.setAccount(con6);
        con5.setProposal(p5);
        con6.setProposal(p6);


        p1.setStatus(ProposalStatus.PENDING_CLIENT_ADDRESS);
        p2.setStatus(ProposalStatus.PENDING_CLIENT_DOCUMENTATION);
        p3.setStatus(ProposalStatus.PENDING_CLIENT_CONFIRMATION);
        p4.setStatus(ProposalStatus.PENDING_SYSTEM_ACCEPTANCE);
        p5.setStatus(ProposalStatus.IMPLEMENTED);
        p6.setStatus(ProposalStatus.IMPLEMENTED);

        proposalRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6));
    }
}
