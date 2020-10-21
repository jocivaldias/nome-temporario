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

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository,
                           AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public Transfer insert(Transfer transfer){
        transfer.setId(null);
        return transferRepository.save(transfer);
    }

    public Transfer fromDTO(TransferDTO transferDTO) {
        Transfer transfer = new Transfer();

        transfer.setId(null);
        transfer.setOriginBranchNumber(transferDTO.getOriginBranchNumber());
        transfer.setOriginBankNumber(transferDTO.getOriginBankNumber());
        transfer.setOriginAccountNumber(transferDTO.getOriginAccountNumber());
        transfer.setTransferDate(transferDTO.getTransferDate());
        transfer.setIdentifierDocument(transferDTO.getIdentifierDocument());
        transfer.setTransferIdOriginBank(transferDTO.getTransferIdOriginBank());
        transfer.setTransferValue(transferDTO.getTransferValue());

        Account account = accountRepository.findByBranchNumberAndAccountNumber(transferDTO.getTargetBranchNumber(), transferDTO.getTargetAccountNumber());

        // Account does not exist in our bank
        if( account == null){
            logger.info("Account not found in our bank: {"
                    +transferDTO.getTargetBranchNumber()
                    + ", "
                    + transferDTO.getTargetAccountNumber()
                    + "}.");
            return null;
        }

        transfer.setTargetAccount(account);
        return transfer;
    }

    @Async
    public void processesTransfers(List<TransferDTO> transferDTOList) {
        transferDTOList.stream().forEach(
                transferDTO -> {
                    Transfer transfer = fromDTO(transferDTO);
                    if(transfer != null) {
                        try {
                            insert(transfer);
                            accountService.updateBalance(transfer.getTargetAccount(), transfer.getTransferValue());
                        } catch (Exception ex){
                            logger.info("Transfer already registered, error: " + ex.getMessage());
                        }
                    }
                }
        );
    }
}
