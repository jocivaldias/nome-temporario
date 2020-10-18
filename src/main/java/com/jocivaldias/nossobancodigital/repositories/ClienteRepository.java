package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Transactional(readOnly = true)
    public Cliente findByEmail(String email);

    public Cliente findByEmailAndCpf(String email, String cpf);

}
