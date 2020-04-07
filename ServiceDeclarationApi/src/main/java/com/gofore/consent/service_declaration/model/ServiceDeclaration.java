package com.gofore.consent.service_declaration.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "service_declaration", schema = "service_declaration_api")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter @Setter
public class ServiceDeclaration implements Serializable {

    private static final long serialVersionUID = 4049961366368846485L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length = 40)
    private String identifier;

    @Column(nullable=false, length = 100)
    private String name;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private Boolean valid;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider provider;

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"identifier\":\"" + identifier + "\",\"name\":\""
                + name + "\",\"description\":\"" + description + "\",\"valid\":"
                + valid + ",\"provider\":" + provider + "}";
    }
}
