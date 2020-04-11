package com.gofore.consent.service_declaration;

import com.gofore.consent.service_declaration.exception.DuplicateDeclarationException;
import com.gofore.consent.service_declaration.exception.InvalidRequestException;
import com.gofore.consent.service_declaration.exception.TooBroadQueryException;
import com.gofore.consent.service_declaration.model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest
class ServiceDeclarationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceDeclarationApiService serviceDeclarationApiService;

    @ParameterizedTest
    @MethodSource("provideArgumentsForListDeclarations")
    void testListServiceDeclarations(List<ServiceDeclaration> declarations,
                                     ServiceProvider provider,
                                     int expectedStatus,
                                     String expectedMessage,
                                     boolean willThrow,
                                     Class exception) throws Exception {
        if (willThrow) {
            given(serviceDeclarationApiService.findDeclarations(any())).willThrow(exception);
        } else {
            given(serviceDeclarationApiService.findDeclarations(any())).willReturn(declarations);
        }

        String bodyContent = createBodyContentForListDeclarations(provider.getIdentifier(), declarations.get(0).getIdentifier());
        MvcResult mvcResult = this.mockMvc.perform(get("/api/listServiceDeclarations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        if (willThrow) {
            assertEquals(expectedMessage, content);
        } else {
            ListServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ListServiceDeclarationResponse.class);
            assertEquals(response.getServiceProviderIdentifier(), provider.getIdentifier());
            assertEquals(response.getServiceDeclarationIdentifier(), declarations.get(0).getIdentifier());
            assertEquals(response.getDeclarations().size(), declarations.size());
        }
        assertEquals(expectedStatus, mvcResult.getResponse().getStatus());
        verify(serviceDeclarationApiService, times(1)).findDeclarations(any());
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForAddDeclaration")
    void testAddServiceDeclaration(ServiceDeclaration declaration,
                                   ServiceProvider provider,
                                   int expectedStatus,
                                   String expectedMessage,
                                   boolean willThrow,
                                   Class exception) throws Exception {
        if (willThrow) {
            given(serviceDeclarationApiService.save(any())).willThrow(exception);
        } else {
            given(serviceDeclarationApiService.save(any())).willReturn(declaration);
        }

        String bodyContent = createBodyContentForAddDeclarations(provider.getIdentifier(), declaration.getIdentifier());
        MvcResult mvcResult = this.mockMvc.perform(post("/api/addServiceDeclaration")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        if (willThrow) {
            assertEquals(expectedMessage, content);
        } else {
            ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);
            assertEquals(expectedMessage, response.getResponse());
        }
        assertEquals(expectedStatus, mvcResult.getResponse().getStatus());
        verify(serviceDeclarationApiService, times(1)).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUpdateDeclaration")
    void testUpdateServiceDeclarationValidUntil(ServiceDeclaration declaration,
                                                ServiceProvider provider,
                                                int expectedStatus,
                                                String expectedMessage,
                                                boolean willThrow,
                                                Class exception) throws Exception {
        if (willThrow) {
            given(serviceDeclarationApiService.update(any())).willThrow(exception);
        } else {
            given(serviceDeclarationApiService.update(any())).willReturn(declaration);
        }

        String bodyContent = createBodyContentForUpdateDeclaration(provider.getIdentifier(), declaration.getIdentifier());
        MvcResult mvcResult = this.mockMvc.perform(put("/api/updateServiceDeclarationValidUntil")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        if (willThrow) {
            assertEquals(expectedMessage, content);
        } else {
            ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);
            assertEquals(expectedMessage, response.getResponse());
        }
        assertEquals(expectedStatus, mvcResult.getResponse().getStatus());
        verify(serviceDeclarationApiService, times(1)).update(any());
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
                Arguments.of(Arrays.asList(declarations.get(0), declarations.get(1)), provider, 200, null, false, null),
                Arguments.of(Arrays.asList(declarations.get(2), declarations.get(3)), provider, 200, null, false, null),
                Arguments.of(declarations, provider, 200, null, false, null),
                Arguments.of(declarations, provider, 400, expectedMessages.get(0), true, InvalidRequestException.class),
                Arguments.of(declarations, provider, 400, expectedMessages.get(1), true, TooBroadQueryException.class)
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
                Arguments.of(declarations.get(0), provider, 200, expectedMessages.get(0), false, null),
                Arguments.of(declarations.get(1), provider, 200, expectedMessages.get(0), false, null),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(1), true, InvalidRequestException.class),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(2), true, DuplicateDeclarationException.class)
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
                Arguments.of(declarations.get(0), provider, 200, expectedMessages.get(0), false, null),
                Arguments.of(declarations.get(1), provider, 200, expectedMessages.get(0), false, null),
                Arguments.of(declarations.get(0), provider, 400, expectedMessages.get(1), true, InvalidRequestException.class)
        );
    }

    private static String createBodyContentForListDeclarations(String providerIdentifier, String declarationIdentifier) {
        return ("{ \"serviceProviderIdentifier\": \"" + providerIdentifier + "\", " +
                "\"serviceDeclarationIdentifier\": \"" + declarationIdentifier + "\", " +
                "\"name\": \"Test 1\", " +
                "\"description\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validAt\": \"2020-04-23T18:25:43.511Z\", " +
                "\"details\": \"true\"}");
    }

    private static String createBodyContentForAddDeclarations(String providerIdentifier, String declarationIdentifier) {
        return ("{ \"serviceProviderIdentifier\": \"" + providerIdentifier + "\", " +
                "\"serviceDeclarationIdentifier\": \"" + declarationIdentifier + "\", " +
                "\"serviceDeclarationName\": \"Test name\", " +
                "\"serviceDeclarationDescription\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\", " +
                "\"consentMaxDurationSeconds\": \"60\", " +
                "\"maxCacheSeconds\": \"60\", " +
                "\"needSignature\": \"false\"}");
    }

    private static String createBodyContentForUpdateDeclaration(String providerIdentifier, String declarationIdentifier) {
        return ("{ \"serviceProviderIdentifier\": \"" + providerIdentifier + "\", " +
                "\"serviceDeclarationIdentifier\": \"" + declarationIdentifier + "\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\"}");
    }

}
