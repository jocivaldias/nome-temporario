package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.TokenAtivacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenAtivacaoRepository extends JpaRepository<TokenAtivacao, Integer> {

    @Transactional(readOnly = true)
    public TokenAtivacao findByContaIdAndToken(Integer id, String token);

}
