package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    // write test cases here

    @Test
    public void whenFindByEmail_thenReturnCliente() {
        // given
        Cliente cli = new Cliente(null, "Exemplo", "Sobrenome", "mail@mail.com",
                LocalDate.of(2000,01,01), "41949564002");
        entityManager.persist(cli);
        entityManager.flush();

        // when
        Cliente found = clienteRepository.findByEmail(cli.getEmail());

        // then
        assertThat(found.getEmail())
                .isEqualTo(cli.getEmail());
    }

    @Test
    public void whenFindByEmailAndCpf_thenReturnCliente() {
        // given
        Cliente cli = new Cliente(null, "Exemplo", "Sobrenome", "mail@mail.com",
                LocalDate.of(2000,01,01), "41949564002");
        entityManager.persist(cli);
        entityManager.flush();

        // when
        Cliente found = clienteRepository.findByEmailAndCpf(cli.getEmail(), cli.getCpf());

        // then
        assertThat(found.getEmail())
                .isEqualTo(cli.getEmail());
        assertThat(found.getCpf())
                .isEqualTo(cli.getCpf());
    }

    @Test
    public void whenNotFound_thenThrowObjectNotFoundException() {
        // given
        Integer id = Integer.valueOf(Integer.MAX_VALUE);

        // when
        Optional<Cliente> found = clienteRepository.findById(id);

        // then
        assertThrows(ObjectNotFoundException.class, () -> {
            found.orElseThrow(() -> new ObjectNotFoundException(""));
        });
    }

}
