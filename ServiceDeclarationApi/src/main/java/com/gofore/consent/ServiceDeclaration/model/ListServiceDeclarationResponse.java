package com.gofore.consent.ServiceDeclaration.model;

import java.io.Serializable;
import java.util.List;

public class ListServiceDeclarationResponse implements Serializable {

    private static final long serialVersionUID = 4049961576368846345L;

    public List<ServiceDeclaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<ServiceDeclaration> declarations) {
        this.declarations = declarations;
    }

    private List<ServiceDeclaration> declarations;


    public String getServiceProviderIdentifier() {
        return serviceProviderIdentifier;
    }

    public void setServiceProviderIdentifier(String serviceProviderIdentifier) {
        this.serviceProviderIdentifier = serviceProviderIdentifier;
    }

    private String serviceProviderIdentifier;


    public String getServiceDeclarationIdentifier() {
        return serviceDeclarationIdentifier;
    }

    public void setServiceDeclarationIdentifier(String serviceDeclarationIdentifier) {
        this.serviceDeclarationIdentifier = serviceDeclarationIdentifier;
    }

    private String serviceDeclarationIdentifier;

    @Override
    public String toString() {
        return "{\"declarations\":" + declarations
                + ", \"serviceProviderIdentifier\":\"" + serviceProviderIdentifier
                + "\",\"serviceDeclarationIdentifier\":\""
                + serviceDeclarationIdentifier + "\"}";
    }

}
