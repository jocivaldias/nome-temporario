package com.jocivaldias.nossobancodigital.config;

import com.jocivaldias.nossobancodigital.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    private final DBService dbService;

    @Autowired
    public TestConfig(DBService dbService) {
        this.dbService = dbService;
    }

    @Bean
    public boolean instantiateDatabase() throws ParseException {
        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
