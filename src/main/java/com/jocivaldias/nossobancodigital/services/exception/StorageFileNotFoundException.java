package com.jocivaldias.nossobancodigital.services.exception;

public class StorageFileNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
