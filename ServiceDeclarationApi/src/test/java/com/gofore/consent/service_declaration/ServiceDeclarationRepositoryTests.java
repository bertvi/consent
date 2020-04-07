package com.gofore.consent.service_declaration;

import com.gofore.consent.service_declaration.model.ServiceDeclaration;
import com.gofore.consent.service_declaration.model.ServiceProvider;
import com.gofore.consent.service_declaration.repository.ServiceDeclarationRepository;
import com.gofore.consent.service_declaration.repository.ServiceProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
class ServiceDeclarationRepositoryTests {

    @Autowired
    ServiceDeclarationRepository serviceDeclarationRepository;

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Test
    public void testFindDeclarationByIdentifier() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        ServiceProvider newProvider = serviceProviderRepository.save(provider);
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration("TEST1",
                "Test declaration", "Test description", Boolean.TRUE, newProvider);
        serviceDeclarationRepository.save(declaration);
        declaration = serviceDeclarationRepository.findByIdentifier("TEST1");
        assertNotNull(declaration);
    }

    @Test
    public void testFindDeclarationByIdentifierNotFound() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        ServiceProvider newProvider = serviceProviderRepository.save(provider);
        ServiceDeclaration declaration = TestUtil.getInstance().createDeclaration("TEST1",
                "Test declaration", "Test description", Boolean.TRUE, newProvider);
        serviceDeclarationRepository.save(declaration);
        declaration = serviceDeclarationRepository.findByIdentifier("TEST2");
        assertNull(declaration);
    }

}
