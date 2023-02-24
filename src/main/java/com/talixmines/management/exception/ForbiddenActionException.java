package com.talixmines.management.exception;

public class ForbiddenActionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenActionException(String message) {
        super(message);
    }

}

