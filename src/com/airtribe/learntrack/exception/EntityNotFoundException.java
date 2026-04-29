package com.airtribe.learntrack.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityType, int id) {
        super(entityType + " with id " + id + " was not found.");
    }
}
