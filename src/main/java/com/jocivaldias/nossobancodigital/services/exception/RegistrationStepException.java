package com.jocivaldias.nossobancodigital.services.exception;

public class RegistrationStepException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RegistrationStepException(String message) {
        super(message);
    }

    public RegistrationStepException(String message, Throwable cause) {
        super(message, cause);
    }

}
