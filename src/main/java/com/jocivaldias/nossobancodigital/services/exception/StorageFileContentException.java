package com.jocivaldias.nossobancodigital.services.exception;

public class StorageFileContentException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public StorageFileContentException(String message) {
        super(message);
    }

    public StorageFileContentException(String message, Throwable cause) {
        super(message, cause);
    }

}
