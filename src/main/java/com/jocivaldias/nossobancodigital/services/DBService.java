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

import java.math.BigDecimal;
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
        Proposal proposal1 = new Proposal(null);
        Client client1 = new Client(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");

        //Proposta com Cliente e Endereço porém sem Documento
        Proposal proposal2 = new Proposal(null);
        Client client2 = new Client(null, "Exemplo", "De Cliente 002", "exemplo002@email.com", LocalDate.of(2001,01,01), "90619206047");
        Address address2 = new Address(null, "38500000", "Rua Exemplo 002", "Bairo exemplo 002", "complemento 002", "Cidade 002", "Estado 002");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Cliente
        Proposal proposal3 = new Proposal(null);
        Client client3 = new Client(null, "Exemplo", "De Cliente 002", "exemplo003@email.com", LocalDate.of(2001,01,01), "56451738050");
        Address address3 = new Address(null, "38500000", "Rua Exemplo 003", "Bairo exemplo 003", "complemento 003", "Cidade 003", "Estado 003");

        //Proposta com Cliente, Endereço e Documento aguardando Aprovação Sistema
        Proposal proposal4 = new Proposal(null);
        Client client4 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo004@email.com", LocalDate.of(2001,01,01), "72179695063");
        Address address4 = new Address(null, "38500000", "Rua Exemplo 004", "Bairo exemplo 004", "complemento 004", "Cidade 004", "Estado 004");

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposal proposal5 = new Proposal(null);
        Client client5 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo005@email.com", LocalDate.of(2001,01,01), "26901012039");
        Address address5 = new Address(null, "38500000", "Rua Exemplo 005", "Bairo exemplo 005", "complemento 005", "Cidade 005", "Estado 005");
        Account account5 = new Account(null, "0000", "00000000", "000", proposal5, BigDecimal.ZERO, bCryptPasswordEncoder.encode("senha"));

        //Proposta com Cliente, Endereço, Documento e Conta
        Proposal proposal6 = new Proposal(null);
        Client client6 = new Client(null, "Exemplo", "De Cliente 00$", "exemplo006@email.com", LocalDate.of(2001,01,01), "35981980001");
        Address address6 = new Address(null, "38500000", "Rua Exemplo 006", "Bairo exemplo 006", "complemento 006", "Cidade 006", "Estado 006");
        Account account6 = new Account(null, "0001", "00000001", "000", proposal6, BigDecimal.ZERO, bCryptPasswordEncoder.encode("senha"));

        proposal1.setClient(client1);
        proposal2.setClient(client2);
        proposal3.setClient(client3);
        proposal4.setClient(client4);
        proposal5.setClient(client5);
        proposal6.setClient(client6);

        client1.setProposal(proposal1);
        client2.setProposal(proposal2);
        client3.setProposal(proposal3);
        client4.setProposal(proposal4);
        client5.setProposal(proposal5);
        client6.setProposal(proposal6);

        client2.setAddress(address2);
        client3.setAddress(address3);
        client4.setAddress(address4);
        client5.setAddress(address5);
        client6.setAddress(address6);

        address2.setClient(client2);
        address3.setClient(client3);
        address4.setClient(client4);
        address5.setClient(client5);
        address6.setClient(client6);

        proposal3.setFilename("arquivo.pdf");
        proposal4.setFilename("teste.pdf");
        proposal5.setFilename("outro.pdf");
        proposal6.setFilename("final.pdf");

        proposal5.setAccount(account5);
        proposal6.setAccount(account6);
        account5.setProposal(proposal5);
        account6.setProposal(proposal6);


        proposal1.setStatus(ProposalStatus.PENDING_CLIENT_ADDRESS);
        proposal2.setStatus(ProposalStatus.PENDING_CLIENT_DOCUMENTATION);
        proposal3.setStatus(ProposalStatus.PENDING_CLIENT_CONFIRMATION);
        proposal4.setStatus(ProposalStatus.PENDING_SYSTEM_ACCEPTANCE);
        proposal5.setStatus(ProposalStatus.IMPLEMENTED);
        proposal6.setStatus(ProposalStatus.IMPLEMENTED);

        proposalRepository.saveAll(Arrays.asList(proposal1, proposal2, proposal3, proposal4, proposal5, proposal6));
    }
}
