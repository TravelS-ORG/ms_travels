CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. SCHEMA PLATFORM: Quản lý thông tin các doanh nghiệp thuê phần mềm (Tenants)
CREATE SCHEMA IF NOT EXISTS spacehub_platform;

CREATE TABLE IF NOT EXISTS spacehub_platform.tenants (
    id VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    domain VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, SUSPENDED, DELETED
    spacehub_created_ts TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    spacehub_updated_ts TIMESTAMPTZ NULL,
    
    CONSTRAINT pk_tenants PRIMARY KEY (id),
    CONSTRAINT uq_tenants_domain UNIQUE (domain)
);

-- 2. SCHEMA AUTH: Quản lý tài khoản người dùng và cô lập theo Tenant
CREATE SCHEMA IF NOT EXISTS spacehub_auth;

CREATE TABLE IF NOT EXISTS spacehub_auth.spacehub_user (
    id BIGSERIAL NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    spacehub_created_ts TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    spacehub_updated_ts TIMESTAMPTZ NULL,
    
    CONSTRAINT pk_spacehub_user PRIMARY KEY (id),
    CONSTRAINT fk_spacehub_user_tenant FOREIGN KEY (tenant_id) REFERENCES spacehub_platform.tenants(id) ON DELETE CASCADE,
    CONSTRAINT uq_spacehub_user_tenant_username UNIQUE (tenant_id, username),
    CONSTRAINT uq_spacehub_user_email UNIQUE (email)
);
CREATE INDEX IF NOT EXISTS idx_user_tenant ON spacehub_auth.spacehub_user(tenant_id);