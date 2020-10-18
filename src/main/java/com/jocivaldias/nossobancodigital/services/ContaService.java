package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.domain.enums.Perfil;
import com.jocivaldias.nossobancodigital.domain.enums.StatusProposta;
import com.jocivaldias.nossobancodigital.repositories.ContaRepository;
import com.jocivaldias.nossobancodigital.security.UserSS;
import com.jocivaldias.nossobancodigital.services.exception.AprovacaoApiException;
import com.jocivaldias.nossobancodigital.services.exception.AuthorizationException;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.persistence.LockModeType;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

    private final ContaRepository repo;
    private final PropostaService propostaService;

    private Random rand = new Random();

    @Value("${url.api.aprovacao}")
    private String urlApiAprovacao;

    @Autowired
    public ContaService(ContaRepository repo, PropostaService propostaService) {
        this.repo = repo;
        this.propostaService = propostaService;
    }

    public Conta find(Integer id) {
        UserSS user = UserService.authenticated();

        if(user==null || !id.equals(user.getId())){
            throw new AuthorizationException("Acesso negado");
        }

        Optional<Conta> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Conta não encontrada! Id: " + id + ", Tipo: " + Conta.class.getName()
        ));
    }

    public Conta insert(Conta obj) {
        obj.setId(null);
        return repo.save(obj);
    }

    public void liberarConta(Proposta proposta) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/propostas/{id}/confirmacao-api")
                .buildAndExpand(proposta.getId())
                .toUri();

        WebClient webClient = WebClient.builder()
                .baseUrl(urlApiAprovacao)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Nosso Banco Digital")
                .defaultHeader(HttpHeaders.LOCATION, uri.toString())
                .filter(logRequest())
                .filter(logResponse())
                .build();

        webClient
                .post()
                .uri("/4589e639-1f72-4e15-b672-c4049f03e87b")
                .body(BodyInserters.fromValue(proposta))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess((clientResponse) -> {
                    if (clientResponse.getStatusCode().is5xxServerError()
                            || clientResponse.getStatusCode().is4xxClientError()) {
                        propostaService.updateStatus(proposta, StatusProposta.REENVIAR_LIBERACAO_SISTEMA);
                        logger.error("Erro durante a aprovação da conta, status: [{}]", clientResponse.getStatusCode());
                        throw new AprovacaoApiException("Erro durante a aprovação da conta");
                    }
                })
                .doOnError((throwable) -> {
                    logger.error("Não foi possível enviar a requisição");
                    throw new AprovacaoApiException("Erro ao enviar requisição");
                })
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))
                        .doAfterRetry(retrySignal -> {
                            logger.info("Tentativas " + retrySignal.totalRetries());
                        })
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal)
                                -> new AprovacaoApiException("Não foi possível conectar com o serviço")))
                .subscribe();
    }

    public Conta criaConta(Proposta proposta) {
        Conta conta = new Conta();

        conta.setAgencia(geraRandomico(4));
        conta.setConta(geraRandomico(8));
        conta.setCodigoBanco("341");
        conta.setSaldo(0.00);

        conta.setProposta(proposta);
        proposta.setConta(conta);

        conta = repo.save(conta);

        return conta;
    }

    public void updateSenha(Conta obj) {
        Conta saveObj = repo.findById(obj.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Conta não encontrada"));

        saveObj.setSenha(obj.getSenha());
        repo.save(saveObj);
    }


    public synchronized void atualizaSaldo(Conta obj, Double valor) {
        Conta saveObj = repo.findById(obj.getId()).orElseThrow(
                () -> new ObjectNotFoundException("Conta não encontrada"));
        obj.setSaldo(saveObj.getSaldo() + valor);
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

    private String geraRandomico(int size){
        char[] vet = new char[size];
        for( int i=0; i<size; i++){
            vet[i] = randomDigitChar();
        }
        return new String(vet);
    }

    private char randomDigitChar() {
        return (char) (rand.nextInt(10) + 48); // Tabela ASCII
    }

}
