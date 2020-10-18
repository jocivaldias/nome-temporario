package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Endereco;
import com.jocivaldias.nossobancodigital.domain.TokenAtivacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenAtivacaoRepository extends JpaRepository<TokenAtivacao, Integer> {

    public TokenAtivacao findByContaIdAndToken(Integer id, String token);

}
