package com.gofore.consent.ServiceDeclaration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooBroadQueryException extends RuntimeException {

    public TooBroadQueryException() {
    }

    public TooBroadQueryException(String msg) {
        super(msg);
    }
}
