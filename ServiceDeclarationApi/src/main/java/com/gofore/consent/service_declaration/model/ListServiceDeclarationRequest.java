package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ListServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049961366358846345L;

    @Getter @Setter
    private String serviceProviderIdentifier;

    @Getter @Setter
    private String serviceDeclarationIdentifier;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String technicalDescription;

    @Getter @Setter
    private LocalDateTime validAt;

    @Getter @Setter
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
