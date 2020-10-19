package com.jocivaldias.nossobancodigital.services.validation;

import com.jocivaldias.nossobancodigital.domain.Client;
import com.jocivaldias.nossobancodigital.dto.ProposalDTO;
import com.jocivaldias.nossobancodigital.repositories.ClientRepository;
import com.jocivaldias.nossobancodigital.resources.exception.FieldMessage;
import com.jocivaldias.nossobancodigital.services.validation.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropostaUpdateValidator implements ConstraintValidator<PropostaUpdate, ProposalDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void initialize(PropostaUpdate ann) {
    }

    @Override
    public boolean isValid(ProposalDTO objDto, ConstraintValidatorContext context) {
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        if(!DateUtils.maiorDeIdade(objDto.getBirthdate())){
            list.add(new FieldMessage("dataNascimento", "Deve ser maior de idade (18 anos)."));
        }

        Client aux = clientRepository.findByEmail(objDto.getEmail());
        if( aux != null && !aux.getId().equals(uriId) ){
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