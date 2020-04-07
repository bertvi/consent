package com.gofore.consent.service_declaration.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "service_provider", schema = "service_declaration_api")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class ServiceProvider implements Serializable {
    private static final long serialVersionUID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length = 100)
    private String identifier;

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"identifier\":\"" + identifier + "\"}";
    }
}
