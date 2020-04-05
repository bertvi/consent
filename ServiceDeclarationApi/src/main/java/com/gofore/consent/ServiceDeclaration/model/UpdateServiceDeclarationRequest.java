package com.gofore.consent.ServiceDeclaration.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UpdateServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049964376368846345L;

    private String serviceProviderIdentifier;

    private String serviceDeclarationIdentifier;

    private LocalDateTime validUntil;

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

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String toString() {
        return "{\"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentfier\":\"" + serviceDeclarationIdentifier
                + "\",\"validUntil\":\""
                + validUntil + "\"}";
    }
}
