package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.repositories.ContaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContaServiceTest {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    // write test cases here
    @Test
    public void givenConta_whenThreadUpdateSaldo_thenReturnSaldoCorreto() throws InterruptedException {
        // when
        final ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                Conta aux = contaRepository.findById(1).orElseThrow();
                contaService.atualizaSaldo(aux, 10.00);
            });
            executor.execute(() -> {
                Conta aux = contaRepository.findById(2).orElseThrow();
                contaService.atualizaSaldo(aux, 5.00);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        Conta aux = contaRepository.findById(1).orElseThrow();
        Conta aux2 = contaRepository.findById(2).orElseThrow();

        // then
        assertThat(aux.getSaldo())
                .isEqualTo(200.00);
        assertThat(aux2.getSaldo())
                .isEqualTo(100.00);

    }
}
