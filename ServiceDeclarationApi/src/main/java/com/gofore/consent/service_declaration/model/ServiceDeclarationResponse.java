package com.gofore.consent.service_declaration.model;

import java.io.Serializable;

public class ServiceDeclarationResponse implements Serializable {

    private static final long serialVersionUID = 4049961376368846345L;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private String response;

    @Override
    public String toString() {
        return "{\"response\": \"" + response + "\"}";
    }

}
