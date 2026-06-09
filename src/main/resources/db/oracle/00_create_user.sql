-- ================================================================
--  PASO 1 DE 2 - Ejecutar como SYS o SYSTEM en SQL Developer
--  Crea el usuario TRINIDAD_DB con los permisos necesarios.
--  Solo se ejecuta UNA vez.
-- ================================================================

ALTER SESSION SET CONTAINER = XE;

-- Eliminar usuario si ya existe (para reinstalacion limpia)
BEGIN
    EXECUTE IMMEDIATE 'DROP USER TRINIDAD_DB CASCADE';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

CREATE USER TRINIDAD_DB IDENTIFIED BY "Trinidad2026"
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;

GRANT CONNECT, RESOURCE TO TRINIDAD_DB;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER,
      CREATE VIEW, CREATE PROCEDURE TO TRINIDAD_DB;

-- Verificar que se creo correctamente
SELECT USERNAME, ACCOUNT_STATUS FROM DBA_USERS WHERE USERNAME = 'TRINIDAD_DB';
