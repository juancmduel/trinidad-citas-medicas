# 🏥 PROYECTO: Sistema de Gestión de Citas Médicas — Trinidad & Especialidades Médicas S.A.C.

> **Documento maestro para GitHub Copilot / Copilot Chat en Visual Studio Code**
> Generado a partir del **Sílabo de Marcos de Desarrollo Web (100000S52T)** y del **Informe de Proyecto Trinidad**.
> **Stack obligatorio del curso (con Oracle como BD en lugar de MySQL):**
> Frontend: HTML5 + CSS3 + JavaScript + **Bootstrap 5** + **Thymeleaf**
> Backend: **Java 17 + Spring Boot 3.x + Spring Web + Spring Data JPA + Hibernate + Spring Security + JWT**
> Base de datos: **Oracle Database 21c XE** (con driver `ojdbc11`)
> Servidor: **Apache Tomcat 10** (embebido en Spring Boot)

---

## 📑 Tabla de contenido

1. [Contexto del negocio](#1-contexto-del-negocio)
2. [Alcance funcional (módulos a implementar)](#2-alcance-funcional)
3. [Arquitectura técnica y stack](#3-arquitectura-técnica)
4. [Estructura de carpetas del proyecto Spring Boot](#4-estructura-de-carpetas)
5. [Configuración: `pom.xml` y `application.properties`](#5-configuración)
6. [Modelo de datos — DDL Oracle completo](#6-modelo-de-datos-oracle)
7. [Datos maestros (DML semilla)](#7-datos-maestros)
8. [Mapeo Entidad ↔ Tabla ↔ Módulo del sílabo](#8-mapeo-entidad-tabla-modulo)
9. [Roadmap de implementación por unidad del sílabo](#9-roadmap-por-unidad-del-sílabo)
10. [Convenciones y prompts para Copilot](#10-convenciones-y-prompts-para-copilot)

---

## 1. Contexto del negocio

**Empresa:** Trinidad & Especialidades Médicas S.A.C. — RUC 20494088208 — Tarapoto, San Martín, Perú.
**Categoría MINSA:** Centro de Salud con Camas de Internamiento (Categoría I-4).

**Problema:** procesos manuales de admisión, citas e historias clínicas en papel, sin canal digital ni reportes en tiempo real.

**Objetivo del sistema:** automatizar admisión, programación de citas, historias clínicas electrónicas, pagos y dashboard gerencial para 15+ especialidades.

**Roles del sistema:** `PACIENTE`, `RECEPCIONISTA`, `MEDICO`, `ADMINISTRADOR`, `GERENTE`.

---

## 2. Alcance funcional

### Módulos del sistema (derivados de los RF-01 al RF-30 del informe)

| Módulo | Funciones clave | Requerimientos |
|---|---|---|
| **M1. Autenticación y Seguridad** | Login, logout, recuperación de contraseña, control por rol, JWT para API REST | RF-01, RF-02, RF-03 |
| **M2. Gestión de Pacientes** | CRUD pacientes, validación DNI único, edición desde portal | RF-04, RF-05, RF-06 |
| **M3. Gestión de Médicos y Horarios** | CRUD médicos, asignación de especialidad y CMP, horarios por día | RF-21, RF-22, RF-23 |
| **M4. Catálogo de Especialidades** | CRUD especialidades, precio de consulta | RF-24 |
| **M5. Gestión de Citas** | Disponibilidad en tiempo real, agendar 24/7, confirmación, recordatorio, cancelación, no-show automático | RF-07 a RF-12 |
| **M6. Admisión / Check-in** | Check-in por QR/DNI, asignación de turno y consultorio | RF-13, RF-14 |
| **M7. Pagos** | Cobro en línea o caja, comprobante electrónico | RF-15, RF-16 |
| **M8. Historia Clínica Electrónica** | Consulta HC, registro de atención (motivo, anamnesis, examen, diagnóstico CIE-10, tratamiento), recetas DIGEMID, órdenes de exámenes | RF-17 a RF-20 |
| **M9. Reportes y Dashboard** | KPIs en tiempo real, reportes mensuales, exportación PDF/Excel | RF-25, RF-26, RF-27 |
| **M10. Auditoría y Notificaciones** | Logs de acciones críticas, portal del paciente con citas y resultados | RF-28, RF-29, RF-30 |

---

## 3. Arquitectura técnica

### Patrón
**Multicapa (n-tier) + MVC + REST** dentro de un monolito Spring Boot:

```
┌─────────────────────────────────────────────────────┐
│  Capa Presentación                                  │
│  ─ Thymeleaf + Bootstrap 5 (vistas server-side)     │
│  ─ JavaScript (fetch a endpoints REST)              │
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│  Capa Controladores                                 │
│  ─ @Controller (Thymeleaf MVC)                      │
│  ─ @RestController (API JSON, protegida con JWT)    │
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│  Capa Servicios (lógica de negocio)                 │
│  ─ @Service + @Transactional                        │
│  ─ Validaciones (Spring Validator + Bean Validation)│
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│  Capa Repositorios (persistencia)                   │
│  ─ Spring Data JPA + Hibernate                      │
│  ─ JpaRepository<Entity, ID>                        │
└─────────────────────────────────────────────────────┘
                         │
┌─────────────────────────────────────────────────────┐
│  Oracle Database 21c XE                             │
│  ─ Esquema: TRINIDAD_DB                             │
│  ─ Tablas + Secuencias + Triggers de auditoría      │
└─────────────────────────────────────────────────────┘

Transversal: Spring Security + JWT + BCrypt
```

### Versiones de referencia

| Componente | Versión |
|---|---|
| Java JDK | 17 (LTS) |
| Spring Boot | 3.2.x |
| Spring Security | 6.x |
| Hibernate | 6.x (incluido en Spring Boot 3) |
| Driver Oracle | `ojdbc11` 23.x |
| Bootstrap | 5.3.x (vía CDN o webjars) |
| Thymeleaf | 3.1.x |
| jjwt (JWT) | 0.12.x |
| Maven | 3.9+ |

---

## 4. Estructura de carpetas

```
trinidad-citas-medicas/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/trinidad/citas/
    │   │       │
    │   │       ├── TrinidadCitasApplication.java       # @SpringBootApplication
    │   │       │
    │   │       ├── config/
    │   │       │   ├── SecurityConfig.java             # Spring Security + JWT filter
    │   │       │   ├── WebMvcConfig.java               # Recursos estáticos, interceptores
    │   │       │   ├── JwtAuthFilter.java              # Filtro JWT
    │   │       │   ├── JwtService.java                 # Generación/validación JWT
    │   │       │   └── PasswordEncoderConfig.java      # BCryptPasswordEncoder
    │   │       │
    │   │       ├── model/                              # @Entity (JPA)
    │   │       │   ├── Usuario.java
    │   │       │   ├── Rol.java
    │   │       │   ├── UsuarioRol.java
    │   │       │   ├── Paciente.java
    │   │       │   ├── Medico.java
    │   │       │   ├── Especialidad.java
    │   │       │   ├── HorarioMedico.java
    │   │       │   ├── Cita.java
    │   │       │   ├── EstadoCita.java                 # enum
    │   │       │   ├── HistoriaClinica.java
    │   │       │   ├── Atencion.java
    │   │       │   ├── DiagnosticoCie10.java
    │   │       │   ├── Receta.java
    │   │       │   ├── DetalleReceta.java
    │   │       │   ├── OrdenExamen.java
    │   │       │   ├── Pago.java
    │   │       │   └── AuditoriaLog.java
    │   │       │
    │   │       ├── repository/                         # extends JpaRepository
    │   │       │   ├── UsuarioRepository.java
    │   │       │   ├── RolRepository.java
    │   │       │   ├── PacienteRepository.java
    │   │       │   ├── MedicoRepository.java
    │   │       │   ├── EspecialidadRepository.java
    │   │       │   ├── HorarioMedicoRepository.java
    │   │       │   ├── CitaRepository.java
    │   │       │   ├── HistoriaClinicaRepository.java
    │   │       │   ├── AtencionRepository.java
    │   │       │   ├── RecetaRepository.java
    │   │       │   ├── PagoRepository.java
    │   │       │   └── AuditoriaLogRepository.java
    │   │       │
    │   │       ├── service/                            # @Service
    │   │       │   ├── AuthService.java
    │   │       │   ├── UsuarioService.java
    │   │       │   ├── PacienteService.java
    │   │       │   ├── MedicoService.java
    │   │       │   ├── EspecialidadService.java
    │   │       │   ├── HorarioMedicoService.java
    │   │       │   ├── CitaService.java                # lógica de disponibilidad y reglas RN-07..RN-12
    │   │       │   ├── HistoriaClinicaService.java
    │   │       │   ├── AtencionService.java
    │   │       │   ├── RecetaService.java
    │   │       │   ├── PagoService.java
    │   │       │   ├── ReporteService.java             # KPIs y dashboards
    │   │       │   └── NotificacionService.java        # email/SMS (mock al inicio)
    │   │       │
    │   │       ├── controller/
    │   │       │   ├── web/                            # @Controller + Thymeleaf
    │   │       │   │   ├── HomeController.java
    │   │       │   │   ├── LoginController.java
    │   │       │   │   ├── PacienteWebController.java
    │   │       │   │   ├── MedicoWebController.java
    │   │       │   │   ├── CitaWebController.java
    │   │       │   │   ├── AtencionWebController.java
    │   │       │   │   ├── DashboardController.java
    │   │       │   │   └── PortalPacienteController.java
    │   │       │   │
    │   │       │   └── api/                            # @RestController + JWT
    │   │       │       ├── AuthRestController.java
    │   │       │       ├── PacienteRestController.java
    │   │       │       ├── CitaRestController.java
    │   │       │       ├── DisponibilidadRestController.java
    │   │       │       └── ReporteRestController.java
    │   │       │
    │   │       ├── dto/                                # objetos de transferencia
    │   │       │   ├── LoginRequest.java
    │   │       │   ├── LoginResponse.java
    │   │       │   ├── PacienteDTO.java
    │   │       │   ├── CitaDTO.java
    │   │       │   ├── DisponibilidadDTO.java
    │   │       │   ├── AtencionDTO.java
    │   │       │   └── KpiDashboardDTO.java
    │   │       │
    │   │       ├── exception/
    │   │       │   ├── ResourceNotFoundException.java
    │   │       │   ├── BusinessException.java
    │   │       │   ├── DuplicateResourceException.java
    │   │       │   └── GlobalExceptionHandler.java     # @ControllerAdvice
    │   │       │
    │   │       └── util/
    │   │           ├── DateUtils.java
    │   │           ├── QrCodeGenerator.java
    │   │           └── PdfExporter.java
    │   │
    │   └── resources/
    │       ├── application.properties                  # config Oracle, JWT, etc.
    │       ├── application-dev.properties
    │       ├── application-prod.properties
    │       ├── data.sql                                # opcional: carga inicial
    │       │
    │       ├── static/
    │       │   ├── css/
    │       │   │   └── trinidad.css
    │       │   ├── js/
    │       │   │   ├── app.js
    │       │   │   ├── citas.js
    │       │   │   └── dashboard.js
    │       │   └── img/
    │       │       └── logo-trinidad.png
    │       │
    │       └── templates/                              # Thymeleaf
    │           ├── fragments/
    │           │   ├── header.html
    │           │   ├── footer.html
    │           │   └── navbar.html
    │           ├── auth/
    │           │   ├── login.html
    │           │   └── recuperar.html
    │           ├── pacientes/
    │           │   ├── lista.html
    │           │   ├── form.html
    │           │   └── detalle.html
    │           ├── medicos/
    │           │   ├── lista.html
    │           │   ├── form.html
    │           │   └── horarios.html
    │           ├── citas/
    │           │   ├── agendar.html
    │           │   ├── mis-citas.html
    │           │   └── calendario.html
    │           ├── atenciones/
    │           │   ├── consultorio.html
    │           │   └── historia-clinica.html
    │           ├── dashboard/
    │           │   └── index.html
    │           ├── portal/
    │           │   ├── home.html
    │           │   └── perfil.html
    │           ├── error/
    │           │   ├── 404.html
    │           │   └── 500.html
    │           └── index.html
    │
    └── test/
        └── java/
            └── com/trinidad/citas/
                ├── service/
                │   ├── CitaServiceTest.java
                │   └── PacienteServiceTest.java
                └── controller/
                    └── AuthControllerTest.java

db/
├── 01_schema.sql        # DDL completo (ver sección 6)
├── 02_seed.sql          # datos maestros (ver sección 7)
├── 03_indexes.sql       # índices opcionales
└── 04_views.sql         # vistas para reportes
```

---

## 5. Configuración

### `pom.xml` (dependencias mínimas obligatorias)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>

    <groupId>com.trinidad</groupId>
    <artifactId>trinidad-citas-medicas</artifactId>
    <version>1.0.0</version>
    <name>Trinidad Citas Medicas</name>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Web + Thymeleaf (Unidad 2) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <!-- Spring Data JPA + Validación (Unidad 3) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Oracle JDBC (en lugar de MySQL) -->
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc11</artifactId>
            <version>23.3.0.23.09</version>
        </dependency>

        <!-- Spring Security + JWT (Unidad 4) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity6</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Bootstrap vía WebJars (alternativa a CDN) -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>5.3.3</version>
        </dependency>

        <!-- Lombok (opcional pero muy recomendado) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### `src/main/resources/application.properties`

```properties
# ============================================
# APLICACIÓN
# ============================================
spring.application.name=trinidad-citas-medicas
server.port=8080

# ============================================
# ORACLE DATABASE 21c XE
# ============================================
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XE
spring.datasource.username=TRINIDAD_DB
spring.datasource.password=Trinidad2026
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# Pool de conexiones HikariCP
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000

# ============================================
# JPA / HIBERNATE
# ============================================
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=validate
# Opciones: validate (recomendado en prod), update (dev), none, create, create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.default_schema=TRINIDAD_DB
spring.jpa.open-in-view=false

# ============================================
# THYMELEAF
# ============================================
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML

# ============================================
# JWT
# ============================================
trinidad.jwt.secret=CAMBIAR_ESTA_CLAVE_MUY_LARGA_Y_SEGURA_EN_PROD_2026
trinidad.jwt.expiration-ms=3600000
# 1 hora

# ============================================
# LOGGING
# ============================================
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE
logging.file.name=logs/trinidad.log
```

---

## 6. Modelo de datos Oracle

> **Esquema:** `TRINIDAD_DB`
> **Convenciones:** nombres de tabla en mayúsculas y singular cuando representa una entidad fuerte. PK con secuencia `SEQ_<TABLA>` y trigger `TRG_<TABLA>_BI` (before insert) para autoincremento. Nombres de FK con prefijo `FK_<HIJA>_<PADRE>`.

### 6.1. Script de creación de usuario y esquema (ejecutar como `SYS` o `SYSTEM`)

```sql
-- =========================================================
-- CREACIÓN DE USUARIO / ESQUEMA TRINIDAD_DB
-- Ejecutar conectado como SYS o SYSTEM al PDB XE
-- =========================================================
ALTER SESSION SET CONTAINER = XE;

CREATE USER TRINIDAD_DB IDENTIFIED BY "Trinidad2026"
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;

GRANT CONNECT, RESOURCE TO TRINIDAD_DB;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER,
      CREATE VIEW, CREATE PROCEDURE TO TRINIDAD_DB;
```

### 6.2. DDL: tablas, secuencias y triggers

> **Conectarse ahora como `TRINIDAD_DB`** y ejecutar el siguiente script (`db/01_schema.sql`).

```sql
-- =========================================================
-- TRINIDAD & ESPECIALIDADES MÉDICAS S.A.C.
-- Sistema de Gestión de Citas Médicas
-- DDL Oracle 21c XE
-- =========================================================
SET DEFINE OFF;

-- ---------------------------------------------------------
-- 1) TABLA ROL
-- ---------------------------------------------------------
CREATE TABLE ROL (
    ID_ROL        NUMBER(10)        NOT NULL,
    NOMBRE        VARCHAR2(30 CHAR) NOT NULL,
    DESCRIPCION   VARCHAR2(200 CHAR),
    ACTIVO        NUMBER(1)         DEFAULT 1 NOT NULL,
    CONSTRAINT PK_ROL          PRIMARY KEY (ID_ROL),
    CONSTRAINT UQ_ROL_NOMBRE   UNIQUE (NOMBRE),
    CONSTRAINT CK_ROL_ACTIVO   CHECK (ACTIVO IN (0, 1))
);

CREATE SEQUENCE SEQ_ROL START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_ROL_BI
BEFORE INSERT ON ROL
FOR EACH ROW
WHEN (NEW.ID_ROL IS NULL)
BEGIN
    :NEW.ID_ROL := SEQ_ROL.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 2) TABLA USUARIO (autenticación)
-- ---------------------------------------------------------
CREATE TABLE USUARIO (
    ID_USUARIO       NUMBER(10)         NOT NULL,
    USERNAME         VARCHAR2(50 CHAR)  NOT NULL,
    PASSWORD_HASH    VARCHAR2(255 CHAR) NOT NULL,   -- BCrypt
    EMAIL            VARCHAR2(120 CHAR) NOT NULL,
    ACTIVO           NUMBER(1)          DEFAULT 1 NOT NULL,
    INTENTOS_FALLIDOS NUMBER(3)         DEFAULT 0 NOT NULL,
    BLOQUEADO        NUMBER(1)          DEFAULT 0 NOT NULL,
    FECHA_CREACION   TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    FECHA_ULT_LOGIN  TIMESTAMP,
    CONSTRAINT PK_USUARIO         PRIMARY KEY (ID_USUARIO),
    CONSTRAINT UQ_USUARIO_USER    UNIQUE (USERNAME),
    CONSTRAINT UQ_USUARIO_EMAIL   UNIQUE (EMAIL),
    CONSTRAINT CK_USUARIO_ACTIVO  CHECK (ACTIVO IN (0, 1)),
    CONSTRAINT CK_USUARIO_BLOQ    CHECK (BLOQUEADO IN (0, 1))
);

CREATE SEQUENCE SEQ_USUARIO START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_USUARIO_BI
BEFORE INSERT ON USUARIO
FOR EACH ROW
WHEN (NEW.ID_USUARIO IS NULL)
BEGIN
    :NEW.ID_USUARIO := SEQ_USUARIO.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 3) TABLA USUARIO_ROL (N:M)
-- ---------------------------------------------------------
CREATE TABLE USUARIO_ROL (
    ID_USUARIO   NUMBER(10) NOT NULL,
    ID_ROL       NUMBER(10) NOT NULL,
    CONSTRAINT PK_USUARIO_ROL PRIMARY KEY (ID_USUARIO, ID_ROL),
    CONSTRAINT FK_UR_USUARIO  FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO) ON DELETE CASCADE,
    CONSTRAINT FK_UR_ROL      FOREIGN KEY (ID_ROL)     REFERENCES ROL (ID_ROL)
);

-- ---------------------------------------------------------
-- 4) TABLA PACIENTE
-- ---------------------------------------------------------
CREATE TABLE PACIENTE (
    ID_PACIENTE        NUMBER(10)         NOT NULL,
    ID_USUARIO         NUMBER(10),                            -- nullable: pacientes sin acceso web
    DNI                VARCHAR2(8 CHAR)   NOT NULL,
    NOMBRES            VARCHAR2(80 CHAR)  NOT NULL,
    APELLIDO_PATERNO   VARCHAR2(50 CHAR)  NOT NULL,
    APELLIDO_MATERNO   VARCHAR2(50 CHAR),
    FECHA_NACIMIENTO   DATE               NOT NULL,
    SEXO               CHAR(1)            NOT NULL,            -- M / F
    TELEFONO           VARCHAR2(15 CHAR),
    EMAIL              VARCHAR2(120 CHAR),
    DIRECCION          VARCHAR2(200 CHAR),
    DISTRITO           VARCHAR2(80 CHAR),
    TIPO_SANGRE        VARCHAR2(5 CHAR),                       -- O+, A-, etc.
    ALERGIAS           VARCHAR2(500 CHAR),
    ACTIVO             NUMBER(1)          DEFAULT 1 NOT NULL,
    FECHA_REGISTRO     TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_PACIENTE        PRIMARY KEY (ID_PACIENTE),
    CONSTRAINT UQ_PACIENTE_DNI    UNIQUE (DNI),                -- RN-02
    CONSTRAINT FK_PAC_USUARIO     FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT CK_PACIENTE_SEXO   CHECK (SEXO IN ('M', 'F')),
    CONSTRAINT CK_PACIENTE_DNI    CHECK (LENGTH(DNI) = 8 AND REGEXP_LIKE(DNI, '^[0-9]+$')),
    CONSTRAINT CK_PACIENTE_ACTIVO CHECK (ACTIVO IN (0, 1))
);

CREATE SEQUENCE SEQ_PACIENTE START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_PACIENTE_BI
BEFORE INSERT ON PACIENTE
FOR EACH ROW
WHEN (NEW.ID_PACIENTE IS NULL)
BEGIN
    :NEW.ID_PACIENTE := SEQ_PACIENTE.NEXTVAL;
END;
/

CREATE INDEX IDX_PACIENTE_NOMBRES ON PACIENTE (APELLIDO_PATERNO, APELLIDO_MATERNO, NOMBRES);

-- ---------------------------------------------------------
-- 5) TABLA ESPECIALIDAD
-- ---------------------------------------------------------
CREATE TABLE ESPECIALIDAD (
    ID_ESPECIALIDAD   NUMBER(10)         NOT NULL,
    NOMBRE            VARCHAR2(80 CHAR)  NOT NULL,
    DESCRIPCION       VARCHAR2(300 CHAR),
    PRECIO_CONSULTA   NUMBER(10, 2)      NOT NULL,
    DURACION_MINUTOS  NUMBER(3)          DEFAULT 20 NOT NULL,   -- RN-09
    ACTIVO            NUMBER(1)          DEFAULT 1 NOT NULL,
    CONSTRAINT PK_ESPECIALIDAD       PRIMARY KEY (ID_ESPECIALIDAD),
    CONSTRAINT UQ_ESPECIALIDAD_NOMB  UNIQUE (NOMBRE),
    CONSTRAINT CK_ESPEC_PRECIO       CHECK (PRECIO_CONSULTA >= 0),
    CONSTRAINT CK_ESPEC_DURACION     CHECK (DURACION_MINUTOS BETWEEN 5 AND 240),
    CONSTRAINT CK_ESPEC_ACTIVO       CHECK (ACTIVO IN (0, 1))
);

CREATE SEQUENCE SEQ_ESPECIALIDAD START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_ESPECIALIDAD_BI
BEFORE INSERT ON ESPECIALIDAD
FOR EACH ROW
WHEN (NEW.ID_ESPECIALIDAD IS NULL)
BEGIN
    :NEW.ID_ESPECIALIDAD := SEQ_ESPECIALIDAD.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 6) TABLA MEDICO
-- ---------------------------------------------------------
CREATE TABLE MEDICO (
    ID_MEDICO          NUMBER(10)         NOT NULL,
    ID_USUARIO         NUMBER(10)         NOT NULL,
    ID_ESPECIALIDAD    NUMBER(10)         NOT NULL,
    CMP                VARCHAR2(15 CHAR)  NOT NULL,             -- Colegio Médico del Perú
    NOMBRES            VARCHAR2(80 CHAR)  NOT NULL,
    APELLIDO_PATERNO   VARCHAR2(50 CHAR)  NOT NULL,
    APELLIDO_MATERNO   VARCHAR2(50 CHAR),
    DNI                VARCHAR2(8 CHAR)   NOT NULL,
    TELEFONO           VARCHAR2(15 CHAR),
    EMAIL              VARCHAR2(120 CHAR),
    CONSULTORIO        VARCHAR2(20 CHAR),
    ACTIVO             NUMBER(1)          DEFAULT 1 NOT NULL,
    CONSTRAINT PK_MEDICO         PRIMARY KEY (ID_MEDICO),
    CONSTRAINT UQ_MEDICO_CMP     UNIQUE (CMP),
    CONSTRAINT UQ_MEDICO_DNI     UNIQUE (DNI),
    CONSTRAINT FK_MED_USUARIO    FOREIGN KEY (ID_USUARIO)      REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT FK_MED_ESPEC      FOREIGN KEY (ID_ESPECIALIDAD) REFERENCES ESPECIALIDAD (ID_ESPECIALIDAD),
    CONSTRAINT CK_MEDICO_DNI     CHECK (LENGTH(DNI) = 8 AND REGEXP_LIKE(DNI, '^[0-9]+$')),
    CONSTRAINT CK_MEDICO_ACTIVO  CHECK (ACTIVO IN (0, 1))
);

CREATE SEQUENCE SEQ_MEDICO START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_MEDICO_BI
BEFORE INSERT ON MEDICO
FOR EACH ROW
WHEN (NEW.ID_MEDICO IS NULL)
BEGIN
    :NEW.ID_MEDICO := SEQ_MEDICO.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 7) TABLA HORARIO_MEDICO
-- ---------------------------------------------------------
CREATE TABLE HORARIO_MEDICO (
    ID_HORARIO       NUMBER(10)        NOT NULL,
    ID_MEDICO        NUMBER(10)        NOT NULL,
    DIA_SEMANA       NUMBER(1)         NOT NULL,           -- 1=Lunes ... 7=Domingo
    HORA_INICIO      VARCHAR2(5 CHAR)  NOT NULL,           -- 'HH24:MI'
    HORA_FIN         VARCHAR2(5 CHAR)  NOT NULL,
    ACTIVO           NUMBER(1)         DEFAULT 1 NOT NULL,
    CONSTRAINT PK_HORARIO_MEDICO   PRIMARY KEY (ID_HORARIO),
    CONSTRAINT FK_HOR_MEDICO       FOREIGN KEY (ID_MEDICO) REFERENCES MEDICO (ID_MEDICO) ON DELETE CASCADE,
    CONSTRAINT CK_HOR_DIA          CHECK (DIA_SEMANA BETWEEN 1 AND 7),
    CONSTRAINT CK_HOR_ACTIVO       CHECK (ACTIVO IN (0, 1)),
    CONSTRAINT UQ_HORARIO_UNICO    UNIQUE (ID_MEDICO, DIA_SEMANA, HORA_INICIO)
);

CREATE SEQUENCE SEQ_HORARIO_MEDICO START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_HORARIO_MEDICO_BI
BEFORE INSERT ON HORARIO_MEDICO
FOR EACH ROW
WHEN (NEW.ID_HORARIO IS NULL)
BEGIN
    :NEW.ID_HORARIO := SEQ_HORARIO_MEDICO.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 8) TABLA HISTORIA_CLINICA (1:1 con paciente)
-- ---------------------------------------------------------
CREATE TABLE HISTORIA_CLINICA (
    ID_HISTORIA       NUMBER(10)         NOT NULL,
    ID_PACIENTE       NUMBER(10)         NOT NULL,
    NRO_HISTORIA      VARCHAR2(15 CHAR)  NOT NULL,
    FECHA_APERTURA    DATE               DEFAULT SYSDATE NOT NULL,
    OBSERVACIONES     VARCHAR2(1000 CHAR),
    ACTIVO            NUMBER(1)          DEFAULT 1 NOT NULL,
    CONSTRAINT PK_HISTORIA_CLINICA   PRIMARY KEY (ID_HISTORIA),
    CONSTRAINT UQ_HC_PACIENTE        UNIQUE (ID_PACIENTE),
    CONSTRAINT UQ_HC_NRO             UNIQUE (NRO_HISTORIA),
    CONSTRAINT FK_HC_PACIENTE        FOREIGN KEY (ID_PACIENTE) REFERENCES PACIENTE (ID_PACIENTE) ON DELETE CASCADE,
    CONSTRAINT CK_HC_ACTIVO          CHECK (ACTIVO IN (0, 1))
);

CREATE SEQUENCE SEQ_HISTORIA_CLINICA START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_HISTORIA_CLINICA_BI
BEFORE INSERT ON HISTORIA_CLINICA
FOR EACH ROW
WHEN (NEW.ID_HISTORIA IS NULL)
BEGIN
    :NEW.ID_HISTORIA := SEQ_HISTORIA_CLINICA.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 9) TABLA CITA
-- ---------------------------------------------------------
CREATE TABLE CITA (
    ID_CITA            NUMBER(10)         NOT NULL,
    ID_PACIENTE        NUMBER(10)         NOT NULL,
    ID_MEDICO          NUMBER(10)         NOT NULL,
    ID_ESPECIALIDAD    NUMBER(10)         NOT NULL,
    FECHA_CITA         DATE               NOT NULL,
    HORA_INICIO        VARCHAR2(5 CHAR)   NOT NULL,            -- 'HH24:MI'
    HORA_FIN           VARCHAR2(5 CHAR)   NOT NULL,
    ESTADO             VARCHAR2(20 CHAR)  DEFAULT 'PROGRAMADA' NOT NULL,
        -- Valores: PROGRAMADA, CONFIRMADA, EN_ATENCION, ATENDIDA, CANCELADA, NO_ASISTIO, REPROGRAMADA
    MOTIVO_CONSULTA    VARCHAR2(300 CHAR),
    CANAL_RESERVA      VARCHAR2(20 CHAR)  DEFAULT 'WEB' NOT NULL,  -- WEB, TELEFONO, PRESENCIAL
    CODIGO_QR          VARCHAR2(100 CHAR),
    NUMERO_TURNO       NUMBER(4),
    FECHA_REGISTRO     TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    FECHA_CHECKIN      TIMESTAMP,
    OBSERVACIONES      VARCHAR2(500 CHAR),
    CONSTRAINT PK_CITA              PRIMARY KEY (ID_CITA),
    CONSTRAINT FK_CITA_PACIENTE     FOREIGN KEY (ID_PACIENTE)     REFERENCES PACIENTE (ID_PACIENTE),
    CONSTRAINT FK_CITA_MEDICO       FOREIGN KEY (ID_MEDICO)       REFERENCES MEDICO (ID_MEDICO),
    CONSTRAINT FK_CITA_ESPEC        FOREIGN KEY (ID_ESPECIALIDAD) REFERENCES ESPECIALIDAD (ID_ESPECIALIDAD),
    CONSTRAINT CK_CITA_ESTADO       CHECK (ESTADO IN ('PROGRAMADA','CONFIRMADA','EN_ATENCION','ATENDIDA','CANCELADA','NO_ASISTIO','REPROGRAMADA')),
    CONSTRAINT CK_CITA_CANAL        CHECK (CANAL_RESERVA IN ('WEB','TELEFONO','PRESENCIAL','APP')),
    -- RN-07: no se permite que el mismo médico tenga dos citas en el mismo horario
    CONSTRAINT UQ_CITA_MEDICO_SLOT  UNIQUE (ID_MEDICO, FECHA_CITA, HORA_INICIO)
);

CREATE SEQUENCE SEQ_CITA START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_CITA_BI
BEFORE INSERT ON CITA
FOR EACH ROW
WHEN (NEW.ID_CITA IS NULL)
BEGIN
    :NEW.ID_CITA := SEQ_CITA.NEXTVAL;
END;
/

CREATE INDEX IDX_CITA_FECHA   ON CITA (FECHA_CITA);
CREATE INDEX IDX_CITA_PAC_FEC ON CITA (ID_PACIENTE, FECHA_CITA);
CREATE INDEX IDX_CITA_MED_FEC ON CITA (ID_MEDICO, FECHA_CITA);

-- ---------------------------------------------------------
-- 10) TABLA DIAGNOSTICO_CIE10 (catálogo)
-- ---------------------------------------------------------
CREATE TABLE DIAGNOSTICO_CIE10 (
    CODIGO        VARCHAR2(10 CHAR)  NOT NULL,
    DESCRIPCION   VARCHAR2(300 CHAR) NOT NULL,
    CAPITULO      VARCHAR2(80 CHAR),
    ACTIVO        NUMBER(1)          DEFAULT 1 NOT NULL,
    CONSTRAINT PK_CIE10        PRIMARY KEY (CODIGO),
    CONSTRAINT CK_CIE10_ACTIVO CHECK (ACTIVO IN (0, 1))
);

-- ---------------------------------------------------------
-- 11) TABLA ATENCION
-- ---------------------------------------------------------
CREATE TABLE ATENCION (
    ID_ATENCION         NUMBER(10)         NOT NULL,
    ID_CITA             NUMBER(10)         NOT NULL,
    ID_HISTORIA         NUMBER(10)         NOT NULL,
    ID_MEDICO           NUMBER(10)         NOT NULL,
    FECHA_ATENCION      TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    MOTIVO_CONSULTA     VARCHAR2(500 CHAR),
    ANAMNESIS           CLOB,
    EXAMEN_FISICO       CLOB,
    DIAGNOSTICO_CIE10   VARCHAR2(10 CHAR),
    DIAGNOSTICO_DESC    VARCHAR2(500 CHAR),
    TRATAMIENTO         CLOB,
    OBSERVACIONES       VARCHAR2(1000 CHAR),
    -- signos vitales
    PRESION_ARTERIAL    VARCHAR2(15 CHAR),                   -- "120/80"
    FRECUENCIA_CARDIACA NUMBER(3),
    TEMPERATURA         NUMBER(4, 1),
    PESO_KG             NUMBER(5, 2),
    TALLA_CM            NUMBER(5, 2),
    SATURACION_O2       NUMBER(3),
    -- firma
    FIRMADO_POR         NUMBER(10),
    FECHA_FIRMA         TIMESTAMP,
    CONSTRAINT PK_ATENCION         PRIMARY KEY (ID_ATENCION),
    CONSTRAINT UQ_ATENCION_CITA    UNIQUE (ID_CITA),         -- 1 atención por cita
    CONSTRAINT FK_ATEN_CITA        FOREIGN KEY (ID_CITA)            REFERENCES CITA (ID_CITA),
    CONSTRAINT FK_ATEN_HISTORIA    FOREIGN KEY (ID_HISTORIA)        REFERENCES HISTORIA_CLINICA (ID_HISTORIA),
    CONSTRAINT FK_ATEN_MEDICO      FOREIGN KEY (ID_MEDICO)          REFERENCES MEDICO (ID_MEDICO),
    CONSTRAINT FK_ATEN_CIE10       FOREIGN KEY (DIAGNOSTICO_CIE10)  REFERENCES DIAGNOSTICO_CIE10 (CODIGO),
    CONSTRAINT FK_ATEN_FIRMADOR    FOREIGN KEY (FIRMADO_POR)        REFERENCES USUARIO (ID_USUARIO)
);

CREATE SEQUENCE SEQ_ATENCION START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_ATENCION_BI
BEFORE INSERT ON ATENCION
FOR EACH ROW
WHEN (NEW.ID_ATENCION IS NULL)
BEGIN
    :NEW.ID_ATENCION := SEQ_ATENCION.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 12) TABLA RECETA
-- ---------------------------------------------------------
CREATE TABLE RECETA (
    ID_RECETA        NUMBER(10)        NOT NULL,
    ID_ATENCION      NUMBER(10)        NOT NULL,
    NRO_RECETA       VARCHAR2(20 CHAR) NOT NULL,
    FECHA_EMISION    TIMESTAMP         DEFAULT SYSTIMESTAMP NOT NULL,
    OBSERVACIONES    VARCHAR2(500 CHAR),
    CONSTRAINT PK_RECETA       PRIMARY KEY (ID_RECETA),
    CONSTRAINT UQ_RECETA_NRO   UNIQUE (NRO_RECETA),
    CONSTRAINT FK_RECETA_ATEN  FOREIGN KEY (ID_ATENCION) REFERENCES ATENCION (ID_ATENCION) ON DELETE CASCADE
);

CREATE SEQUENCE SEQ_RECETA START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_RECETA_BI
BEFORE INSERT ON RECETA
FOR EACH ROW
WHEN (NEW.ID_RECETA IS NULL)
BEGIN
    :NEW.ID_RECETA := SEQ_RECETA.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 13) TABLA DETALLE_RECETA
-- (cumple RN-15 / DIGEMID: nombre genérico, dosis, frecuencia, duración)
-- ---------------------------------------------------------
CREATE TABLE DETALLE_RECETA (
    ID_DETALLE          NUMBER(10)         NOT NULL,
    ID_RECETA           NUMBER(10)         NOT NULL,
    NOMBRE_GENERICO     VARCHAR2(150 CHAR) NOT NULL,
    NOMBRE_COMERCIAL    VARCHAR2(150 CHAR),
    PRESENTACION        VARCHAR2(80 CHAR),                  -- "Tabletas 500 mg"
    DOSIS               VARCHAR2(80 CHAR)  NOT NULL,        -- "1 tableta"
    FRECUENCIA          VARCHAR2(80 CHAR)  NOT NULL,        -- "cada 8 horas"
    DURACION_DIAS       NUMBER(3)          NOT NULL,
    VIA_ADMINISTRACION  VARCHAR2(30 CHAR),                  -- ORAL, IV, IM, etc.
    INDICACIONES        VARCHAR2(500 CHAR),
    CONSTRAINT PK_DETALLE_RECETA   PRIMARY KEY (ID_DETALLE),
    CONSTRAINT FK_DETREC_RECETA    FOREIGN KEY (ID_RECETA) REFERENCES RECETA (ID_RECETA) ON DELETE CASCADE,
    CONSTRAINT CK_DETREC_DURACION  CHECK (DURACION_DIAS > 0)
);

CREATE SEQUENCE SEQ_DETALLE_RECETA START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_DETALLE_RECETA_BI
BEFORE INSERT ON DETALLE_RECETA
FOR EACH ROW
WHEN (NEW.ID_DETALLE IS NULL)
BEGIN
    :NEW.ID_DETALLE := SEQ_DETALLE_RECETA.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 14) TABLA ORDEN_EXAMEN
-- ---------------------------------------------------------
CREATE TABLE ORDEN_EXAMEN (
    ID_ORDEN          NUMBER(10)         NOT NULL,
    ID_ATENCION       NUMBER(10)         NOT NULL,
    TIPO_EXAMEN       VARCHAR2(30 CHAR)  NOT NULL,          -- LABORATORIO, IMAGEN, OTRO
    NOMBRE_EXAMEN     VARCHAR2(150 CHAR) NOT NULL,
    INDICACIONES      VARCHAR2(500 CHAR),
    ESTADO            VARCHAR2(20 CHAR)  DEFAULT 'SOLICITADO' NOT NULL,
        -- SOLICITADO, EN_PROCESO, COMPLETADO, ANULADO
    FECHA_SOLICITUD   TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    FECHA_RESULTADO   TIMESTAMP,
    RESULTADO         CLOB,
    URL_RESULTADO     VARCHAR2(300 CHAR),
    CONSTRAINT PK_ORDEN_EXAMEN     PRIMARY KEY (ID_ORDEN),
    CONSTRAINT FK_ORDEN_ATENCION   FOREIGN KEY (ID_ATENCION) REFERENCES ATENCION (ID_ATENCION) ON DELETE CASCADE,
    CONSTRAINT CK_ORDEN_TIPO       CHECK (TIPO_EXAMEN IN ('LABORATORIO','IMAGEN','OTRO')),
    CONSTRAINT CK_ORDEN_ESTADO     CHECK (ESTADO IN ('SOLICITADO','EN_PROCESO','COMPLETADO','ANULADO'))
);

CREATE SEQUENCE SEQ_ORDEN_EXAMEN START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_ORDEN_EXAMEN_BI
BEFORE INSERT ON ORDEN_EXAMEN
FOR EACH ROW
WHEN (NEW.ID_ORDEN IS NULL)
BEGIN
    :NEW.ID_ORDEN := SEQ_ORDEN_EXAMEN.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 15) TABLA PAGO
-- ---------------------------------------------------------
CREATE TABLE PAGO (
    ID_PAGO          NUMBER(10)         NOT NULL,
    ID_CITA          NUMBER(10)         NOT NULL,
    MONTO            NUMBER(10, 2)      NOT NULL,
    METODO_PAGO      VARCHAR2(20 CHAR)  NOT NULL,           -- EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA
    ESTADO           VARCHAR2(20 CHAR)  DEFAULT 'PENDIENTE' NOT NULL,
        -- PENDIENTE, PAGADO, ANULADO, REEMBOLSADO
    NRO_COMPROBANTE  VARCHAR2(30 CHAR),
    TIPO_COMPROBANTE VARCHAR2(15 CHAR),                     -- BOLETA, FACTURA, TICKET
    FECHA_PAGO       TIMESTAMP,
    FECHA_REGISTRO   TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_PAGO          PRIMARY KEY (ID_PAGO),
    CONSTRAINT FK_PAGO_CITA     FOREIGN KEY (ID_CITA) REFERENCES CITA (ID_CITA),
    CONSTRAINT CK_PAGO_MONTO    CHECK (MONTO >= 0),
    CONSTRAINT CK_PAGO_METODO   CHECK (METODO_PAGO IN ('EFECTIVO','TARJETA','YAPE','PLIN','TRANSFERENCIA')),
    CONSTRAINT CK_PAGO_ESTADO   CHECK (ESTADO IN ('PENDIENTE','PAGADO','ANULADO','REEMBOLSADO')),
    CONSTRAINT CK_PAGO_TIPOCOMP CHECK (TIPO_COMPROBANTE IN ('BOLETA','FACTURA','TICKET'))
);

CREATE SEQUENCE SEQ_PAGO START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_PAGO_BI
BEFORE INSERT ON PAGO
FOR EACH ROW
WHEN (NEW.ID_PAGO IS NULL)
BEGIN
    :NEW.ID_PAGO := SEQ_PAGO.NEXTVAL;
END;
/

-- ---------------------------------------------------------
-- 16) TABLA AUDITORIA_LOG (RF-28)
-- ---------------------------------------------------------
CREATE TABLE AUDITORIA_LOG (
    ID_LOG          NUMBER(10)         NOT NULL,
    ID_USUARIO      NUMBER(10),
    USERNAME        VARCHAR2(50 CHAR),
    ACCION          VARCHAR2(30 CHAR)  NOT NULL,           -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT
    ENTIDAD         VARCHAR2(50 CHAR),                     -- nombre de tabla/clase
    ID_ENTIDAD      VARCHAR2(50 CHAR),
    DETALLE         VARCHAR2(2000 CHAR),
    IP_ORIGEN       VARCHAR2(45 CHAR),
    FECHA_HORA      TIMESTAMP          DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT PK_AUDITORIA_LOG  PRIMARY KEY (ID_LOG),
    CONSTRAINT FK_AUDIT_USUARIO  FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO)
);

CREATE SEQUENCE SEQ_AUDITORIA_LOG START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE OR REPLACE TRIGGER TRG_AUDITORIA_LOG_BI
BEFORE INSERT ON AUDITORIA_LOG
FOR EACH ROW
WHEN (NEW.ID_LOG IS NULL)
BEGIN
    :NEW.ID_LOG := SEQ_AUDITORIA_LOG.NEXTVAL;
END;
/

CREATE INDEX IDX_AUDIT_FECHA ON AUDITORIA_LOG (FECHA_HORA);

-- ---------------------------------------------------------
-- 17) VISTAS DE APOYO PARA EL DASHBOARD (RF-25)
-- ---------------------------------------------------------
CREATE OR REPLACE VIEW V_KPI_CITAS_DIA AS
SELECT
    TRUNC(FECHA_CITA)                                          AS FECHA,
    COUNT(*)                                                   AS TOTAL_CITAS,
    SUM(CASE WHEN ESTADO = 'ATENDIDA'   THEN 1 ELSE 0 END)     AS ATENDIDAS,
    SUM(CASE WHEN ESTADO = 'NO_ASISTIO' THEN 1 ELSE 0 END)     AS NO_SHOW,
    SUM(CASE WHEN ESTADO = 'CANCELADA'  THEN 1 ELSE 0 END)     AS CANCELADAS
FROM CITA
GROUP BY TRUNC(FECHA_CITA);

CREATE OR REPLACE VIEW V_INGRESOS_ESPECIALIDAD AS
SELECT
    e.ID_ESPECIALIDAD,
    e.NOMBRE                  AS ESPECIALIDAD,
    TRUNC(p.FECHA_PAGO, 'MM') AS MES,
    SUM(p.MONTO)              AS INGRESO_TOTAL,
    COUNT(*)                  AS NRO_PAGOS
FROM PAGO p
JOIN CITA c         ON c.ID_CITA = p.ID_CITA
JOIN ESPECIALIDAD e ON e.ID_ESPECIALIDAD = c.ID_ESPECIALIDAD
WHERE p.ESTADO = 'PAGADO'
GROUP BY e.ID_ESPECIALIDAD, e.NOMBRE, TRUNC(p.FECHA_PAGO, 'MM');

CREATE OR REPLACE VIEW V_PRODUCTIVIDAD_MEDICO AS
SELECT
    m.ID_MEDICO,
    m.APELLIDO_PATERNO || ' ' || m.NOMBRES AS MEDICO,
    e.NOMBRE                                AS ESPECIALIDAD,
    TRUNC(c.FECHA_CITA, 'MM')               AS MES,
    COUNT(*)                                AS TOTAL_CITAS,
    SUM(CASE WHEN c.ESTADO = 'ATENDIDA' THEN 1 ELSE 0 END) AS ATENDIDAS
FROM CITA c
JOIN MEDICO m       ON m.ID_MEDICO = c.ID_MEDICO
JOIN ESPECIALIDAD e ON e.ID_ESPECIALIDAD = m.ID_ESPECIALIDAD
GROUP BY m.ID_MEDICO, m.APELLIDO_PATERNO, m.NOMBRES, e.NOMBRE, TRUNC(c.FECHA_CITA, 'MM');

COMMIT;
```

### 6.3. Diagrama lógico (relaciones principales)

```
ROL ─┐
     └─< USUARIO_ROL >─ USUARIO ──┬── PACIENTE ──── HISTORIA_CLINICA ──< ATENCION
                                  │       │                                  │
                                  │       └────< CITA >─── PAGO              ├──< RECETA ──< DETALLE_RECETA
                                  │                │                         │
                                  └─── MEDICO ─────┤                         └──< ORDEN_EXAMEN
                                          │        │
                                          ├─< HORARIO_MEDICO
                                          │
                                  ESPECIALIDAD ────┘

ATENCION ── DIAGNOSTICO_CIE10
USUARIO ──< AUDITORIA_LOG
```

---

## 7. Datos maestros

`db/02_seed.sql` — datos imprescindibles para arrancar:

```sql
SET DEFINE OFF;

-- Roles
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES ('ADMINISTRADOR',  'Administrador del sistema');
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES ('GERENTE',        'Gerencia / dashboards');
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES ('MEDICO',         'Médico especialista');
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES ('RECEPCIONISTA',  'Personal de admisión');
INSERT INTO ROL (NOMBRE, DESCRIPCION) VALUES ('PACIENTE',       'Paciente del centro médico');

-- Usuario administrador (password = "admin123" en BCrypt)
-- Hash generado con BCryptPasswordEncoder strength=10
INSERT INTO USUARIO (USERNAME, PASSWORD_HASH, EMAIL)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@trinidadtarapoto.com');

INSERT INTO USUARIO_ROL (ID_USUARIO, ID_ROL)
SELECT u.ID_USUARIO, r.ID_ROL
FROM USUARIO u, ROL r
WHERE u.USERNAME = 'admin' AND r.NOMBRE = 'ADMINISTRADOR';

-- Especialidades (15 según el informe)
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Medicina General',     50.00, 20);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Pediatría',            70.00, 20);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Ginecología',          80.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Cardiología',         100.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Traumatología',        90.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Dermatología',         85.00, 20);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Oftalmología',         90.00, 20);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Otorrinolaringología', 90.00, 20);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Neurología',          110.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Psiquiatría',         120.00, 45);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Urología',            100.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Endocrinología',      100.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Gastroenterología',   100.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Reumatología',        110.00, 30);
INSERT INTO ESPECIALIDAD (NOMBRE, PRECIO_CONSULTA, DURACION_MINUTOS) VALUES ('Odontología',          60.00, 30);

-- Catálogo CIE-10 mínimo (los más frecuentes en consulta externa)
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('J00',   'Rinofaringitis aguda (resfriado común)', 'Sistema respiratorio');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('J06.9', 'Infección aguda de las vías respiratorias superiores, no especificada', 'Sistema respiratorio');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('K29.7', 'Gastritis no especificada', 'Sistema digestivo');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('I10',   'Hipertensión esencial (primaria)', 'Sistema circulatorio');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('E11.9', 'Diabetes mellitus tipo 2, sin complicaciones', 'Endocrinas');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('M54.5', 'Lumbago no especificado', 'Sistema osteomuscular');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('R51',   'Cefalea', 'Síntomas generales');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('R50.9', 'Fiebre, no especificada', 'Síntomas generales');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('Z00.0', 'Examen médico general de adulto', 'Factores que influyen en el estado de salud');
INSERT INTO DIAGNOSTICO_CIE10 (CODIGO, DESCRIPCION, CAPITULO) VALUES ('A09',   'Diarrea y gastroenteritis de presunto origen infeccioso', 'Infecciosas');

COMMIT;
```

> **⚠️ Importante:** el hash de BCrypt incluido es para la palabra `admin123`. **Cámbialo en producción** generando uno nuevo con `BCryptPasswordEncoder().encode("tu-clave")`.

---

## 8. Mapeo Entidad ↔ Tabla ↔ Módulo

| Entidad JPA (`@Entity`) | Tabla Oracle | Módulo del sistema | RF cubiertos |
|---|---|---|---|
| `Usuario` | `USUARIO` | M1 Autenticación | RF-01, RF-02 |
| `Rol`, `UsuarioRol` | `ROL`, `USUARIO_ROL` | M1 | RF-02 |
| `Paciente` | `PACIENTE` | M2 | RF-04, RF-05, RF-06 |
| `Especialidad` | `ESPECIALIDAD` | M4 | RF-24 |
| `Medico` | `MEDICO` | M3 | RF-21 |
| `HorarioMedico` | `HORARIO_MEDICO` | M3 | RF-22, RF-23 |
| `Cita` | `CITA` | M5, M6 | RF-07 a RF-14 |
| `Pago` | `PAGO` | M7 | RF-15, RF-16 |
| `HistoriaClinica` | `HISTORIA_CLINICA` | M8 | RF-17 |
| `Atencion` | `ATENCION` | M8 | RF-18 |
| `DiagnosticoCie10` | `DIAGNOSTICO_CIE10` | M8 | RF-18 |
| `Receta`, `DetalleReceta` | `RECETA`, `DETALLE_RECETA` | M8 | RF-19 |
| `OrdenExamen` | `ORDEN_EXAMEN` | M8 | RF-20, RF-30 |
| `AuditoriaLog` | `AUDITORIA_LOG` | M10 | RF-28 |
| Vistas `V_KPI_*` | (vistas) | M9 Dashboard | RF-25, RF-26 |

---

## 9. Roadmap por unidad del sílabo

### 🟢 Unidad 1 — Frameworks de Front-end (Sem. 1–4) → **APF1**
- Crear vistas estáticas con **HTML5 + CSS3 + JavaScript**.
- Integrar **Bootstrap 5** vía CDN o WebJars (`navbar`, sistema de columnas, formularios, modales).
- Maquetar: `login.html`, `index.html` (landing), `agendar.html` (formulario de cita), `mis-citas.html`, `dashboard.html` (mockup).
- **Entregable APF1:** prototipo navegable HTML/CSS/JS responsivo con todas las pantallas principales.

### 🟢 Unidad 2 — Introducción a Spring (Sem. 5–8) → **APF2**
- Crear proyecto **Spring Boot** con Spring Initializr (dependencias: Web, Thymeleaf, DevTools, Lombok).
- Configurar `application.properties` (sin BD aún o con perfil `mock`).
- Migrar las vistas estáticas a **Thymeleaf** (`th:each`, `th:if`, `th:href`, fragmentos).
- Crear `@Controller` para rutas web (`/`, `/login`, `/citas`, `/dashboard`).
- **Entregable APF2:** sitio web servido desde Spring Boot con Thymeleaf y datos mock en memoria.

### 🟢 Unidad 3 — Spring con bases de datos (Sem. 9–12) → **APF3**
- Agregar dependencias: `spring-boot-starter-data-jpa` + `ojdbc11` + `spring-boot-starter-validation`.
- Configurar conexión a **Oracle 21c XE** en `application.properties`.
- Ejecutar `01_schema.sql` y `02_seed.sql` en SQL Developer.
- Crear las **17 entidades JPA** (`@Entity`, `@Table`, `@Id`, `@GeneratedValue` con `@SequenceGenerator`).
- Crear **repositorios** (`JpaRepository`).
- Implementar **CRUD completo** vía REST (`@RestController`) y MVC (`@Controller`) para Pacientes, Médicos, Especialidades, Citas.
- Aplicar **Bean Validation** (`@NotNull`, `@Pattern`, `@Email`, `@Size`).
- **Entregable APF3:** sistema funcional con BD Oracle, CRUD completo y validaciones.

### 🟢 Unidad 4 — Spring Security (Sem. 13–18) → **PROYECTO FINAL**
- Agregar `spring-boot-starter-security` + `jjwt`.
- Configurar `SecurityConfig` con `SecurityFilterChain`.
- Implementar `UserDetailsService` cargando desde `USUARIO` y `USUARIO_ROL`.
- Hash de contraseñas con `BCryptPasswordEncoder`.
- Configurar **autorización por rol** (`hasRole("MEDICO")`, etc.).
- Crear `JwtService` + `JwtAuthFilter` para proteger endpoints `/api/**`.
- Implementar **login dual**: formulario Thymeleaf (sesión) + endpoint `/api/auth/login` (JWT).
- Implementar el módulo de **dashboard** con KPIs (Recharts/Chart.js consumiendo endpoints REST).
- Implementar la **auditoría** (`@EventListener` o aspecto AOP que escriba a `AUDITORIA_LOG`).
- **Entregable PROY:** sistema completo desplegado, con seguridad, dashboard y documentación final.

---

## 10. Convenciones y prompts para Copilot

### Convenciones de código
- **Paquete base:** `com.trinidad.citas`
- **Nombres en español** para entidades del dominio (Paciente, Cita, Atencion) — refleja el negocio peruano.
- **DTOs** suffix `DTO` para no exponer entidades JPA en endpoints REST.
- **Endpoints REST** bajo `/api/v1/...` — **MVC web** sin prefijo.
- **Validación** en DTOs con anotaciones de `jakarta.validation`.
- **Manejo de errores** centralizado en `GlobalExceptionHandler` con `@ControllerAdvice`.
- **Logs** con SLF4J (`private static final Logger log = LoggerFactory.getLogger(...)`).
- **Transacciones** en la capa de servicio con `@Transactional`.

### Prompts útiles para Copilot Chat (en VS Code)

Pega estos en el chat de Copilot cuando arranques cada parte:

> **Para crear una entidad JPA:**
> *"Genera la entidad JPA `Paciente` que mapea a la tabla `PACIENTE` del esquema `TRINIDAD_DB`, usando `@SequenceGenerator` con `SEQ_PACIENTE`. Incluye Bean Validation: DNI con `@Pattern("^[0-9]{8}$")`, nombres `@NotBlank`, fecha de nacimiento `@Past`, sexo restringido a `M` o `F`. Usa Lombok."*

> **Para el repositorio:**
> *"Crea el repositorio `PacienteRepository extends JpaRepository<Paciente, Long>` con métodos: `findByDni(String dni)`, `existsByDni(String dni)`, y una query con `@Query` que liste pacientes paginados filtrando por nombre o apellido (LIKE)."*

> **Para el servicio de Citas con reglas de negocio:**
> *"Implementa `CitaService.agendarCita(CitaDTO dto)` que valide: (RN-07) que el médico no tenga otra cita en ese slot, (RN-09) que la duración respete `Especialidad.duracionMinutos`, (RN-10) que el paciente no tenga otra cita activa con el mismo médico en los últimos 7 días. Lanza `BusinessException` con mensajes claros."*

> **Para Spring Security:**
> *"Configura `SecurityConfig` con `SecurityFilterChain`: rutas `/`, `/login`, `/css/**`, `/js/**` públicas; `/api/auth/**` pública; `/admin/**` solo `ADMINISTRADOR`; `/medico/**` solo `MEDICO`; `/portal/**` solo `PACIENTE`. Login form en `/login`, logout en `/logout`. Configura CSRF deshabilitado solo en `/api/**` y agrega el `JwtAuthFilter` antes del `UsernamePasswordAuthenticationFilter`."*

> **Para JWT:**
> *"Genera `JwtService` con `jjwt 0.12.x` que tenga: `generateToken(UserDetails user)`, `extractUsername(String token)`, `isTokenValid(String token, UserDetails user)`. Lee la clave secreta y la expiración desde `application.properties` (`trinidad.jwt.secret`, `trinidad.jwt.expiration-ms`)."*

> **Para Thymeleaf con Bootstrap:**
> *"Crea la plantilla `templates/citas/agendar.html` que extienda el fragmento `fragments/header.html`, use Bootstrap 5 (cards, form-control, btn-primary), un `select` para especialidad (Thymeleaf `th:each` sobre `${especialidades}`), date picker, y al cambiar la especialidad llame por `fetch` al endpoint `/api/v1/disponibilidad?idEspecialidad={id}&fecha={fecha}` para listar médicos disponibles."*

### Pasos para arrancar el proyecto

1. **Instalar Oracle 21c XE** y crear el PDB `XE` (viene por defecto).
2. **SQL Developer** → conectar como `SYS` y ejecutar la sección 6.1 (crear usuario `TRINIDAD_DB`).
3. Reconectar como `TRINIDAD_DB` y ejecutar `01_schema.sql` y luego `02_seed.sql`.
4. **Spring Initializr** ([start.spring.io](https://start.spring.io)) — generar proyecto Maven, Java 17, Spring Boot 3.2.x con dependencias: Web, Thymeleaf, JPA, Validation, Security, DevTools, Lombok.
5. Reemplazar el `pom.xml` con el de la sección 5 (agregar `ojdbc11` y `jjwt`).
6. Configurar `application.properties` con tus credenciales reales.
7. **VS Code** — abrir la carpeta, instalar extensiones: *Extension Pack for Java*, *Spring Boot Extension Pack*, *GitHub Copilot*.
8. Crear las clases siguiendo la estructura de la sección 4, usando los prompts del paso 10.
9. Ejecutar: `./mvnw spring-boot:run` y abrir `http://localhost:8080`.

---

**Documento listo para Copilot.** Pégalo en `docs/PROYECTO_TRINIDAD_COPILOT.md` dentro de tu repo y agrégalo al contexto del Copilot Chat con `@workspace` o `#file:PROYECTO_TRINIDAD_COPILOT.md` para que Copilot tenga toda la información del dominio, modelo de datos y convenciones al generar código.
