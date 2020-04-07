package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.ServiceDeclarationApiService;
import com.gofore.consent.service_declaration.model.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "/api")
@RestController
@RequestMapping("/api")
public class ServiceDeclarationController {

    private static final String RESPONSE_OK = "OK";

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
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request valid, response body contains service declarations"),
            @ApiResponse(code = 400, message = "Request invalid, response body contains error description")
    })
    @GetMapping(path = "/listServiceDeclarations", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> listServiceDeclarations(
            @ApiParam(defaultValue = LIST_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody ListServiceDeclarationRequest request) {
        List<ServiceDeclaration> declarations = serviceDeclarationApiService.findDeclarations(request);
        ListServiceDeclarationResponse response = ListServiceDeclarationResponse.builder()
                .serviceDeclarationIdentifier(request.getServiceDeclarationIdentifier())
                .serviceProviderIdentifier(request.getServiceProviderIdentifier())
                .declarations(declarations).build();

        return ResponseEntity.ok(response.toString());
    }

    @ApiOperation("Add service declaration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request valid, response body is OK"),
            @ApiResponse(code = 400, message = "Request invalid, response body contains error description")
    })
    @PostMapping(path = "/addServiceDeclaration", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addServiceDeclaration(
            @ApiParam(defaultValue = ADD_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody AddServiceDeclarationRequest request) {
        serviceDeclarationApiService.save(request);
        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();
        return ResponseEntity.ok(response.toString());
    }

    @ApiOperation("Update service declaration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request valid, response body is OK"),
            @ApiResponse(code = 400, message = "Request invalid, response body contains error description")
    })
    @PutMapping(path = "/updateServiceDeclarationValidUntil", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateServiceDeclarationValidUntil(
            @ApiParam(defaultValue = UPDATE_DECLARATIONS_EXAMPLE, required = true)
            @Valid @RequestBody UpdateServiceDeclarationRequest request) {
        serviceDeclarationApiService.update(request);
        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();
        return ResponseEntity.ok(response.toString());
    }


}
