package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    // write test cases here
    @Test
    public void givenAccount_whenThreadUpdateBalance_thenReturnCorrectBalance() throws InterruptedException {
        // when
        final ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                Account aux = accountRepository.findById(1).orElseThrow();
                accountService.updateBalance(aux, new BigDecimal(10));
            });
            executor.execute(() -> {
                Account aux = accountRepository.findById(2).orElseThrow();
                accountService.updateBalance(aux, new BigDecimal(5));
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        Account aux = accountRepository.findById(1).orElseThrow();
        Account aux2 = accountRepository.findById(2).orElseThrow();

        // then
        assertThat(aux.getBalance())
                .isEqualByComparingTo(new BigDecimal(200));
        assertThat(aux2.getBalance())
                .isEqualByComparingTo(new BigDecimal(100));

    }
}
