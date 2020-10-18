package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {

    public Conta findByPropostaClienteEmailAndPropostaClienteCpf(String email, String cpf);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<Conta> findById(Integer id);

    public Conta findByAgenciaAndConta(String agencia, String conta);
}
