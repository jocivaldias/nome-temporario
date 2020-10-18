package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Conta;
import com.jocivaldias.nossobancodigital.domain.enums.Perfil;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.ContaRepository;
import com.jocivaldias.nossobancodigital.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final ContaRepository contaRepository;

    @Autowired
    public UserDetailServiceImpl(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String agenciaConta) throws UsernameNotFoundException {
        Conta conta = contaRepository.findByAgenciaAndConta(agenciaConta.substring(0, 4), agenciaConta.substring(4, 12));

        if( conta == null ){
            throw new UsernameNotFoundException(agenciaConta);
        }

        Set<Perfil> perfis = new HashSet<>();
        perfis.add(Perfil.CLIENTE);
        return new UserSS(conta.getId(), agenciaConta, conta.getSenha(), perfis);
    }

}
