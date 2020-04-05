package com.gofore.consent.ServiceDeclaration.controller;

import com.gofore.consent.ServiceDeclaration.ServiceDeclarationApiService;
import com.gofore.consent.ServiceDeclaration.exception.DuplicateDeclarationException;
import com.gofore.consent.ServiceDeclaration.exception.InvalidRequestException;
import com.gofore.consent.ServiceDeclaration.exception.TooBroadQueryException;
import com.gofore.consent.ServiceDeclaration.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "Service declaration related services", value = "/api")
@RestController
@RequestMapping("/api")
public class ServiceDeclarationController {

    private static final String LIST_DECLARATIONS_EXAMPLE = ("{ \"serviceProviderIdentifier\": \"Test provider\", " +
            "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
            "\"name\": \"Test 1\", " +
            "\"description\": \"Test description\", " +
            "\"technicalDescription\": \"Openapi 3.0.0\", " +
            "\"validAt\": \"2020-04-23T18:25:43.511Z\", " +
            "\"details\": \"true\"}");

    private static final String ADD_DECLARATIONS_EXAMPLE = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
            "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
            "\"serviceDeclarationName\": \"Test name\", " +
            "\"serviceDeclarationDescription\": \"Test description\", " +
            "\"technicalDescription\": \"Openapi 3.0.0\", " +
            "\"validUntil\": \"2022-04-23T18:25:43.511Z\", " +
            "\"consentMaxDurationSeconds\": \"60\", " +
            "\"maxCacheSeconds\": \"60\", " +
            "\"needSignature\": \"false\"}");

    private static final String UPDATE_DECLARATIONS_EXAMPLE = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
            "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
            "\"validUntil\": \"2022-04-23T18:25:43.511Z\"}");

    @Autowired
    ServiceDeclarationApiService serviceDeclarationApiService;

    @ApiOperation("List service declarations")
    @GetMapping(path = "/listServiceDeclarations", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> listServiceDeclarations(
            @ApiParam(defaultValue = LIST_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody ListServiceDeclarationRequest request) {
        try {
            List<ServiceDeclaration> declarations = serviceDeclarationApiService.findDeclarations(request);
            ListServiceDeclarationResponse response = new ListServiceDeclarationResponse();
            response.setServiceDeclarationIdentifier(request.getServiceDeclarationIdentifier());
            response.setServiceProviderIdentifier(request.getServiceProviderIdentifier());
            response.setDeclarations(declarations);
            return ResponseEntity.ok(response.toString());
        } catch (TooBroadQueryException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (InvalidRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @ApiOperation("Add service declaration")
    @PostMapping(path = "/addServiceDeclaration", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addServiceDeclaration(
            @ApiParam(defaultValue = ADD_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody AddServiceDeclarationRequest request) {
        try {
            serviceDeclarationApiService.save(request);
            ServiceDeclarationResponse response = new ServiceDeclarationResponse();
            response.setResponse("OK");
            return ResponseEntity.ok(response.toString());
        } catch (DuplicateDeclarationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (InvalidRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ApiOperation("Update service declaration")
    @PutMapping(path = "/updateServiceDeclarationValidUntil", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateServiceDeclarationValidUntil(
            @ApiParam(defaultValue = UPDATE_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody UpdateServiceDeclarationRequest request) {
        try {
            serviceDeclarationApiService.update(request);
            ServiceDeclarationResponse response = new ServiceDeclarationResponse();
            response.setResponse("OK");
            return ResponseEntity.ok(response.toString());
        } catch (InvalidRequestException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
