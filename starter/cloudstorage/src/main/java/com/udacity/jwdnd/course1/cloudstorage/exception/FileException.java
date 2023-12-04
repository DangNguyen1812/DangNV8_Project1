package com.udacity.jwdnd.course1.cloudstorage.exception;

public class FileException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public FileException(String message) {
        super(message);
    }
}
