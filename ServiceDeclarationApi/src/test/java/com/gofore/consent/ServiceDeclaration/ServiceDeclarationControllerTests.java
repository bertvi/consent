package com.gofore.consent.ServiceDeclaration;

import com.gofore.consent.ServiceDeclaration.exception.InvalidRequestException;
import com.gofore.consent.ServiceDeclaration.model.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;

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



    @Test
    public void testListServiceDeclarationsSuccess() throws Exception {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        ServiceDeclaration declarationOne = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, provider);
        ServiceDeclaration declarationTwo = TestUtil.getInstance().createDeclaration( "TestDeclaration2", "Test 2",
                "Test description2", Boolean.TRUE, provider);

        ListServiceDeclarationRequest request = new ListServiceDeclarationRequest();
        request.setServiceProviderIdentifier(provider.getIdentifier());
        request.setServiceDeclarationIdentifier(declarationOne.getIdentifier());
        request.setValidAt(LocalDateTime.now());
        request.setTechnicalDescription("Openapi 3.0.0");
        request.setDetails(Boolean.TRUE);
        given(serviceDeclarationApiService.findDeclarations(any())).willReturn(Arrays.asList(declarationOne, declarationTwo));

        String bodyContent = ("{ \"serviceProviderIdentifier\": \"Test provider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"name\": \"Test 1\", " +
                "\"description\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validAt\": \"2020-04-23T18:25:43.511Z\", " +
                "\"details\": \"true\"}");

        MvcResult mvcResult = this.mockMvc.perform(get("/api/listServiceDeclarations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        ListServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ListServiceDeclarationResponse.class);

        assertEquals(200, status);
        assertEquals(2, response.getDeclarations().size());
        assertEquals("Test provider", response.getServiceProviderIdentifier());
        assertEquals("TestDeclaration", response.getServiceDeclarationIdentifier());
        verify(serviceDeclarationApiService, times(1)).findDeclarations(any());
    }

    @Test
    public void testListServiceDeclarationsException() throws Exception {
        given(serviceDeclarationApiService.findDeclarations(any())).willThrow(InvalidRequestException.class);

        String bodyContent = ("{ \"serviceProviderIdentifier\": \"Test provider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"name\": \"Test 1\", " +
                "\"description\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validAt\": \"2020-04-23T18:25:43.511Z\", " +
                "\"details\": \"true\"}");

        MvcResult mvcResult = this.mockMvc.perform(get("/api/listServiceDeclarations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(400, status);
        assertEquals("", content);
        verify(serviceDeclarationApiService, times(1)).findDeclarations(any());
    }

    @Test
    public void testAddServiceDeclarationSuccess() throws Exception {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration("TestDeclaration",
                "Test name",
                "Test description",
                Boolean.TRUE,
                provider);
        given(serviceDeclarationApiService.save(any())).willReturn(declaration);

        AddServiceDeclarationRequest request = new AddServiceDeclarationRequest();
        request.setServiceProviderIdentifier(provider.getIdentifier());
        request.setServiceDeclarationName(declaration.getName());
        request.setServiceDeclarationIdentifier(declaration.getIdentifier());
        request.setServiceDeclarationDescription(declaration.getDescription());
        request.setValidUntil(LocalDateTime.now());
        request.setNeedSignature(Boolean.FALSE);
        request.setMaxCacheSeconds(60L);
        request.setConsentMaxDurationSeconds(60L);
        request.setTechnicalDescription("Technical description");


        String bodyContent = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"serviceDeclarationName\": \"Test name\", " +
                "\"serviceDeclarationDescription\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\", " +
                "\"consentMaxDurationSeconds\": \"60\", " +
                "\"maxCacheSeconds\": \"60\", " +
                "\"needSignature\": \"false\"}");

        MvcResult mvcResult = this.mockMvc.perform(post("/api/addServiceDeclaration")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);

        assertEquals(200, status);
        assertEquals("OK", response.getResponse());
        verify(serviceDeclarationApiService, times(1)).save(any());

    }

    @Test
    public void testAddServiceDeclarationException() throws Exception {
        given(serviceDeclarationApiService.save(any())).willThrow(InvalidRequestException.class);

        String bodyContent = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"serviceDeclarationName\": \"Test name\", " +
                "\"serviceDeclarationDescription\": \"Test description\", " +
                "\"technicalDescription\": \"Openapi 3.0.0\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\", " +
                "\"consentMaxDurationSeconds\": \"60\", " +
                "\"maxCacheSeconds\": \"60\", " +
                "\"needSignature\": \"false\"}");

        MvcResult mvcResult = this.mockMvc.perform(post("/api/addServiceDeclaration")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(400, status);
        assertEquals("", content);
        verify(serviceDeclarationApiService, times(1)).save(any());

    }

    @Test
    public void testUpdateServiceDeclarationValidUntilSuccess() throws Exception {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration("TestDeclaration",
                "Test name",
                "Test description",
                Boolean.TRUE,
                provider);
        given(serviceDeclarationApiService.update(any())).willReturn(declaration);

        UpdateServiceDeclarationRequest request = new UpdateServiceDeclarationRequest();
        request.setServiceProviderIdentifier(provider.getIdentifier());
        request.setServiceDeclarationIdentifier(declaration.getIdentifier());
        request.setValidUntil(LocalDateTime.now());

        String bodyContent = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\"}");

        MvcResult mvcResult = this.mockMvc.perform(put("/api/updateServiceDeclarationValidUntil")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        ServiceDeclarationResponse response = TestUtil.getInstance().mapFromJson(content, ServiceDeclarationResponse.class);

        assertEquals(200, status);
        assertEquals("OK", response.getResponse());
        verify(serviceDeclarationApiService, times(1)).update(any());

    }

    @Test
    public void testUpdateServiceDeclarationValidUntilException() throws Exception {
        given(serviceDeclarationApiService.update(any())).willThrow(InvalidRequestException.class);

        String bodyContent = ("{ \"serviceProviderIdentifier\": \"TestProvider\", " +
                "\"serviceDeclarationIdentifier\": \"TestDeclaration\", " +
                "\"validUntil\": \"2022-04-23T18:25:43.511Z\"}");

        MvcResult mvcResult = this.mockMvc.perform(put("/api/updateServiceDeclarationValidUntil")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(400, status);
        assertEquals("", content);
        verify(serviceDeclarationApiService, times(1)).update(any());
    }

}
