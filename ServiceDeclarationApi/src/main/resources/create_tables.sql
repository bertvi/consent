DROP DATABASE IF EXISTS consent;

DROP USER IF EXISTS consent;
CREATE USER consent WITH PASSWORD '1234';

CREATE DATABASE consent WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE =
  'en_US.UTF-8' OWNER consent;

CREATE SCHEMA service_declaration_api;

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

CREATE SEQUENCE service_declaration_api.service_provider_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE service_declaration_api.service_provider_id_seq OWNED BY service_declaration_api.service_provider.id;

CREATE SEQUENCE service_declaration_api.service_declaration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE service_declaration_api.service_declaration_id_seq OWNED BY service_declaration_api.service_declaration.id;

CREATE INDEX idx_service_provider_identifier ON service_declaration_api.service_provider(identifier);
CREATE INDEX idx_service_declaration_identifier ON service_declaration_api.service_declaration(identifier);

ALTER TABLE service_declaration_api.service_provider OWNER TO consent;
ALTER TABLE service_declaration_api.service_declaration OWNER TO consent;
GRANT USAGE ON SCHEMA service_declaration_api TO consent;