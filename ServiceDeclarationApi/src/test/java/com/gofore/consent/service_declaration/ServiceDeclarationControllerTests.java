package com.gofore.consent.service_declaration;

import com.gofore.consent.service_declaration.config.JwtTokenUtil;
import com.gofore.consent.service_declaration.exception.DuplicateDeclarationException;
import com.gofore.consent.service_declaration.exception.InvalidRequestException;
import com.gofore.consent.service_declaration.exception.TooBroadQueryException;
import com.gofore.consent.service_declaration.model.*;
import com.gofore.consent.service_declaration.service.JwtUserDetailsService;
import com.gofore.consent.service_declaration.service.ServiceDeclarationApiService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceDeclarationControllerTests {

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private ServiceDeclarationApiService serviceDeclarationApiService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Value("${jwt.secret}")
    private String secret;

    private static final String HOST = "http://localhost";

    private static final String AUTH_ENDPOINT = "/api/auth";

    private static final String LIST_DECLARATIONS_ENDPOINT = "/api/listServiceDeclarations";

    private static final String ADD_DECLARATION_ENDPOINT = "/api/addServiceDeclaration";

    private static final String UPDATE_DECLARATION_ENDPOINT = "/api/updateServiceDeclarationValidUntil";

    private static final String JWT_HEADER = "Jwt";

    private static final String JWT_TOKEN = "SecretJwt";

    private static final String PASSWORD = "password";

    @ParameterizedTest
    @MethodSource("provideArgumentsForListDeclarations")
    void testListServiceDeclarations(List<ServiceDeclaration> declarations,
                                     ServiceProvider provider,
                                     int expectedStatus,
                                     String expectedMessage,
                                     boolean willThrow,
                                     boolean mockJwt,
                                     Class exception) throws Exception {

        if (mockJwt) {

            if (willThrow) {
                given(serviceDeclarationApiService.findDeclarations(any())).willThrow(exception);
            } else {
                given(serviceDeclarationApiService.findDeclarations(any())).willReturn(declarations);
            }

            mockJwt();
            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            ResponseEntity<String> result = this.restTemplate.postForEntity(new URI(HOST + ":" + randomServerPort + LIST_DECLARATIONS_ENDPOINT),
                    new HttpEntity<>(createListServiceDeclarationRequest(declarations.get(0).getIdentifier(),
                            provider.getIdentifier()), headers), String.class);

            String content = result.getBody();
            if (willThrow) {
                assertEquals(expectedMessage, content);
            } else {
                ListServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ListServiceDeclarationResponse.class);
                assertEquals(response.getServiceProviderIdentifier(), provider.getIdentifier());
                assertEquals(response.getServiceDeclarationIdentifier(), declarations.get(0).getIdentifier());
                assertEquals(response.getDeclarations().size(), declarations.size());
            }
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(1)).findDeclarations(any());

        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            ResponseEntity<String> result = this.restTemplate.postForEntity(new URI(HOST + ":" + randomServerPort + LIST_DECLARATIONS_ENDPOINT),
                    new HttpEntity<>(createListServiceDeclarationRequest(declarations.get(0).getIdentifier(),
                            provider.getIdentifier()), headers), String.class);
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(0)).findDeclarations(any());

        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForAddDeclaration")
    void testAddServiceDeclaration(ServiceDeclaration declaration,
                                   ServiceProvider provider,
                                   int expectedStatus,
                                   String expectedMessage,
                                   boolean willThrow,
                                   boolean mockJwt,
                                   Class exception) throws Exception {
        if (mockJwt) {

            if (willThrow) {
                given(serviceDeclarationApiService.save(any())).willThrow(exception);
            } else {
                given(serviceDeclarationApiService.save(any())).willReturn(declaration);
            }

            mockJwt();
            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            ResponseEntity<String> result = this.restTemplate.postForEntity(new URI(HOST + ":" + randomServerPort + ADD_DECLARATION_ENDPOINT),
                    new HttpEntity<>(createAddServiceDeclarationRequest(declaration.getIdentifier(),
                            provider.getIdentifier()), headers), String.class);

            String content = result.getBody();
            if (willThrow) {
                assertEquals(expectedMessage, content);
            } else {
                ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);
                assertEquals(expectedMessage, response.getResponse());
            }
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(1)).save(any());

        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            ResponseEntity<String> result = this.restTemplate.postForEntity(new URI(HOST + ":" + randomServerPort + ADD_DECLARATION_ENDPOINT),
                    new HttpEntity<>(createAddServiceDeclarationRequest(declaration.getIdentifier(),
                            provider.getIdentifier()), headers), String.class);
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(0)).save(any());

        }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUpdateDeclaration")
    void testUpdateServiceDeclarationValidUntil(ServiceDeclaration declaration,
                                                ServiceProvider provider,
                                                int expectedStatus,
                                                String expectedMessage,
                                                boolean willThrow,
                                                boolean mockJwt,
                                                Class exception) throws Exception {

        if (mockJwt) {

            if (willThrow) {
                given(serviceDeclarationApiService.update(any())).willThrow(exception);
            } else {
                given(serviceDeclarationApiService.update(any())).willReturn(declaration);
            }
            mockJwt();
            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            HttpEntity<UpdateServiceDeclarationRequest> entity = new HttpEntity<>(createUpdateServiceDeclarationRequest(declaration.getIdentifier(),
                    provider.getIdentifier()), headers);
            String url = HOST + ":" + randomServerPort + UPDATE_DECLARATION_ENDPOINT;
            ResponseEntity<String> result = this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            String content = result.getBody();
            if (willThrow) {
                assertEquals(expectedMessage, content);
            } else {
                ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);
                assertEquals(expectedMessage, response.getResponse());
            }
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(1)).update(any());

        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set(JWT_HEADER, JWT_TOKEN);
            HttpEntity<UpdateServiceDeclarationRequest> entity = new HttpEntity<>(createUpdateServiceDeclarationRequest(declaration.getIdentifier(),
                    provider.getIdentifier()), headers);
            String url = HOST + ":" + randomServerPort + UPDATE_DECLARATION_ENDPOINT;
            ResponseEntity<String> result = this.restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            assertEquals(expectedStatus, result.getStatusCodeValue());
            verify(serviceDeclarationApiService, times(0)).update(any());

        }
    }

    private static Stream<Arguments> provideArgumentsForListDeclarations() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");

        List<ServiceDeclaration> declarations = new ArrayList<>();
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("{\"message\":\"Invalid request\",\"details\":[null]}");
        expectedMessages.add("{\"message\":\"Too broad query\",\"details\":[null]}");

        return Stream.of(
                Arguments.of(Arrays.asList(declarations.get(0), declarations.get(1)), provider, 200, null, false, true, null),
                Arguments.of(Arrays.asList(declarations.get(2), declarations.get(3)), provider, 200, null, false, true, null),
                Arguments.of(declarations, provider, 200, null, false, true, null),
                Arguments.of(declarations, provider, 400, expectedMessages.get(0), true, true, InvalidRequestException.class),
                Arguments.of(declarations, provider, 400, expectedMessages.get(1), true, true, TooBroadQueryException.class),
                Arguments.of(Arrays.asList(declarations.get(0), declarations.get(1)), provider, 401, null, false, false, null),
                Arguments.of(declarations, provider, 401, expectedMessages.get(0), true, false, InvalidRequestException.class)
        );
    }

    private static Stream<Arguments> provideArgumentsForAddDeclaration() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        List<ServiceDeclaration> declarations = new ArrayList<>();
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("OK");
        expectedMessages.add("{\"message\":\"Invalid request\",\"details\":[null]}");
        expectedMessages.add("{\"message\":\"Duplicate declaration\",\"details\":[null]}");

        return Stream.of(
                Arguments.of(declarations.get(0), provider, 200, expectedMessages.get(0), false, true, null),
                Arguments.of(declarations.get(1), provider, 200, expectedMessages.get(0), false, true, null),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(1), true, true, InvalidRequestException.class),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(2), true, true, DuplicateDeclarationException.class),
                Arguments.of(declarations.get(0), provider, 401, expectedMessages.get(0), false, false, null),
                Arguments.of(declarations.get(0), provider, 401, expectedMessages.get(1), true, false, InvalidRequestException.class)
        );
    }

    private static Stream<Arguments> provideArgumentsForUpdateDeclaration() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        List<ServiceDeclaration> declarations = new ArrayList<>();
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));
        declarations.add(TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider));

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("OK");
        expectedMessages.add("{\"message\":\"Invalid request\",\"details\":[null]}");

        return Stream.of(
                Arguments.of(declarations.get(0), provider, 200, expectedMessages.get(0), false, true, null),
                Arguments.of(declarations.get(1), provider, 200, expectedMessages.get(0), false, true, null),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(1), true, true, InvalidRequestException.class),
                Arguments.of(declarations.get(0), provider, 401, expectedMessages.get(0), false, false, null),
                Arguments.of(declarations.get(0), provider, 401, expectedMessages.get(1), true, false, InvalidRequestException.class)
        );
    }

    private void mockJwt() throws URISyntaxException {
        given(jwtUserDetailsService.loadUserByUsername(any())).willReturn(new User(secret,
                "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>()));
        given(jwtTokenUtil.generateToken(any())).willReturn(JWT_TOKEN);
        given(jwtTokenUtil.getUsernameFromToken(any())).willReturn(secret);
        given(jwtTokenUtil.validateToken(any(), any())).willReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(JWT_HEADER, JWT_TOKEN);
        this.restTemplate.postForEntity(new URI(HOST + ":" + randomServerPort + AUTH_ENDPOINT),
                new HttpEntity<>(JwtRequest.builder().username(secret).password(PASSWORD).build(), headers), String.class);
    }

    private ListServiceDeclarationRequest createListServiceDeclarationRequest(String declarationIdentifier,
                                                                              String providerIdentifier) {
        return ListServiceDeclarationRequest
                .builder().serviceDeclarationIdentifier(declarationIdentifier)
                .serviceProviderIdentifier(providerIdentifier).details(Boolean.TRUE)
                .description("description").name("name").technicalDescription("technical").build();
    }

    private AddServiceDeclarationRequest createAddServiceDeclarationRequest(String declarationIdentifier,
                                                                            String providerIdentifier) {
        return AddServiceDeclarationRequest.builder()
                .serviceDeclarationDescription(declarationIdentifier)
                .serviceProviderIdentifier(providerIdentifier)
                .serviceDeclarationName("name")
                .technicalDescription("description")
                .consentMaxDurationSeconds(60L)
                .maxCacheSeconds(60L)
                .needSignature(Boolean.TRUE)
                .validUntil(LocalDateTime.now()).build();
    }

    private UpdateServiceDeclarationRequest createUpdateServiceDeclarationRequest(String declarationIdentifier,
                                                                                  String providerIdentifier) {
        return UpdateServiceDeclarationRequest.builder()
                .serviceDeclarationIdentifier(declarationIdentifier)
                .serviceProviderIdentifier(providerIdentifier)
                .validUntil(LocalDateTime.now()).build();
    }

}
