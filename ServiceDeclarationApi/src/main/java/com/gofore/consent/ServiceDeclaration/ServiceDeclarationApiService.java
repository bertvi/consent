package com.gofore.consent.ServiceDeclaration;

import com.gofore.consent.ServiceDeclaration.exception.DuplicateDeclarationException;
import com.gofore.consent.ServiceDeclaration.exception.InvalidRequestException;
import com.gofore.consent.ServiceDeclaration.exception.TooBroadQueryException;
import com.gofore.consent.ServiceDeclaration.model.*;
import com.gofore.consent.ServiceDeclaration.repository.ServiceDeclarationRepository;
import com.gofore.consent.ServiceDeclaration.repository.ServiceProviderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceDeclarationApiService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceDeclarationRepository serviceDeclarationRepository;

    public ServiceProvider findProviderByIdentifier(String identifier) {
        return serviceProviderRepository.findByIdentifier(identifier);
    }

    public List<ServiceDeclaration> findDeclarations(ListServiceDeclarationRequest request) throws InvalidRequestException, TooBroadQueryException {
        List<ServiceDeclaration> declarations;
        ServiceProvider provider;
        if (providerExists(request.getServiceProviderIdentifier())) {
            provider = findProviderByIdentifier(request.getServiceProviderIdentifier());
            declarations = serviceDeclarationRepository.findByProvider(provider);
        } else {
            throw new InvalidRequestException("The provider with identifier " + request.getServiceProviderIdentifier() + " does not exist");
        }

        boolean tooBroadQuery = request.getDescription().isEmpty()
                && request.getTechnicalDescription().isEmpty()
                && request.getName().isEmpty();

        if (tooBroadQuery) {
            throw new TooBroadQueryException("Consent Service refuses to fulfill the request/too many responses, please add more specific conditions and try again");
        }

        return declarations;
    }

    public Boolean providerExists(String identifier) {
        ServiceProvider provider = serviceProviderRepository.findByIdentifier(identifier);
        return provider != null;
    }

    public Boolean declarationExists(String identifier) {
        ServiceDeclaration declaration = serviceDeclarationRepository.findByIdentifier(identifier);
        return declaration != null;
    }

    public ServiceDeclaration save(AddServiceDeclarationRequest request) throws DuplicateDeclarationException, InvalidRequestException {
        ServiceProvider provider;
        if (providerExists(request.getServiceProviderIdentifier())) {
            provider = findProviderByIdentifier(request.getServiceProviderIdentifier());
        } else {
            ServiceProvider newProvider = new ServiceProvider();
            newProvider.setIdentifier(request.getServiceProviderIdentifier());
            provider = serviceProviderRepository.save(newProvider);
        }

        if (declarationExists(request.getServiceDeclarationIdentifier())) {
            throw new DuplicateDeclarationException("There already exists a declaration with identifier: " +
                    request.getServiceDeclarationIdentifier());
        }

        if (request.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("The validUntil of this declaration request is in the past");
        }

        if (request.getMaxCacheSeconds().longValue() < 0) {
            throw new InvalidRequestException("MaxCacheSeconds of service declaration must be positive");
        }

        ServiceDeclaration declaration = new ServiceDeclaration();
        declaration.setIdentifier(request.getServiceDeclarationIdentifier());
        declaration.setName(request.getServiceDeclarationName());
        declaration.setDescription(request.getServiceDeclarationDescription());
        declaration.setValid(Boolean.TRUE);
        declaration.setProvider(provider);

        return serviceDeclarationRepository.save(declaration);
    }

    public ServiceDeclaration update(UpdateServiceDeclarationRequest request) throws InvalidRequestException {
        if (!declarationExists(request.getServiceDeclarationIdentifier())) {
            throw new InvalidRequestException("The declaration with identifier " + request.getServiceDeclarationIdentifier() + " does not exist");
        }
        if (request.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("The validUntil of this declaration request is in the past");
        }

        ServiceDeclaration declaration = serviceDeclarationRepository.findByIdentifier(request.getServiceDeclarationIdentifier());
        declaration.setValid(Boolean.TRUE);
        return serviceDeclarationRepository.save(declaration);
    }

}
