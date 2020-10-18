package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {

    @Transactional(readOnly = true)
    public Conta findByPropostaClienteEmailAndPropostaClienteCpf(String email, String cpf);

    public Optional<Conta> findById(Integer id);

    @Transactional(readOnly = true)
    public Conta findByAgenciaAndConta(String agencia, String conta);
}
