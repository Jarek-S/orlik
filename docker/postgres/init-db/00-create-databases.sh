#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Database for application
    SELECT 'CREATE DATABASE "$APP_DB_NAME"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$APP_DB_NAME')\gexec

    -- Database for Keycloak
    SELECT 'CREATE DATABASE "$KC_DB_NAME"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$KC_DB_NAME')\gexec

    GRANT ALL PRIVILEGES ON DATABASE "$APP_DB_NAME" TO "$POSTGRES_USER";
    GRANT ALL PRIVILEGES ON DATABASE "$KC_DB_NAME" TO "$POSTGRES_USER";
EOSQL