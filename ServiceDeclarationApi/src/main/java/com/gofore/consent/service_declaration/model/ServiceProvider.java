package com.gofore.consent.service_declaration.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "service_provider", schema = "service_declaration_api")
public class ServiceProvider implements Serializable {
    private static final long serialVersionUID = 4048798961366546485L;

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"identifier\":\"" + identifier + "\"}";
    }
}
