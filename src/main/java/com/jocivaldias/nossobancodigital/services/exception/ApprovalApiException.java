package com.jocivaldias.nossobancodigital.services.exception;

public class ApprovalApiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ApprovalApiException(String message) {
        super(message);
    }

    public ApprovalApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
