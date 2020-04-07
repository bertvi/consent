package com.gofore.consent.service_declaration.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "service_declaration", schema = "service_declaration_api")
public class ServiceDeclaration implements Serializable {

    private static final long serialVersionUID = 4049961366368846485L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String identifier;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private Boolean valid;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider provider;

    public Long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public void setProvider(ServiceProvider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"identifier\":\"" + identifier + "\",\"name\":\""
                + name + "\",\"description\":\"" + description + "\",\"valid\":"
                + valid + ",\"provider\":" + provider + "}";
    }
}
