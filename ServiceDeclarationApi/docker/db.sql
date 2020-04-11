CREATE DATABASE consent;
GRANT ALL PRIVILEGES ON DATABASE consent TO service_declaration;

\connect consent;

CREATE SCHEMA service_declaration_api AUTHORIZATION service_declaration;
CREATE USER service_declaration_app WITH ENCRYPTED PASSWORD 'service_declaration_app';
GRANT USAGE ON SCHEMA service_declaration_api TO service_declaration_app;

CREATE TABLE service_declaration_api.service_provider
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    identifier VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE service_declaration_api.service_declaration
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    identifier VARCHAR(40) UNIQUE NOT NULL,
    name VARCHAR(100),
    description VARCHAR(255),
    valid BOOLEAN NOT NULL,
    service_provider_id BIGSERIAL NOT NULL
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

ALTER TABLE service_declaration_api.service_declaration
    ADD CONSTRAINT service_declaration_foreign_key
        FOREIGN KEY (service_provider_id) REFERENCES service_declaration_api.service_provider (id);

CREATE INDEX idx_service_provider_identifier ON service_declaration_api.service_provider(identifier);
CREATE INDEX idx_service_declaration_identifier ON service_declaration_api.service_declaration(identifier);
CREATE INDEX idx_service_declaration_service_provider_id ON service_declaration_api.service_declaration(service_provider_id);