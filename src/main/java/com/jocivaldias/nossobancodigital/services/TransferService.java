package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Transfer;
import com.jocivaldias.nossobancodigital.dto.TransferDTO;
import com.jocivaldias.nossobancodigital.repositories.AccountRepository;
import com.jocivaldias.nossobancodigital.repositories.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository repo;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public TransferService(TransferRepository repo, AccountRepository accountRepository,
                           AccountService accountService) {
        this.repo = repo;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Transfer insert(Transfer obj){
        obj.setId(null);
        return repo.save(obj);
    }

    public Transfer fromDTO(TransferDTO objDto) {
        Transfer transfer = new Transfer();

        transfer.setId(null);
        transfer.setOriginBranchNumber(objDto.getOriginBranchNumber());
        transfer.setOriginBankNumber(objDto.getOriginBankNumber());
        transfer.setOriginAccountNumber(objDto.getOriginAccountNumber());
        transfer.setTransferDate(objDto.getTransferDate());
        transfer.setIdentifierDocument(objDto.getIdentifierDocument());
        transfer.setTransferIdOriginBank(objDto.getTransferIdOriginBank());
        transfer.setTransferValue(objDto.getTransferValue());

        Account account = accountRepository.findByBranchNumberAndAccountNumber(objDto.getTargetBranchNumber(), objDto.getTargetAccountNumber());

        // Account does not exist in our bank
        if( account == null){
            logger.info("Account not found in our bank: {"
                    +objDto.getTargetBranchNumber()
                    + ", "
                    + objDto.getTargetAccountNumber()
                    + "}.");
            return null;
        }

        transfer.setTargetAccount(account);
        return transfer;
    }

    @Async
    @Transactional
    public void processesTransfers(List<TransferDTO> listObjDto) {
        for(TransferDTO x: listObjDto){
            Transfer transfer = fromDTO(x);
            if(transfer != null) {
                try {
                    insert(transfer);
                    accountService.updateBalance(transfer.getTargetAccount(), transfer.getTransferValue());
                } catch (Exception ex){
                    logger.info("Transfer already registered, error: " + ex.getMessage());
                }
            }
        }
    }
}
