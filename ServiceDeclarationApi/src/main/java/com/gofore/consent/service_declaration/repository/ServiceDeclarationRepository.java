package com.gofore.consent.service_declaration.repository;

import com.gofore.consent.service_declaration.model.ServiceDeclaration;
import com.gofore.consent.service_declaration.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceDeclarationRepository extends JpaRepository<ServiceDeclaration, Long> {

    @Query(value = "SELECT * FROM service_declaration_api.service_declaration WHERE identifier = ?1", nativeQuery = true)
    ServiceDeclaration findByIdentifier(@Param("identifier") String identifier);

    @Query(value = "SELECT * FROM service_declaration_api.service_declaration WHERE service_provider_id = ?1", nativeQuery = true)
    List<ServiceDeclaration> findByProvider(@Param("provider") ServiceProvider provider);

}