package com.sc.salary.process.exception.handler;

public class FileProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}