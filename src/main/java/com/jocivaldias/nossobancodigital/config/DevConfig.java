package com.jocivaldias.nossobancodigital.config;

import com.jocivaldias.nossobancodigital.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class DevConfig {

    private final DBService dbService;

    @Autowired
    public DevConfig(DBService dbService) {
        this.dbService = dbService;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
        if(!strategy.equals("create")) {
            return false;
        }
        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }

    //Observe que se eu substituir MockEmailService por SmtpEmailService poderia enviar e-mails.
    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }


}
