package com.jocivaldias.nossobancodigital;

import com.jocivaldias.nossobancodigital.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NossoBancoDigitalApplication implements CommandLineRunner {

    @Autowired
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(NossoBancoDigitalApplication.class, args);
    }

    @Override
    public void run(String... args) {
        storageService.init();
    }

}
