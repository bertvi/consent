package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class ListServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049961366358846345L;

    private String serviceProviderIdentifier;

    private String serviceDeclarationIdentifier;

    private String name;

    private String description;

    private String technicalDescription;

    private LocalDateTime validAt;

    private Boolean details;

    @Override
    public String toString() {
        return "{\"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentifier\":\"" + serviceDeclarationIdentifier
                + "\",\"name\":\"" + name
                + "\",\"description\":\"" + description
                + "\",\"technicalDescription\":\"" + technicalDescription
                + "\",\"validAt\":\"" + validAt
                + "\",\"details\":\""
                + details + "\"}";
    }

}
