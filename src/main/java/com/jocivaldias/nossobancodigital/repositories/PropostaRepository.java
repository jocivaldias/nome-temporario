package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Integer> {

}
