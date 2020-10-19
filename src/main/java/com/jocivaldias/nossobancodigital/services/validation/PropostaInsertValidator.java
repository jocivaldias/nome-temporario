package com.jocivaldias.nossobancodigital.services.validation;

import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.dto.NewProposalDTO;
import com.jocivaldias.nossobancodigital.repositories.ClientRepository;
import com.jocivaldias.nossobancodigital.resources.exception.FieldMessage;
import com.jocivaldias.nossobancodigital.services.validation.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PropostaInsertValidator implements ConstraintValidator<PropostaInsert, NewProposalDTO> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void initialize(PropostaInsert ann) {
    }

    @Override
    public boolean isValid(NewProposalDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(!DateUtils.maiorDeIdade(objDto.getBirthdate())){
            list.add(new FieldMessage("dataNascimento", "Deve ser maior de idade (18 anos)."));
        }

        Client aux = clientRepository.findByEmail(objDto.getEmail());
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