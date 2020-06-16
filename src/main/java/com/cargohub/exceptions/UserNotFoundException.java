package com.cargohub.exceptions;

public class UserNotFoundException extends UserServiceException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
