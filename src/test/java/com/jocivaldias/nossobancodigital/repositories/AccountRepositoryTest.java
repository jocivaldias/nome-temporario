package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Address;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    // write test cases here
    @Test
    public void whenFindByProposalClientEmailAndProposalClientCpf_thenReturnAccount() {
        // given
        Proposal p1 = new Proposal(null);
        Client cli1 = new Client(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");
        Address e1 = new Address(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Account account = new Account(null, "0000", "00000000", "341", p1, 0.00, "testesenha");
        p1.setClient(cli1);
        cli1.setProposal(p1);
        cli1.setAddress(e1);
        e1.setClient(cli1);
        p1.setStatus(ProposalStatus.IMPLEMENTED);
        p1.setAccount(account);
        entityManager.persist(p1);
        entityManager.persist(cli1);
        entityManager.persist(e1);

        // when
        Account found = accountRepository.findByProposalClientEmailAndProposalClientCpf(p1.getClient().getEmail(),
                p1.getClient().getCpf());

        // then
        assertThat(found.getBranchNumber())
                .isEqualTo(account.getBranchNumber());
        assertThat(found.getAccountNumber())
                .isEqualTo(account.getAccountNumber());
        assertThat(found.getBankNumber())
                .isEqualTo(account.getBankNumber());
    }

    @Test
    public void whenFindByBranchNumberAndAccountNumber_thenReturnAccount() {
        // given
        Proposal p1 = new Proposal(null);
        Client cli1 = new Client(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");
        Address e1 = new Address(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Account account = new Account(null, "0000", "00000000", "341", p1, 0.00, "testesenha");
        p1.setClient(cli1);
        cli1.setProposal(p1);
        cli1.setAddress(e1);
        e1.setClient(cli1);
        p1.setStatus(ProposalStatus.IMPLEMENTED);
        p1.setAccount(account);
        entityManager.persist(p1);
        entityManager.persist(cli1);
        entityManager.persist(e1);

        // when
        Account found = accountRepository.findByBranchNumberAndAccountNumber(account.getBranchNumber(), account.getAccountNumber());

        // then
        assertThat(found.getBranchNumber())
                .isEqualTo(account.getBranchNumber());
        assertThat(found.getAccountNumber())
                .isEqualTo(account.getAccountNumber());
        assertThat(found.getBankNumber())
                .isEqualTo(account.getBankNumber());
    }

}
