package com.gofore.consent.service_declaration;

import com.gofore.consent.service_declaration.exception.DuplicateDeclarationException;
import com.gofore.consent.service_declaration.exception.InvalidRequestException;
import com.gofore.consent.service_declaration.exception.TooBroadQueryException;
import com.gofore.consent.service_declaration.model.*;
import com.gofore.consent.service_declaration.repository.ServiceDeclarationRepository;
import com.gofore.consent.service_declaration.repository.ServiceProviderRepository;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceDeclarationApiService {

    private static final Logger log = LoggerFactory.getLogger(ServiceDeclarationApiService.class);

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceDeclarationRepository serviceDeclarationRepository;

    public Optional<ServiceProvider> findProviderByIdentifier(String identifier) {
        return Optional.ofNullable(serviceProviderRepository.findByIdentifier(identifier));
    }

    public Optional<ServiceDeclaration> findDeclarationByIdentifier(String identifier) {
        return Optional.ofNullable(serviceDeclarationRepository.findByIdentifier(identifier));
    }

    public boolean checkDatabaseConnection() {
        return Integer.valueOf(1).equals(serviceProviderRepository.checkConnection());
    }

    public List<ServiceDeclaration> findDeclarations(ListServiceDeclarationRequest request) throws InvalidRequestException, TooBroadQueryException {
        List<ServiceDeclaration> declarations;

        log.info("Attempting to find declarations with request {}", request);

        boolean tooBroadQuery = request.getDescription().isEmpty()
                && request.getTechnicalDescription().isEmpty()
                && request.getName().isEmpty();

        if (tooBroadQuery) {
            log.error("Consent Service refuses to fulfill the request/too many responses, please add more specific conditions and try again");
            throw new TooBroadQueryException("Consent Service refuses to fulfill the request/too many responses, please add more specific conditions and try again");
        }

        Optional<ServiceProvider> foundProvider =findProviderByIdentifier(request.getServiceProviderIdentifier());
        if (foundProvider.isPresent()) {
            declarations = serviceDeclarationRepository.findByProvider(foundProvider.get());
        } else {
            log.error("The provider with identifier {} does not exist", request.getServiceProviderIdentifier());
            throw new InvalidRequestException("The provider with identifier " + request.getServiceProviderIdentifier() + " does not exist");
        }

        log.info("Found following declarations: {}", declarations);

        return declarations;
    }

    public ServiceDeclaration save(AddServiceDeclarationRequest request) throws DuplicateDeclarationException, InvalidRequestException {
        ServiceProvider provider;

        log.info("Attempting to save declaration with request {}", request);

        Optional<ServiceProvider> foundProvider =findProviderByIdentifier(request.getServiceProviderIdentifier());
        if (!foundProvider.isPresent()) {
            ServiceProvider newProvider = ServiceProvider.builder().identifier(request.getServiceProviderIdentifier()).build();
            provider = serviceProviderRepository.save(newProvider);
        } else {
            provider = foundProvider.get();
        }

        Optional<ServiceDeclaration> foundDeclaration = findDeclarationByIdentifier(request.getServiceDeclarationIdentifier());
        if (foundDeclaration.isPresent()) {
            log.error("There already exists a declaration with identifier: {}", request.getServiceDeclarationIdentifier());
            throw new DuplicateDeclarationException("There already exists a declaration with identifier: " +
                    request.getServiceDeclarationIdentifier());
        }

        if (request.getValidUntil().isBefore(LocalDateTime.now())) {
            log.error("The validUntil of this declaration is in the past");
            throw new InvalidRequestException("The validUntil of this declaration request is in the past");
        }

        if (request.getMaxCacheSeconds().longValue() < 0) {
            log.error("MaxCacheSeconds of service declaration must be positive");
            throw new InvalidRequestException("MaxCacheSeconds of service declaration must be positive");
        }

        ServiceDeclaration declaration = ServiceDeclaration.builder()
                .identifier(request.getServiceDeclarationIdentifier())
                .name(request.getServiceDeclarationName())
                .description(request.getServiceDeclarationDescription())
                .valid(Boolean.TRUE)
                .provider(provider).build();

        log.info("Saving declaration {} to db", declaration);

        return serviceDeclarationRepository.save(declaration);
    }

    public ServiceDeclaration update(UpdateServiceDeclarationRequest request) throws InvalidRequestException {
        log.info("Attempting to update declaration with request {}", request);

        Optional<ServiceDeclaration> foundDeclaration = findDeclarationByIdentifier(request.getServiceDeclarationIdentifier());
        if (!foundDeclaration.isPresent()) {
            log.error("The declaration with identifier {} does not exist", request.getServiceDeclarationIdentifier());
            throw new InvalidRequestException("The declaration with identifier " + request.getServiceDeclarationIdentifier() + " does not exist");
        }
        if (request.getValidUntil().isBefore(LocalDateTime.now())) {
            log.error("The validUntil of this declaration is in the past");
            throw new InvalidRequestException("The validUntil of this declaration request is in the past");
        }

        ServiceDeclaration declaration = serviceDeclarationRepository.findByIdentifier(request.getServiceDeclarationIdentifier());
        declaration.setValid(Boolean.TRUE);

        log.info("Updating declaration {} in db", declaration);

        return serviceDeclarationRepository.save(declaration);
    }

}
