package com.jocivaldias.nossobancodigital.services.validation;

import com.jocivaldias.nossobancodigital.domain.Cliente;
import com.jocivaldias.nossobancodigital.domain.Proposta;
import com.jocivaldias.nossobancodigital.dto.PropostaNewDTO;
import com.jocivaldias.nossobancodigital.repositories.ClienteRepository;
import com.jocivaldias.nossobancodigital.repositories.PropostaRepository;
import com.jocivaldias.nossobancodigital.resources.exception.FieldMessage;
import com.jocivaldias.nossobancodigital.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PropostaInsertValidator implements ConstraintValidator<PropostaInsert, PropostaNewDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(PropostaInsert ann) {
    }

    @Override
    public boolean isValid(PropostaNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(!BR.isValidCPF(objDto.getCpf())){
            list.add(new FieldMessage("cpf", "CPF inválido."));
        }

        Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
        if( aux != null ){
            list.add(new FieldMessage("email", "E-mail já existente."));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }

        return list.isEmpty();
    }
}