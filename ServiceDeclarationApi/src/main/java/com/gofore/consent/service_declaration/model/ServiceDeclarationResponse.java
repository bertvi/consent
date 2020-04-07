package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceDeclarationResponse implements Serializable {

    private static final long serialVersionUID = 4049961376368846345L;
    @Getter @Setter
    private String response;

    @Override
    public String toString() {
        return "{\"response\": \"" + response + "\"}";
    }

}
