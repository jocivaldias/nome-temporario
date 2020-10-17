package com.jocivaldias.nossobancodigital.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public interface StorageService {

    static final List<String> contentTypes = Arrays.asList("application/pdf");

    void init();

    void store(MultipartFile file, String fileName);

    void deleteAll();

    Resource loadAsResource(String filename);

}
