package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class ListServiceDeclarationResponse implements Serializable {

    private static final long serialVersionUID = 4049961576368846345L;

    private List<ServiceDeclaration> declarations;

    private String serviceProviderIdentifier;

    private String serviceDeclarationIdentifier;

    @Override
    public String toString() {
        return "{\"declarations\":" + declarations
                + ", \"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentifier\":\""
                + serviceDeclarationIdentifier + "\"}";
    }

}
