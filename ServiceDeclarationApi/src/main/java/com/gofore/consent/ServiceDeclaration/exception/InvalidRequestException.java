package com.gofore.consent.ServiceDeclaration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String msg) {
        super(msg);
    }

}
