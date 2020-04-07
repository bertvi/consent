package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ListServiceDeclarationResponse implements Serializable {

    private static final long serialVersionUID = 4049961576368846345L;

    @Getter @Setter
    private List<ServiceDeclaration> declarations;

    @Getter @Setter
    private String serviceProviderIdentifier;

    @Getter @Setter
    private String serviceDeclarationIdentifier;

    @Override
    public String toString() {
        return "{\"declarations\":" + declarations
                + ", \"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentifier\":\""
                + serviceDeclarationIdentifier + "\"}";
    }

}
