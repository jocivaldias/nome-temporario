package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {

}
