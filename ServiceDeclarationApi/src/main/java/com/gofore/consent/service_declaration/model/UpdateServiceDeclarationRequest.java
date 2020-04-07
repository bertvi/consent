package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049964376368846345L;

    @Getter @Setter
    private String serviceProviderIdentifier;

    @Getter @Setter
    private String serviceDeclarationIdentifier;

    @Getter @Setter
    private LocalDateTime validUntil;

    @Override
    public String toString() {
        return "{\"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentfier\":\"" + serviceDeclarationIdentifier
                + "\",\"validUntil\":\""
                + validUntil + "\"}";
    }
}
