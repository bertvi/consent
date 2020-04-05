package com.gofore.consent.ServiceDeclaration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateDeclarationException extends RuntimeException {

    public DuplicateDeclarationException() {
    }

    public DuplicateDeclarationException(String msg) {
        super(msg);
    }
}
