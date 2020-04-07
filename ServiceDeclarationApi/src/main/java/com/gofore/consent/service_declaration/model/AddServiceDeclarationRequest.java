package com.gofore.consent.service_declaration.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AddServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049961366368846345L;

    @Getter @Setter
    private String serviceProviderIdentifier;

    @Getter @Setter
    private String serviceDeclarationIdentifier;

    @Getter @Setter
    private String serviceDeclarationName;

    @Getter @Setter
    private String serviceDeclarationDescription;

    @Getter @Setter
    private String technicalDescription;

    @Getter @Setter
    private LocalDateTime validUntil;

    @Getter @Setter
    private Long consentMaxDurationSeconds;

    @Getter @Setter
    private Long maxCacheSeconds;

    @Getter @Setter
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
