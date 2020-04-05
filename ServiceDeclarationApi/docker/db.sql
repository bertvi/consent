CREATE DATABASE consent;
GRANT ALL PRIVILEGES ON DATABASE consent TO declaration_api;

CREATE SCHEMA service_declaration_api AUTHORIZATION declaration_api;

CREATE TABLE service_declaration_api.service_provider
(
    id bigserial PRIMARY KEY NOT NULL,
    identifier character varying(100)
);

CREATE TABLE service_declaration_api.service_declaration
(
    id bigserial PRIMARY KEY NOT NULL,
    identifier character varying(40),
    name character varying(100),
    description character varying(255),
    valid boolean,
    service_provider_id bigserial NOT NULL REFERENCES service_declaration_api.service_provider(id)
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
