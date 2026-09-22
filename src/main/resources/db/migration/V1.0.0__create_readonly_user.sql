CREATE SCHEMA IF NOT EXISTS travels_data;
CREATE SCHEMA IF NOT EXISTS travels_audit;

DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'travels_readonly') THEN
    CREATE ROLE travels_readonly;
  END IF;
END$$;

GRANT USAGE ON SCHEMA travels_data TO travels_readonly;
GRANT USAGE ON SCHEMA travels_audit TO travels_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA travels_data GRANT SELECT ON TABLES TO travels_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA travels_audit GRANT SELECT ON TABLES TO travels_readonly;