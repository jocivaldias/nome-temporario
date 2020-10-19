package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.services.exception.StorageException;
import com.jocivaldias.nossobancodigital.services.exception.StorageFileNotFoundException;
import com.jocivaldias.nossobancodigital.services.exception.StorageFileContentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService{

    @Value("${document.dir}")
    private String rootDir;

    @Override
    public void store(MultipartFile file, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Error saving empty file " + fileName);
            }

            if(!contentTypes.contains(file.getContentType())) {
                throw new StorageFileContentException("File content not allowed. Allowed: " + contentTypes);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(rootDir).resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
                throw new StorageException("Error saving file " + fileName, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(rootDir).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(rootDir));
        }
        catch (IOException e) {
            throw new StorageException("The storage system could not be initialized", e);
        }
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            if(fileName == null){
                throw new StorageFileNotFoundException("This file does not exist");
            }
            Path file = Paths.get(rootDir).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Couldn't read the file: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read the file: " + fileName, e);
        }
    }

}
