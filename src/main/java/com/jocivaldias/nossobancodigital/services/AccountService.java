package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.dto.AccountDTO;
import com.jocivaldias.nossobancodigital.repositories.AccountRepository;
import com.jocivaldias.nossobancodigital.security.UserSS;
import com.jocivaldias.nossobancodigital.services.exception.ApprovalApiException;
import com.jocivaldias.nossobancodigital.services.exception.AuthorizationException;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final ProposalService proposalService;

    private Random randomGenerator = new Random();

    @Value("${url.api.aprovacao}")
    private String approvalApiUrl;

    @Autowired
    public AccountService(AccountRepository accountRepository, ProposalService proposalService) {
        this.accountRepository = accountRepository;
        this.proposalService = proposalService;
    }

    public Account find(Integer id) {
        UserSS user = UserService.authenticated();

        if(user==null || !id.equals(user.getId())){
            throw new AuthorizationException("Access denied");
        }

        Optional<Account> account = accountRepository.findById(id);
        return account.orElseThrow(() -> new ObjectNotFoundException(
                "Account not found! Id: " + id + ", Type: " + Account.class.getName()
        ));
    }

    public Account insert(Account account) {
        account.setId(null);
        return accountRepository.save(account);
    }

    public void requestProposalApproval(Proposal proposal) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/proposals/{id}/api-confirmation")
                .buildAndExpand(proposal.getId())
                .toUri();

        WebClient webClient = WebClient.builder()
                .baseUrl(approvalApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Nosso Banco Digital")
                .defaultHeader(HttpHeaders.LOCATION, uri.toString())
                .filter(logRequest())
                .filter(logResponse())
                .build();

        webClient
                .post()
                .uri("/4589e639-1f72-4e15-b672-c4049f03e87b")
                .body(BodyInserters.fromValue(proposal))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess((clientResponse) -> {
                    if (clientResponse.getStatusCode().is5xxServerError()
                            || clientResponse.getStatusCode().is4xxClientError()) {
                        proposalService.updateStatus(proposal, ProposalStatus.RESEND_SYSTEM_ACCEPTANCE);
                        logger.error("Error during account approval, status: [{}]", clientResponse.getStatusCode());
                        throw new ApprovalApiException("Error during account approval");
                    }
                })
                .doOnError((throwable) -> {
                    logger.error("Request could not be sent");
                    throw new ApprovalApiException("Error sending request");
                })
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))
                        .doAfterRetry(retrySignal -> {
                            logger.info("Attempts " + retrySignal.totalRetries());
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)
                                -> new ApprovalApiException("Could not connect to the service")))
                .subscribe();
    }

    public Account createAccount(Proposal proposal) {
        Account account = new Account();

        account.setBranchNumber(generatesRandom(4));
        account.setAccountNumber(generatesRandom(8));
        account.setBankNumber("341");
        account.setBalance(BigDecimal.ZERO);

        account.setProposal(proposal);
        proposal.setAccount(account);

        account = accountRepository.save(account);

        return account;
    }

    public void updatePassword(Account dataAccount) {
        Account saveAccount = accountRepository.findById(dataAccount.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Account not found"));

        saveAccount.setPassword(dataAccount.getPassword());
        accountRepository.save(saveAccount);
    }

    public synchronized void updateBalance(Account originAccount, BigDecimal valor) {
        Account saveAccount = accountRepository.findById(originAccount.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Account not found"));
        originAccount.setBalance(saveAccount.getBalance().add(valor));
        accountRepository.save(originAccount);
    }


    public AccountDTO toDTO(Account account) {
        return new AccountDTO(account.getBranchNumber(), account.getAccountNumber(), account.getBankNumber(),
                account.getProposal(), account.getBalance());
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logger.info("Request {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            logger.info("Response status code {} ", response.statusCode());
            return Mono.just(response);
        });
    }

    private String generatesRandom(int size){
        char[] randomString = new char[size];
        for( int i=0; i<size; i++){
            randomString[i] = randomDigitChar();
        }
        return new String(randomString);
    }

    private char randomDigitChar() {
        return (char) (randomGenerator.nextInt(10) + 48);
    }
}
