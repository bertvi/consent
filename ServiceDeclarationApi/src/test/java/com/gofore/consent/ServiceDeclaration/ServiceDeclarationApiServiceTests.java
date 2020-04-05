package com.gofore.consent.ServiceDeclaration;

import com.gofore.consent.ServiceDeclaration.exception.DuplicateDeclarationException;
import com.gofore.consent.ServiceDeclaration.exception.InvalidRequestException;
import com.gofore.consent.ServiceDeclaration.exception.TooBroadQueryException;
import com.gofore.consent.ServiceDeclaration.model.*;
import com.gofore.consent.ServiceDeclaration.repository.ServiceDeclarationRepository;
import com.gofore.consent.ServiceDeclaration.repository.ServiceProviderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
class ServiceDeclarationApiServiceTests {

    @Autowired
    ServiceDeclarationApiService serviceDeclarationApiService;

    @Autowired
    ServiceDeclarationRepository serviceDeclarationRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Test
    public void testFindProviderByIdentifier() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TEST1");
        serviceProviderRepository.save(provider);
        provider = serviceDeclarationApiService.findProviderByIdentifier("TEST1");
        assertNotNull(provider);
    }

    @Test
    public void testFindProviderByIdentifierNotFound() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TEST1");
        serviceProviderRepository.save(provider);
        provider = serviceDeclarationApiService.findProviderByIdentifier("TEST2");
        assertNull(provider);
    }

    @Test
    public void testProviderExists() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        serviceProviderRepository.save(provider);
        Boolean exists = serviceDeclarationApiService.providerExists("TestProvider");
        Assert.assertTrue(exists);
    }

    @Test
    public void testProviderExistsFalse() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        serviceProviderRepository.save(provider);
        Boolean exists = serviceDeclarationApiService.providerExists("TestProvider2");
        Assert.assertFalse(exists);
    }

    @Test
    public void testDeclarationExists() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        ServiceProvider savedProvider = serviceProviderRepository.save(provider);
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, savedProvider);
        serviceDeclarationRepository.save(declaration);
        Boolean exists = serviceDeclarationApiService.declarationExists("TestDeclaration");
        Assert.assertTrue(exists);
    }

    @Test
    public void testDeclarationExistsFalse() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        ServiceProvider savedProvider = serviceProviderRepository.save(provider);
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, savedProvider);
        serviceDeclarationRepository.save(declaration);
        Boolean exists = serviceDeclarationApiService.declarationExists("TestDeclaration2");
        Assert.assertFalse(exists);
    }

    @Test
    public void testSave() throws InvalidRequestException, DuplicateDeclarationException {
        AddServiceDeclarationRequest request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                Boolean.TRUE, LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                "Declaration description", "TestDeclaration1",
                "Test name", "TestProvider1");
        serviceDeclarationApiService.save(request);
        ServiceDeclaration declaration = serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
        assertNotNull(declaration);
    }

    @Test
    public void testSaveWithInvalidRequestException() throws DuplicateDeclarationException {
        boolean thrown = false;

        try {
            AddServiceDeclarationRequest request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                    Boolean.TRUE, LocalDateTime.now(), "Declaration description", "TestDeclaration1",
                    "Test name", "TestProvider1");
            serviceDeclarationApiService.save(request);
            serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
        } catch (InvalidRequestException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testSaveWithDuplicateDeclarationException() throws InvalidRequestException {
        boolean thrown = false;

        try {
            AddServiceDeclarationRequest request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                    Boolean.TRUE, LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                    "Declaration description", "TestDeclaration1",
                    "Test name", "TestProvider1");
            serviceDeclarationApiService.save(request);
            request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                    Boolean.TRUE, LocalDateTime.now(), "Declaration description", "TestDeclaration1",
                    "Test name", "TestProvider1");
            serviceDeclarationApiService.save(request);
            serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
        } catch (DuplicateDeclarationException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testUpdate() throws DuplicateDeclarationException, InvalidRequestException {
        AddServiceDeclarationRequest request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                Boolean.TRUE, LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                "Declaration description", "TestDeclaration1",
                "Test name", "TestProvider1");
        serviceDeclarationApiService.save(request);
        ServiceDeclaration declarationBeforeUpdate = serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
        assertEquals(declarationBeforeUpdate.getValid(), Boolean.TRUE);
        UpdateServiceDeclarationRequest updateRequest = TestUtil.getInstance().createUpdateServiceDeclarationRequest("TestDeclaration1",
                "TestProvider1", LocalDateTime.of(2021, 1, 1, 0, 0, 0));
        serviceDeclarationApiService.update(updateRequest);
        ServiceDeclaration declarationAfterUpdate = serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
        assertEquals(declarationAfterUpdate.getValid(), Boolean.TRUE);
    }

    @Test
    public void testUpdateWithInvalidRequestException() throws DuplicateDeclarationException {
        boolean thrown = false;

        try {
            AddServiceDeclarationRequest request = TestUtil.getInstance().createAddServiceDeclarationRequest(60L, 60L,
                    Boolean.TRUE, LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                    "Declaration description", "TestDeclaration1",
                    "Test name", "TestProvider1");
            serviceDeclarationApiService.save(request);
            ServiceDeclaration declarationBeforeUpdate = serviceDeclarationRepository.findByIdentifier("TestDeclaration1");
            assertEquals(declarationBeforeUpdate.getValid(), Boolean.TRUE);
            UpdateServiceDeclarationRequest updateRequest = TestUtil.getInstance().createUpdateServiceDeclarationRequest("TestDeclaration1",
                    "TestProvider1", LocalDateTime.of(2020, 1, 1, 0, 0, 0));
            serviceDeclarationApiService.update(updateRequest);
        } catch (InvalidRequestException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testFindDeclarations() throws InvalidRequestException, TooBroadQueryException {
        ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
        ServiceProvider savedProvider = serviceProviderRepository.save(provider);
        ServiceDeclaration declarationOne = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                "Test description", Boolean.TRUE, savedProvider);
        ServiceDeclaration declarationTwo = TestUtil.getInstance().createDeclaration( "TestDeclaration2", "Test 2",
                "Test description2", Boolean.TRUE, savedProvider);
        serviceDeclarationRepository.save(declarationOne);
        serviceDeclarationRepository.save(declarationTwo);
        ListServiceDeclarationRequest request = TestUtil.getInstance().createListServiceDeclarationRequest("Test description",
                Boolean.TRUE, "Test name", "TestDeclaration", "TestProvider",
                "Technical description", LocalDateTime.now());
        List<ServiceDeclaration> foundDeclarations = serviceDeclarationApiService.findDeclarations(request);
        assertEquals(2, foundDeclarations.size());
    }

    @Test
    public void testFindDeclarationsWithInvalidRequestException() throws TooBroadQueryException {
        boolean thrown = false;

        try {
            ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
            ServiceProvider savedProvider = serviceProviderRepository.save(provider);
            ServiceDeclaration declarationOne = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                    "Test description", Boolean.TRUE, savedProvider);
            ServiceDeclaration declarationTwo = TestUtil.getInstance().createDeclaration( "TestDeclaration2", "Test 2",
                    "Test description2", Boolean.TRUE, savedProvider);
            serviceDeclarationRepository.save(declarationOne);
            serviceDeclarationRepository.save(declarationTwo);
            ListServiceDeclarationRequest request = TestUtil.getInstance().createListServiceDeclarationRequest("Test description",
                    Boolean.TRUE, "Test name", "TestDeclaration", "TestProvider2",
                    "Technical description", LocalDateTime.now());
            serviceDeclarationApiService.findDeclarations(request);
        } catch (InvalidRequestException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testFindDeclarationsWithTooBroadQueryException() throws InvalidRequestException {
        boolean thrown = false;

        try {
            ServiceProvider provider = TestUtil.getInstance().createProvider("TestProvider");
            ServiceProvider savedProvider = serviceProviderRepository.save(provider);
            ServiceDeclaration declarationOne = TestUtil.getInstance().createDeclaration( "TestDeclaration", "Test 1",
                    "Test description", Boolean.TRUE, savedProvider);
            ServiceDeclaration declarationTwo = TestUtil.getInstance().createDeclaration( "TestDeclaration2", "Test 2",
                    "Test description2", Boolean.TRUE, savedProvider);
            serviceDeclarationRepository.save(declarationOne);
            serviceDeclarationRepository.save(declarationTwo);
            ListServiceDeclarationRequest request = TestUtil.getInstance().createListServiceDeclarationRequest("",
                    Boolean.TRUE, "", "TestDeclaration", "TestProvider",
                    "", LocalDateTime.now());
            serviceDeclarationApiService.findDeclarations(request);
        } catch (TooBroadQueryException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
