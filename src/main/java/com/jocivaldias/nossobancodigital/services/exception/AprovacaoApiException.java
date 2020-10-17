package com.jocivaldias.nossobancodigital.services.exception;

public class AprovacaoApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AprovacaoApiException(String message) {
        super(message);
    }

    public AprovacaoApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
