package com.udacity.jwdnd.course1.cloudstorage.exception;

public class CredentialException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CredentialException(String message) {
        super(message);
    }
}
