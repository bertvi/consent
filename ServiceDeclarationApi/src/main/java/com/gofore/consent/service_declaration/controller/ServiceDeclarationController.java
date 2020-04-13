package com.gofore.consent.service_declaration.controller;

import com.gofore.consent.service_declaration.service.ServiceDeclarationApiService;
import com.gofore.consent.service_declaration.model.*;
import com.gofore.consent.service_declaration.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ServiceDeclarationController {

    private static final String RESPONSE_OK = "OK";

    @Autowired
    ServiceDeclarationApiService serviceDeclarationApiService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;


    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping(path = "/listServiceDeclarations", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> listServiceDeclarations(@Valid @RequestBody ListServiceDeclarationRequest request,
                                                     @Valid @RequestHeader String jwt) {
        List<ServiceDeclaration> declarations = serviceDeclarationApiService.findDeclarations(request);

        ListServiceDeclarationResponse response = ListServiceDeclarationResponse.builder()
                .serviceDeclarationIdentifier(request.getServiceDeclarationIdentifier())
                .serviceProviderIdentifier(request.getServiceProviderIdentifier())
                .declarations(declarations).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/addServiceDeclaration", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addServiceDeclaration(@Valid @RequestBody AddServiceDeclarationRequest request,
                                                   @Valid @RequestHeader String jwt) {
        serviceDeclarationApiService.save(request);

        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/updateServiceDeclarationValidUntil", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateServiceDeclarationValidUntil(@Valid @RequestBody UpdateServiceDeclarationRequest request,
                                                                @Valid @RequestHeader String jwt) {
        serviceDeclarationApiService.update(request);

        ServiceDeclarationResponse response = ServiceDeclarationResponse.builder().response(RESPONSE_OK).build();

        return ResponseEntity.ok(response);
    }


}
