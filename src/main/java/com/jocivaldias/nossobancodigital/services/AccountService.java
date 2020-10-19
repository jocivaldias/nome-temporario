package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Account;
import com.jocivaldias.nossobancodigital.domain.Proposal;
import com.jocivaldias.nossobancodigital.domain.enums.ProposalStatus;
import com.jocivaldias.nossobancodigital.repositories.AccountRepository;
import com.jocivaldias.nossobancodigital.security.UserSS;
import com.jocivaldias.nossobancodigital.services.exception.AprovacaoApiException;
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

import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository repo;
    private final ProposalService proposalService;

    private Random rand = new Random();

    @Value("${url.api.aprovacao}")
    private String approvalApiUrl;

    @Autowired
    public AccountService(AccountRepository repo, ProposalService proposalService) {
        this.repo = repo;
        this.proposalService = proposalService;
    }

    public Account find(Integer id) {
        UserSS user = UserService.authenticated();

        if(user==null || !id.equals(user.getId())){
            throw new AuthorizationException("Access denied");
        }

        Optional<Account> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Account not found! Id: " + id + ", Type: " + Account.class.getName()
        ));
    }

    public Account insert(Account obj) {
        obj.setId(null);
        return repo.save(obj);
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
                        throw new AprovacaoApiException("Error during account approval");
                    }
                })
                .doOnError((throwable) -> {
                    logger.error("Request could not be sent");
                    throw new AprovacaoApiException("Error sending request");
                })
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))
                        .doAfterRetry(retrySignal -> {
                            logger.info("Attempts " + retrySignal.totalRetries());
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)
                                -> new AprovacaoApiException("Could not connect to the service")))
                .subscribe();
    }

    public Account createAccount(Proposal proposal) {
        Account account = new Account();

        account.setBranchNumber(generatesRandom(4));
        account.setAccountNumber(generatesRandom(8));
        account.setBankNumber("341");
        account.setBalance(0.00);

        account.setProposal(proposal);
        proposal.setAccount(account);

        account = repo.save(account);

        return account;
    }

    public void updatePassword(Account obj) {
        Account saveObj = repo.findById(obj.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Account not found"));

        saveObj.setPassword(obj.getPassword());
        repo.save(saveObj);
    }


    public synchronized void updateBalance(Account obj, Double valor) {
        Account saveObj = repo.findById(obj.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Account not found"));
        obj.setBalance(saveObj.getBalance() + valor);
        repo.save(obj);
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
        char[] vet = new char[size];
        for( int i=0; i<size; i++){
            vet[i] = randomDigitChar();
        }
        return new String(vet);
    }

    private char randomDigitChar() {
        return (char) (rand.nextInt(10) + 48);
    }

}
