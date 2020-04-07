package com.gofore.consent.service_declaration;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofore.consent.service_declaration.model.*;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.time.LocalDateTime;

public class TestUtil {

    private static TestUtil instance = null;

    private TestUtil() {

    }

    public static TestUtil getInstance() {
        if (instance == null)
            instance = new TestUtil();

        return instance;
    }

    public ServiceProvider createProvider(String identifier) {
        return ServiceProvider.builder().identifier(identifier).build();
    }

    public ServiceDeclaration createDeclaration(String identifier, String name, String description, Boolean valid, ServiceProvider provider) {
        return ServiceDeclaration.builder()
                .provider(provider)
                .identifier(identifier)
                .name(name)
                .description(description)
                .valid(valid).build();
    }

    public ListServiceDeclarationRequest createListServiceDeclarationRequest(String description,
                                                                              Boolean details,
                                                                              String name,
                                                                              String declarationIdentifier,
                                                                              String providerIdentifier,
                                                                              String technicalDescription,
                                                                              LocalDateTime validAt) {
        return ListServiceDeclarationRequest.builder()
                .description(description)
                .details(details)
                .name(name)
                .serviceDeclarationIdentifier(declarationIdentifier)
                .serviceProviderIdentifier(providerIdentifier)
                .technicalDescription(technicalDescription)
                .validAt(validAt).build();
    }

    public AddServiceDeclarationRequest createAddServiceDeclarationRequest(Long consentMaxSeconds,
                                                                            Long cacheMaxSeconds,
                                                                            Boolean needSignature,
                                                                            LocalDateTime validUntil,
                                                                            String declarationDescription,
                                                                            String declarationIdentifier,
                                                                            String name,
                                                                            String providerIdentifier) {
        return AddServiceDeclarationRequest.builder()
                .consentMaxDurationSeconds(consentMaxSeconds)
                .maxCacheSeconds(cacheMaxSeconds)
                .needSignature(needSignature)
                .validUntil(validUntil)
                .serviceDeclarationDescription(declarationDescription)
                .serviceDeclarationIdentifier(declarationIdentifier)
                .serviceDeclarationName(name)
                .serviceProviderIdentifier(providerIdentifier).build();
    }

    public UpdateServiceDeclarationRequest createUpdateServiceDeclarationRequest(String declarationIdentifier,
                                                                                  String providerIdentifier,
                                                                                  LocalDateTime validUntil) {
        return UpdateServiceDeclarationRequest.builder()
                .serviceDeclarationIdentifier(declarationIdentifier)
                .serviceProviderIdentifier(providerIdentifier)
                .validUntil(validUntil).build();
    }

    public <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
