-- ============================================================
--  Seed mínimo para pruebas de integración (H2)
--  Se ejecuta con @Sql(scripts = "/seed-test.sql")
--  Es seguro ejecutarlo múltiples veces (usa DELETE al inicio).
-- ============================================================

-- Limpieza previa para idempotencia
DELETE FROM USUARIO_ROL;
DELETE FROM ROL_PERMISO;
DELETE FROM PERMISO;
DELETE FROM ROL;
DELETE FROM USUARIO;

-- ============================================================
--  PERMISOS (IDs explícitos porque Hibernate no genera
--  secuencias para INSERT directo con create-drop en H2)
-- ============================================================
INSERT INTO PERMISO (ID_PERMISO, NOMBRE, DESCRIPCION, RECURSO, ACCION, ACTIVO)
VALUES
(1, 'CITA_CREAR',    'Crear citas',          'CITAS',    'CREAR',    1),
(2, 'CITA_VER',      'Ver citas',            'CITAS',    'VER',      1),
(3, 'CITA_CANCELAR', 'Cancelar citas',       'CITAS',    'CANCELAR', 1),
(4, 'CITA_EDITAR',   'Editar citas',         'CITAS',    'EDITAR',   1),
(5, 'USUARIO_VER',   'Ver usuarios',         'USUARIOS', 'VER',      1),
(6, 'REPORTE_VER',   'Ver reportes',         'REPORTES', 'VER',      1);

-- ============================================================
--  ROLES
-- ============================================================
INSERT INTO ROL (ID_ROL, NOMBRE, DESCRIPCION, ACTIVO)
VALUES
(1, 'ADMINISTRADOR', 'Administrador del sistema', 1),
(2, 'RECEPCIONISTA', 'Recepcionista', 1),
(3, 'MEDICO',        'Médico', 1),
(4, 'PACIENTE',      'Paciente', 1);

-- ============================================================
--  ROL_PERMISO (ADMIN → todos, resto → específicos)
-- ============================================================
INSERT INTO ROL_PERMISO (ID_ROL, ID_PERMISO) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
INSERT INTO ROL_PERMISO (ID_ROL, ID_PERMISO) VALUES (2,1),(2,2),(2,3),(2,4);
INSERT INTO ROL_PERMISO (ID_ROL, ID_PERMISO) VALUES (3,2),(3,4);
INSERT INTO ROL_PERMISO (ID_ROL, ID_PERMISO) VALUES (4,1),(4,2);

-- ============================================================
--  USUARIOS (password = "password" en BCrypt)
-- ============================================================
INSERT INTO USUARIO (ID_USUARIO, USERNAME, PASSWORD_HASH, EMAIL, ACTIVO, INTENTOS_FALLIDOS, BLOQUEADO, FECHA_CREACION)
VALUES
(1, 'admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@trinidad.com',    1, 0, 0, '2026-07-03T00:00:00'),
(2, 'recepcion','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'recepcion@trinidad.com',1, 0, 0, '2026-07-03T00:00:00'),
(3, 'medico1',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'medico1@trinidad.com',  1, 0, 0, '2026-07-03T00:00:00'),
(4, 'paciente1','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'paciente1@trinidad.com',1, 0, 0, '2026-07-03T00:00:00');

-- ============================================================
--  USUARIO_ROL
-- ============================================================
INSERT INTO USUARIO_ROL (ID_USUARIO, ID_ROL) VALUES
(1,1),  -- admin → ADMINISTRADOR
(2,2),  -- recepcion → RECEPCIONISTA
(3,3),  -- medico1 → MEDICO
(4,4);  -- paciente1 → PACIENTE
