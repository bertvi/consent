package com.gofore.consent.ServiceDeclaration.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    public String getServiceProviderIdentifier() {
        return serviceProviderIdentifier;
    }

    public void setServiceProviderIdentifier(String serviceProviderIdentifier) {
        this.serviceProviderIdentifier = serviceProviderIdentifier;
    }

    public String getServiceDeclarationIdentifier() {
        return serviceDeclarationIdentifier;
    }

    public void setServiceDeclarationIdentifier(String serviceDeclarationIdentifier) {
        this.serviceDeclarationIdentifier = serviceDeclarationIdentifier;
    }

    public String getServiceDeclarationName() {
        return serviceDeclarationName;
    }

    public void setServiceDeclarationName(String serviceDeclarationName) {
        this.serviceDeclarationName = serviceDeclarationName;
    }

    public String getServiceDeclarationDescription() {
        return serviceDeclarationDescription;
    }

    public void setServiceDeclarationDescription(String serviceDeclarationDescription) {
        this.serviceDeclarationDescription = serviceDeclarationDescription;
    }

    public String getTechnicalDescription() {
        return technicalDescription;
    }

    public void setTechnicalDescription(String technicalDescription) {
        this.technicalDescription = technicalDescription;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Long getConsentMaxDurationSeconds() {
        return consentMaxDurationSeconds;
    }

    public void setConsentMaxDurationSeconds(Long consentMaxDurationSeconds) {
        this.consentMaxDurationSeconds = consentMaxDurationSeconds;
    }

    public Long getMaxCacheSeconds() {
        return maxCacheSeconds;
    }

    public void setMaxCacheSeconds(Long maxCacheSeconds) {
        this.maxCacheSeconds = maxCacheSeconds;
    }

    public Boolean getNeedSignature() {
        return needSignature;
    }

    public void setNeedSignature(Boolean needSignature) {
        this.needSignature = needSignature;
    }

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
