package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.dto.PropostaDTO;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import com.jocivaldias.nossobancodigital.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropostaService {

    @Autowired
    private PropostaRepository repo;
    @Autowired
    private ClienteRepository clienteRepository;

    public Proposta find(Integer id){
        Optional<Proposta> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Proposta.class.getName()
        ));
    }

    public Proposta insert(Proposta obj){
        obj.setId(null);
        obj = repo.save(obj);
        clienteRepository.save(obj.getCliente());
        return obj;
    }

    public Proposta update(Proposta obj) {
        Proposta newObj = find(obj.getId());
        updateDadosBasicos(newObj, obj);
        newObj = repo.save(newObj);
        clienteRepository.save(newObj.getCliente());
        return newObj;
    }

    public Proposta fromDTO(PropostaNewDTO objDto) {
        Proposta proposta = new Proposta(null);

        Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getSobrenome(), objDto.getEmail(),
                objDto.getDataNascimento(), objDto.getCpf());

        proposta.setCliente(cliente);
        cliente.setProposta(proposta);

        return proposta;
    }

    public Proposta fromDTO(PropostaDTO objDto) {
        Proposta proposta = new Proposta(null);

        Cliente cliente = new Cliente();
        cliente.setNome(objDto.getNome());
        cliente.setSobrenome(objDto.getSobrenome());
        cliente.setEmail(objDto.getEmail());
        cliente.setDataNascimento(objDto.getDataNascimento());

        cliente.setProposta(proposta);
        proposta.setCliente(cliente);

        return proposta;
    }

    private void updateDadosBasicos(Proposta newObj, Proposta obj) {
        newObj.getCliente().setNome(obj.getCliente().getNome());
        newObj.getCliente().setSobrenome(obj.getCliente().getSobrenome());
        newObj.getCliente().setEmail(obj.getCliente().getEmail());
        newObj.getCliente().setDataNascimento(obj.getCliente().getDataNascimento());
    }
}
