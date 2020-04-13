package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091876091924046844L;

    @Getter
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

}
