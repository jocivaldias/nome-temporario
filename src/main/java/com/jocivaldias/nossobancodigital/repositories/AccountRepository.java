package com.jocivaldias.nossobancodigital.repositories;

import com.jocivaldias.nossobancodigital.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Transactional(readOnly = true)
    public Account findByProposalClientEmailAndProposalClientCpf(String email, String cpf);

    @Transactional(readOnly = true)
    public Account findByBranchNumberAndAccountNumber(String branchNumber, String accountNumber);
}
