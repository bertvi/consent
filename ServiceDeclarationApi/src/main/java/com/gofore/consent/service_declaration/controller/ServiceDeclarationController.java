package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.ServiceDeclarationApiService;
import com.gofore.consent.service_declaration.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceDeclarationController {

    private static final String RESPONSE_OK = "OK";

    @Autowired
    ServiceDeclarationApiService serviceDeclarationApiService;

    @GetMapping(path = "/listServiceDeclarations", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> listServiceDeclarations(@Valid @RequestBody ListServiceDeclarationRequest request) {
        List<ServiceDeclaration> declarations = serviceDeclarationApiService.findDeclarations(request);

        ListServiceDeclarationResponse response = ListServiceDeclarationResponse.builder()
                .serviceDeclarationIdentifier(request.getServiceDeclarationIdentifier())
                .serviceProviderIdentifier(request.getServiceProviderIdentifier())
                .declarations(declarations).build();

        return ResponseEntity.ok(response.toString());
    }

    @PostMapping(path = "/addServiceDeclaration", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addServiceDeclaration(@Valid @RequestBody AddServiceDeclarationRequest request) {
        serviceDeclarationApiService.save(request);

        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();

        return ResponseEntity.ok(response.toString());
    }

    @PutMapping(path = "/updateServiceDeclarationValidUntil", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateServiceDeclarationValidUntil(@Valid @RequestBody UpdateServiceDeclarationRequest request) {
        serviceDeclarationApiService.update(request);

        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();

        return ResponseEntity.ok(response.toString());
    }


}
