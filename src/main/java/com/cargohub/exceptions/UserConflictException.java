package com.cargohub.exceptions;

public class UserConflictException extends UserServiceException {

    public UserConflictException(String message) {
        super(message);
    }
}
