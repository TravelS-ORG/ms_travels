CREATE SCHEMA IF NOT EXISTS spacehub_rbac;

CREATE TABLE IF NOT EXISTS spacehub_rbac.spacehub_role (
    id BIGSERIAL NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    role_name VARCHAR(50) NOT NULL, -- e.g., 'CRM_MANAGER', 'TENANT_ADMIN', 'TENANT_USER'
    description VARCHAR(200) NULL,

    CONSTRAINT pk_spacehub_role PRIMARY KEY (id),
    CONSTRAINT fk_spacehub_role_tenant FOREIGN KEY (tenant_id) REFERENCES spacehub_platform.tenants(id) ON DELETE CASCADE,
    CONSTRAINT uq_spacehub_role_tenant_name UNIQUE (tenant_id, role_name)
);

CREATE TABLE IF NOT EXISTS spacehub_rbac.spacehub_permission (
    id BIGSERIAL NOT NULL,
    permission_key VARCHAR(50) NOT NULL, -- e.g., 'BOOKING_CREATE', 'RESOURCE_LOCK'
    description VARCHAR(200) NULL,

    CONSTRAINT pk_spacehub_permission PRIMARY KEY (id),
    CONSTRAINT uq_spacehub_permission_key UNIQUE (permission_key)
);

CREATE TABLE IF NOT EXISTS spacehub_rbac.spacehub_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    CONSTRAINT pk_spacehub_role_permission PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_spacehub_role_permission_role FOREIGN KEY (role_id) REFERENCES spacehub_rbac.spacehub_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_spacehub_role_permission_permission FOREIGN KEY (permission_id) REFERENCES spacehub_rbac.spacehub_permission(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS spacehub_rbac.user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES spacehub_auth.spacehub_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES spacehub_rbac.spacehub_role(id) ON DELETE CASCADE
);