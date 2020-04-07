package com.gofore.consent.service_declaration;

import com.gofore.consent.service_declaration.model.ServiceProvider;
import com.gofore.consent.service_declaration.repository.ServiceProviderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
class ServiceProviderRepositoryTests {

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Test
    public void testFindProviderByIdentifier() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        serviceProviderRepository.save(provider);
        provider = serviceProviderRepository.findByIdentifier("Test provider");
        assertNotNull(provider);
    }

    @Test
    public void testFindProviderByIdentifierNotFound() {
        ServiceProvider provider = TestUtil.getInstance().createProvider("Test provider");
        serviceProviderRepository.save(provider);
        provider = serviceProviderRepository.findByIdentifier("Test provider2");
        Assert.assertNull(provider);
    }

}
