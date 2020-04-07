package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class UpdateServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049964376368846345L;

    private String serviceProviderIdentifier;

    private String serviceDeclarationIdentifier;

    private LocalDateTime validUntil;

    @Override
    public String toString() {
        return "{\"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentfier\":\"" + serviceDeclarationIdentifier
                + "\",\"validUntil\":\""
                + validUntil + "\"}";
    }
}
