package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class AddServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049961366368846345L;

    private String serviceProviderIdentifier;

    private String serviceDeclarationIdentifier;

    private String serviceDeclarationName;

    private String serviceDeclarationDescription;

    private String technicalDescription;

    private LocalDateTime validUntil;

    private Long consentMaxDurationSeconds;

    private Long maxCacheSeconds;

    private Boolean needSignature;

    @Override
    public String toString() {
        return "{\"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentfier\":\"" + serviceDeclarationIdentifier
                + "\",\"serviceDeclarationName\":\"" + serviceDeclarationName
                + "\",\"serviceDeclarationDescription\":\"" + serviceDeclarationDescription
                + "\",\"technicalDescription\":\"" + technicalDescription
                + "\",\"validUntil\":\"" + validUntil
                + "\",\"consentMaxDurationSeconds\":\"" + consentMaxDurationSeconds
                + "\",\"maxCacheSeconds\":\"" + maxCacheSeconds
                + "\",\"needSignature\":\""
                + needSignature + "\"}";
    }
}
