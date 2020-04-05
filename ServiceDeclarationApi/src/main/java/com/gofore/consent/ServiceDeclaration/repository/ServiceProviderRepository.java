package com.gofore.consent.ServiceDeclaration.repository;

import com.gofore.consent.ServiceDeclaration.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    @Query(value = "SELECT * FROM service_declaration_api.service_provider WHERE identifier = ?1", nativeQuery = true)
    ServiceProvider findByIdentifier(@Param("identifier") String identifier);

    @Query(value = "SELECT 1", nativeQuery = true)
    Integer checkConnection();

}