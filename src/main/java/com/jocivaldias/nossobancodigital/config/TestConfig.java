package com.jocivaldias.nossobancodigital.config;

import com.jocivaldias.nossobancodigital.services.DBService;
import com.jocivaldias.nossobancodigital.services.FileSystemStorageService;
import com.jocivaldias.nossobancodigital.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
        dbService.instantiateTestDatabase();
        return true;
    }

    @Bean
    public StorageService storageService() {
        return new FileSystemStorageService();
    }
}
