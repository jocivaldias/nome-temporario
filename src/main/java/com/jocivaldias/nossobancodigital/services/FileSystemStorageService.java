package com.jocivaldias.nossobancodigital.services;

import com.jocivaldias.nossobancodigital.services.exception.StorageException;
import com.jocivaldias.nossobancodigital.services.exception.StorageFileNotFoundException;
import com.jocivaldias.nossobancodigital.services.exception.StorageFileContentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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
                throw new StorageException("Erro ao salvar arquivo vazio " + fileName);
            }

            if(!contentTypes.contains(file.getContentType())) {
                throw new StorageFileContentException("Conteúdo do arquivo não permitido. Permitidos: " + contentTypes);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(rootDir).resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Erro ao salvar arquivo " + fileName, e);
        }
    }

    @Override
    public void deleteAll() {
        //FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(rootDir));
        }
        catch (IOException e) {
            throw new StorageException("Não foi possível inicializar o sistema de armazenamento", e);
        }
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            if(fileName == null){
                throw new StorageFileNotFoundException("Este arquivo não existe");
            }
            Path file = Paths.get(rootDir).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Não foi possível ler o arquivo: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Não foi possível ler o arquivo: " + fileName, e);
        }
    }

}
