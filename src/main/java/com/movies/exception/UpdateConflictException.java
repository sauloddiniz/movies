package com.movies.exception;

public class UpdateConflictException extends RuntimeException {
    public UpdateConflictException(String message) {
        super(message);
    }
}
