package com.jocivaldias.nossobancodigital.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService{

    private static final Logger logger = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void simpleEmail(SimpleMailMessage simpleMailMessage) {
        logger.info("------------------ Simulating email sending ----------------------");
        logger.info(simpleMailMessage.toString());
        logger.info("------------------ Email sent ----------------------");
    }

}
