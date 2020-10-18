package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.TokenAtivacao;
import com.jocivaldias.nossobancodigital.dto.CadastrarSenhaDTO;
import com.jocivaldias.nossobancodigital.dto.SolicitarTokenDTO;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.ContaRepository;
import com.jocivaldias.nossobancodigital.repositories.TokenAtivacaoRepository;
import com.jocivaldias.nossobancodigital.services.exception.InvalidTokenException;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Random;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ContaRepository contaRepository;

    private final EmailService emailService;

    private final TokenAtivacaoRepository tokenAtivacaoRepository;

    private final ContaService contaService;

    private Random rand = new Random();

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    public AuthService(ClienteRepository clienteRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailService emailService, TokenAtivacaoRepository tokenAtivacaoRepository,
                       ContaRepository contaRepository, ContaService contaService) {
        this.clienteRepository = clienteRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.tokenAtivacaoRepository = tokenAtivacaoRepository;
        this.contaRepository = contaRepository;
        this.contaService = contaService;
    }

    public void solicitarToken(SolicitarTokenDTO solicitarTokenDTO) {
        Cliente cliente = clienteRepository.findByEmailAndCpf(solicitarTokenDTO.getEmail(), solicitarTokenDTO.getCpf());

        if(cliente == null){
            throw new ObjectNotFoundException("Cliente não encontrado");
        }

        TokenAtivacao tokenAtivacao = new TokenAtivacao();
        tokenAtivacao.setConta(cliente.getProposta().getConta());
        tokenAtivacao.setUsado(false);
        tokenAtivacao.setId(null);
        tokenAtivacao.setToken(newToken(6));
        tokenAtivacao.setDataExpiracao(
                LocalDateTime.now().plus(expiration, ChronoField.MILLI_OF_DAY.getBaseUnit())
        );

        tokenAtivacao = tokenAtivacaoRepository.save(tokenAtivacao);
        emailService.registrarNovaSenha(tokenAtivacao);
    }

    public void cadastrarSenha(CadastrarSenhaDTO cadastrarSenhaDTO) {
        Conta conta = contaRepository.findByPropostaClienteEmailAndPropostaClienteCpf(
                cadastrarSenhaDTO.getEmail(), cadastrarSenhaDTO.getCpf());

        if(conta == null){
            throw new ObjectNotFoundException("Conta não encontrada");
        }

        TokenAtivacao tokenAtivacao = tokenAtivacaoRepository.findByContaIdAndToken(conta.getId(),
                cadastrarSenhaDTO.getToken());

        if(tokenAtivacao == null
                || tokenAtivacao.getUsado()
                || tokenAtivacao.getDataExpiracao().isBefore(LocalDateTime.now())
        ) {
            throw new InvalidTokenException("Token de validação inválido");
        }

        tokenAtivacao.setUsado(true);
        tokenAtivacaoRepository.save(tokenAtivacao);

        conta.setSenha(bCryptPasswordEncoder.encode(cadastrarSenhaDTO.getSenha()));

        contaService.updateSenha(conta);
        emailService.senhaAtualizada(conta);
    }

    private String newToken(int size) {
        char[] vet = new char[size];
        for( int i=0; i<size; i++){
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = rand.nextInt(3);
        if( opt == 0 ){ // gera um digito
            return (char) (rand.nextInt(10) + 48);
        } else if( opt == 1){ // gera letra maiscula
            return (char) (rand.nextInt(26) + 65);
        } else { //gera letra minuscula
            return (char) (rand.nextInt(26) + 97);
        }
    }
}
