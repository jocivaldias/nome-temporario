package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContaRepository contaRepository;

    // write test cases here
    @Test
    public void whenFindByPropostaClienteEmailAndPropostaClienteCpf_thenReturnConta() {
        // given
        Proposta p1 = new Proposta(null);
        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");
        Endereco e1 = new Endereco(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Conta conta = new Conta(null, "0000", "00000000", "341", p1, 0.00, "testesenha");
        p1.setCliente(cli1);
        cli1.setProposta(p1);
        cli1.setEndereco(e1);
        e1.setCliente(cli1);
        p1.setStatus(StatusProposta.LIBERADA);
        p1.setConta(conta);
        entityManager.persist(p1);
        entityManager.persist(cli1);
        entityManager.persist(e1);

        // when
        Conta found = contaRepository.findByPropostaClienteEmailAndPropostaClienteCpf(p1.getCliente().getEmail(),
                p1.getCliente().getCpf());

        // then
        assertThat(found.getAgencia())
                .isEqualTo(conta.getAgencia());
        assertThat(found.getConta())
                .isEqualTo(conta.getConta());
        assertThat(found.getCodigoBanco())
                .isEqualTo(conta.getCodigoBanco());
    }

    @Test
    public void whenFindByAgenciaAndConta_thenReturnConta() {
        // given
        Proposta p1 = new Proposta(null);
        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");
        Endereco e1 = new Endereco(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Conta conta = new Conta(null, "0000", "00000000", "341", p1, 0.00, "testesenha");
        p1.setCliente(cli1);
        cli1.setProposta(p1);
        cli1.setEndereco(e1);
        e1.setCliente(cli1);
        p1.setStatus(StatusProposta.LIBERADA);
        p1.setConta(conta);
        entityManager.persist(p1);
        entityManager.persist(cli1);
        entityManager.persist(e1);

        // when
        Conta found = contaRepository.findByAgenciaAndConta(conta.getAgencia(), conta.getConta());

        // then
        assertThat(found.getAgencia())
                .isEqualTo(conta.getAgencia());
        assertThat(found.getConta())
                .isEqualTo(conta.getConta());
        assertThat(found.getCodigoBanco())
                .isEqualTo(conta.getCodigoBanco());
    }

    @Test
    public void whenFindById_thenReturnConta() {
        // given
        Proposta p1 = new Proposta(null);
        Cliente cli1 = new Cliente(null, "Exemplo", "De Cliente 001", "exemplo001@email.com", LocalDate.of(2000,01,01), "50451229088");
        Endereco e1 = new Endereco(null, "38400000", "Rua Exemplo 001", "Bairo exemplo 001", "complemento 001", "Cidade 001", "Estado 001");
        Conta conta = new Conta(null, "0000", "00000000", "341", p1, 0.00, "testesenha");
        p1.setCliente(cli1);
        cli1.setProposta(p1);
        cli1.setEndereco(e1);
        e1.setCliente(cli1);
        p1.setStatus(StatusProposta.LIBERADA);
        p1.setConta(conta);
        entityManager.persist(p1);
        entityManager.persist(cli1);
        entityManager.flush();

        // when
        Conta found = contaRepository.findById(1).orElseThrow();

        // then
        assertThat(found.getAgencia())
                .isEqualTo(conta.getAgencia());
        assertThat(found.getConta())
                .isEqualTo(conta.getConta());
        assertThat(found.getCodigoBanco())
                .isEqualTo(conta.getCodigoBanco());

    }

}
