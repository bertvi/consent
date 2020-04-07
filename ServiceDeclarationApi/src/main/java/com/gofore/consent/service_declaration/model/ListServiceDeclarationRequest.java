package com.gofore.consent.service_declaration.model;

import java.io.Serializable;
import java.time.LocalDateTime;


public class ListServiceDeclarationRequest implements Serializable {

    private static final long serialVersionUID = 4049961366358846345L;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnicalDescription() {
        return technicalDescription;
    }

    public void setTechnicalDescription(String technicalDescription) {
        this.technicalDescription = technicalDescription;
    }

    public LocalDateTime getValidAt() {
        return validAt;
    }

    public void setValidAt(LocalDateTime validAt) {
        this.validAt = validAt;
    }

    public Boolean getDetails() {
        return details;
    }

    public void setDetails(Boolean details) {
        this.details = details;
    }

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
