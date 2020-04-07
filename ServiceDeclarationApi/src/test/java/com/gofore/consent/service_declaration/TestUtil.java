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
        ServiceProvider provider = new ServiceProvider();
        provider.setIdentifier(identifier);

        return provider;
    }

    public ServiceDeclaration createDeclaration(String identifier, String name, String description, Boolean valid, ServiceProvider provider) {
        ServiceDeclaration declaration = new ServiceDeclaration();
        declaration.setProvider(provider);
        declaration.setIdentifier(identifier);
        declaration.setName(name);
        declaration.setDescription(description);
        declaration.setValid(valid);

        return declaration;
    }

    public ListServiceDeclarationRequest createListServiceDeclarationRequest(String description,
                                                                              Boolean details,
                                                                              String name,
                                                                              String declarationIdentifier,
                                                                              String providerIdentifier,
                                                                              String technicalDescription,
                                                                              LocalDateTime validAt) {
        ListServiceDeclarationRequest request = new ListServiceDeclarationRequest();
        request.setDescription(description);
        request.setDetails(details);
        request.setName(name);
        request.setServiceDeclarationIdentifier(declarationIdentifier);
        request.setServiceProviderIdentifier(providerIdentifier);
        request.setTechnicalDescription(technicalDescription);
        request.setValidAt(validAt);
        return request;
    }

    public AddServiceDeclarationRequest createAddServiceDeclarationRequest(Long consentMaxSeconds,
                                                                            Long cacheMaxSeconds,
                                                                            Boolean needSignature,
                                                                            LocalDateTime validUntil,
                                                                            String declarationDescription,
                                                                            String declarationIdentifier,
                                                                            String name,
                                                                            String providerIdentifier) {
        AddServiceDeclarationRequest request = new AddServiceDeclarationRequest();
        request.setConsentMaxDurationSeconds(consentMaxSeconds);
        request.setMaxCacheSeconds(cacheMaxSeconds);
        request.setNeedSignature(needSignature);
        request.setValidUntil(validUntil);
        request.setServiceDeclarationDescription(declarationDescription);
        request.setServiceDeclarationIdentifier(declarationIdentifier);
        request.setServiceDeclarationName(name);
        request.setServiceProviderIdentifier(providerIdentifier);
        return request;
    }

    public UpdateServiceDeclarationRequest createUpdateServiceDeclarationRequest(String declarationIdentifier,
                                                                                  String providerIdentifier,
                                                                                  LocalDateTime validUntil) {
        UpdateServiceDeclarationRequest request = new UpdateServiceDeclarationRequest();
        request.setServiceDeclarationIdentifier(declarationIdentifier);
        request.setServiceProviderIdentifier(providerIdentifier);
        request.setValidUntil(validUntil);
        return request;
    }

    public <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
