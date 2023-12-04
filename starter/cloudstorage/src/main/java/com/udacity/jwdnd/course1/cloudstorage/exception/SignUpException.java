package com.udacity.jwdnd.course1.cloudstorage.exception;

public class SignUpException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SignUpException(String message) {
        super(message);
    }
}
