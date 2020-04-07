CREATE DATABASE consent;
GRANT ALL PRIVILEGES ON DATABASE consent TO declaration_api;

\connect consent;

CREATE SCHEMA service_declaration_api AUTHORIZATION declaration_api;

CREATE TABLE service_declaration_api.service_provider
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    identifier CHARACTER VARYING(100) NOT NULL
);

CREATE TABLE service_declaration_api.service_declaration
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    identifier CHARACTER VARYING(40) NOT NULL,
    name CHARACTER VARYING(100),
    description CHARACTER VARYING(255),
    valid BOOLEAN NOT NULL,
    service_provider_id BIGSERIAL NOT NULL REFERENCES service_declaration_api.service_provider(id)
);

CREATE SEQUENCE IF NOT EXISTS service_declaration_api.service_provider_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE service_declaration_api.service_provider_id_seq OWNED BY service_declaration_api.service_provider.id;

CREATE SEQUENCE IF NOT EXISTS service_declaration_api.service_declaration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE service_declaration_api.service_declaration_id_seq OWNED BY service_declaration_api.service_declaration.id;

CREATE INDEX idx_service_provider_identifier ON service_declaration_api.service_provider(identifier);
CREATE INDEX idx_service_declaration_identifier ON service_declaration_api.service_declaration(identifier);
CREATE INDEX idx_service_provider ON service_declaration_api.service_provider(id);
CREATE INDEX idx_service_declaration ON service_declaration_api.service_declaration(id);
CREATE INDEX idx_service_declaration_service_provider_id ON service_declaration_api.service_declaration(service_provider_id);