package com.gofore.consent.service_declaration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooBroadQueryException extends RuntimeException {

    public TooBroadQueryException(String msg) {
        super(msg);
    }
}
