# Sistema de Gestión de Citas Médicas y Administración
## Trinidad & Especialidades Médicas S.A.C.
### Guía de Desarrollo — Marcos de Desarrollo Web (100000S52T) | UTP 2026

---

## Índice

1. [Contexto del Proyecto](#1-contexto-del-proyecto)
2. [Stack Tecnológico](#2-stack-tecnológico)
3. [Arquitectura del Sistema](#3-arquitectura-del-sistema)
4. [Modelo de Datos (Oracle)](#4-modelo-de-datos-oracle)
5. [Módulos del Sistema](#5-módulos-del-sistema)
6. [Reglas de Negocio](#6-reglas-de-negocio)
7. [Requerimientos Funcionales](#7-requerimientos-funcionales)
8. [Requerimientos No Funcionales](#8-requerimientos-no-funcionales)
9. [Plan de Desarrollo por Semanas](#9-plan-de-desarrollo-por-semanas)
10. [Historias de Usuario](#10-historias-de-usuario)
11. [Estructura de Carpetas del Proyecto](#11-estructura-de-carpetas-del-proyecto)
12. [Convenciones de Código](#12-convenciones-de-código)

---

## 1. Contexto del Proyecto

**Empresa cliente:** Trinidad & Especialidades Médicas S.A.C.
**RUC:** 20494088208
**Ubicación:** Pasaje Las Mesetas N°112, Partido Alto, Tarapoto, San Martín, Perú
**Categoría MINSA:** Centro de Salud con Camas de Internamiento (Categoría I-4)

### Problema central

La clínica opera con más de 15 especialidades médicas y atención 24/7, pero gestiona citas, admisiones e historias clínicas de forma manual (cuadernos físicos, hojas de cálculo desconectadas, fichas en papel). Esto genera:

- Recepción saturada en horas pico con una sola persona atendiendo presencial y teléfono simultáneamente
- Búsqueda de historia clínica en archiveros físicos: entre 5 y 15 minutos por paciente
- Duplicidad de registros de pacientes (6–9% de los registros son duplicados)
- Imposibilidad de agendar citas fuera del horario de recepción
- Inasistencias del 18–22% por falta de recordatorios
- Reportes gerenciales manuales que consumen entre 4 y 8 horas al mes

### Solución propuesta

Sistema web integral con portal de pacientes (agendamiento 24/7), módulo de admisión con check-in por QR/DNI, historias clínicas electrónicas, gestión de médicos y horarios, y dashboard gerencial con KPIs en tiempo real.

---

## 2. Stack Tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Frontend | HTML5 + CSS3 + JavaScript | ES2022+ |
| Framework CSS | Bootstrap | 5.3.x (CDN) |
| Template Engine | Thymeleaf | 3.1.x |
| Backend | Java + Spring Boot | 3.x (JDK 17) |
| Persistencia | Spring Data JPA + Hibernate | — |
| Seguridad | Spring Security + JWT | — |
| Base de datos | Oracle Database 21c XE | — |
| Servidor | Apache Tomcat | 10.x (embebido en Spring Boot) |
| Control de versiones | Git + GitHub | — |
| Diseño UI/UX | Figma | — |
| Modelado BPMN | Bizagi Modeler | — |
| Diagramas UML | Draw.io / StarUML | — |
| IDE | IntelliJ IDEA / VS Code | — |

### Dependencias Maven principales (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Boot Starter Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Oracle JDBC Driver -->
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.12.3</version>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Spring Boot DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### application.properties (Oracle)

```properties
# Oracle DataSource
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=trinidad_user
spring.datasource.password=trinidad_pass
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Session timeout (15 min — RNF-09)
server.servlet.session.timeout=15m

# Server
server.port=8080
```

---

## 3. Arquitectura del Sistema

El sistema sigue arquitectura multicapa (n-tier) con separación estricta de responsabilidades.

```
┌─────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                 │
│         HTML5 + CSS3 + JavaScript + Bootstrap           │
│              Thymeleaf (Server-Side Rendering)           │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP/HTTPS
┌──────────────────────────▼──────────────────────────────┐
│                  CAPA DE CONTROLADORES                  │
│              Spring MVC Controllers / REST              │
│         (AuthController, CitaController, etc.)          │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                   CAPA DE SERVICIOS                     │
│            Business Logic / Spring @Service             │
│       (CitaService, PacienteService, etc.)              │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                  CAPA DE REPOSITORIOS                   │
│         Spring Data JPA / Hibernate ORM                 │
│         (CitaRepository, PacienteRepository, etc.)      │
└──────────────────────────┬──────────────────────────────┘
                           │ JDBC (ojdbc11)
┌──────────────────────────▼──────────────────────────────┐
│                   BASE DE DATOS                         │
│              Oracle Database 21c XE                     │
│         (16 tablas, secuencias, triggers, PL/SQL)       │
└─────────────────────────────────────────────────────────┘
```

### Paquetes Java

```
com.trinidad.medical
├── config/                  # SecurityConfig, JwtConfig, WebMvcConfig
├── controller/              # Spring MVC Controllers
│   ├── AuthController
│   ├── PacienteController
│   ├── CitaController
│   ├── AtencionController
│   ├── MedicoController
│   ├── AdministradorController
│   └── GerenciaController
├── service/                 # Business logic
│   ├── AuthService
│   ├── PacienteService
│   ├── CitaService
│   ├── AtencionService
│   ├── MedicoService
│   ├── HorarioService
│   ├── PagoService
│   ├── NotificacionService
│   └── ReporteService
├── repository/              # Spring Data JPA interfaces
├── entity/                  # JPA @Entity classes (mapeadas a Oracle)
├── dto/                     # Data Transfer Objects
├── security/                # JWT filter, UserDetailsService
├── scheduler/               # Jobs automáticos (@Scheduled)
└── exception/               # GlobalExceptionHandler
```

---

## 4. Modelo de Datos (Oracle)

El esquema consta de **16 tablas** organizadas en cinco grupos funcionales. Todas las PKs se autogeneran con secuencias + triggers `BEFORE INSERT`.

### 4.1 Grupo: Seguridad y Acceso

#### Tabla: ROL
```sql
CREATE TABLE ROL (
    id_rol      NUMBER PRIMARY KEY,
    nombre      VARCHAR2(50) UNIQUE NOT NULL,  -- PACIENTE, RECEPCIONISTA, MEDICO, ADMINISTRADOR, GERENTE
    descripcion VARCHAR2(200),
    activo      NUMBER(1) DEFAULT 1
);
```

#### Tabla: USUARIO
```sql
CREATE TABLE USUARIO (
    id_usuario       NUMBER PRIMARY KEY,
    username         VARCHAR2(50) UNIQUE NOT NULL,
    password_hash    VARCHAR2(200) NOT NULL,       -- BCrypt
    email            VARCHAR2(100) UNIQUE NOT NULL,
    activo           NUMBER(1) DEFAULT 1,
    intentos_fallidos NUMBER DEFAULT 0,
    bloqueado        NUMBER(1) DEFAULT 0,
    fecha_creacion   TIMESTAMP DEFAULT SYSTIMESTAMP,
    fecha_ult_login  TIMESTAMP
);
```

#### Tabla: USUARIO_ROL (N:M)
```sql
CREATE TABLE USUARIO_ROL (
    id_usuario NUMBER NOT NULL,
    id_rol     NUMBER NOT NULL,
    CONSTRAINT pk_usuario_rol PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol     FOREIGN KEY (id_rol)     REFERENCES ROL(id_rol)
);
```

#### Tabla: AUDITORIA_LOG
```sql
CREATE TABLE AUDITORIA_LOG (
    id_log     NUMBER PRIMARY KEY,
    id_usuario NUMBER REFERENCES USUARIO(id_usuario),  -- NULL si acción anónima
    username   VARCHAR2(50),
    accion     VARCHAR2(50) NOT NULL,   -- LOGIN, INSERT, UPDATE, DELETE
    entidad    VARCHAR2(50),
    id_entidad VARCHAR2(50),
    detalle    VARCHAR2(500),
    ip_origen  VARCHAR2(45),
    fecha_hora TIMESTAMP DEFAULT SYSTIMESTAMP
);
```

### 4.2 Grupo: Personas y Catálogos

#### Tabla: PACIENTE
```sql
CREATE TABLE PACIENTE (
    id_paciente      NUMBER PRIMARY KEY,
    id_usuario       NUMBER REFERENCES USUARIO(id_usuario),  -- opcional (0..1)
    dni              VARCHAR2(8) UNIQUE NOT NULL,
    CONSTRAINT chk_dni CHECK (REGEXP_LIKE(dni, '^\d{8}$')),
    nombres          VARCHAR2(100) NOT NULL,
    apellido_paterno VARCHAR2(50) NOT NULL,
    apellido_materno VARCHAR2(50),
    fecha_nacimiento DATE NOT NULL,
    sexo             CHAR(1) NOT NULL,
    CONSTRAINT chk_sexo CHECK (sexo IN ('M','F')),
    telefono         VARCHAR2(20),
    email            VARCHAR2(100),
    direccion        VARCHAR2(200),
    distrito         VARCHAR2(50),
    tipo_sangre      VARCHAR2(5),
    alergias         CLOB,
    activo           NUMBER(1) DEFAULT 1,
    fecha_registro   TIMESTAMP DEFAULT SYSTIMESTAMP
);
```

#### Tabla: ESPECIALIDAD
```sql
CREATE TABLE ESPECIALIDAD (
    id_especialidad  NUMBER PRIMARY KEY,
    nombre           VARCHAR2(100) UNIQUE NOT NULL,
    descripcion      VARCHAR2(300),
    precio_consulta  NUMBER(10,2) NOT NULL,
    duracion_minutos NUMBER DEFAULT 20,
    activo           NUMBER(1) DEFAULT 1
);
```

#### Tabla: MEDICO
```sql
CREATE TABLE MEDICO (
    id_medico        NUMBER PRIMARY KEY,
    id_usuario       NUMBER UNIQUE NOT NULL REFERENCES USUARIO(id_usuario),  -- 1:1
    id_especialidad  NUMBER NOT NULL REFERENCES ESPECIALIDAD(id_especialidad),
    cmp              VARCHAR2(20) UNIQUE NOT NULL,
    nombres          VARCHAR2(100) NOT NULL,
    apellido_paterno VARCHAR2(50) NOT NULL,
    apellido_materno VARCHAR2(50),
    telefono         VARCHAR2(20),
    email            VARCHAR2(100),
    consultorio      VARCHAR2(20),
    activo           NUMBER(1) DEFAULT 1
);
```

#### Tabla: HORARIO_MEDICO
```sql
CREATE TABLE HORARIO_MEDICO (
    id_horario  NUMBER PRIMARY KEY,
    id_medico   NUMBER NOT NULL REFERENCES MEDICO(id_medico) ON DELETE CASCADE,
    dia_semana  NUMBER(1) NOT NULL,   -- 1=Lunes, 7=Domingo
    CONSTRAINT chk_dia CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio DATE NOT NULL,
    hora_fin    DATE NOT NULL,
    activo      NUMBER(1) DEFAULT 1
);
```

### 4.3 Grupo: Operación Clínica

#### Tabla: HISTORIA_CLINICA
```sql
CREATE TABLE HISTORIA_CLINICA (
    id_historia   NUMBER PRIMARY KEY,
    id_paciente   NUMBER UNIQUE NOT NULL REFERENCES PACIENTE(id_paciente) ON DELETE CASCADE,
    nro_historia  VARCHAR2(20) UNIQUE NOT NULL,
    fecha_apertura DATE DEFAULT SYSDATE,
    observaciones VARCHAR2(500),
    activo        NUMBER(1) DEFAULT 1
);
```

#### Tabla: CITA
```sql
CREATE TABLE CITA (
    id_cita         NUMBER PRIMARY KEY,
    id_paciente     NUMBER NOT NULL REFERENCES PACIENTE(id_paciente),
    id_medico       NUMBER NOT NULL REFERENCES MEDICO(id_medico),
    id_especialidad NUMBER NOT NULL REFERENCES ESPECIALIDAD(id_especialidad),
    fecha_cita      DATE NOT NULL,
    hora_inicio     DATE NOT NULL,
    hora_fin        DATE NOT NULL,
    estado          VARCHAR2(20) DEFAULT 'PROGRAMADA',
    CONSTRAINT chk_estado_cita CHECK (estado IN ('PROGRAMADA','CONFIRMADA','ATENDIDA','NO_ASISTIDA','CANCELADA','REPROGRAMADA')),
    motivo_consulta VARCHAR2(300),
    canal_reserva   VARCHAR2(20) DEFAULT 'WEB',
    CONSTRAINT chk_canal CHECK (canal_reserva IN ('WEB','PRESENCIAL','TELEFONO')),
    codigo_qr       VARCHAR2(200),
    numero_turno    NUMBER,
    fecha_registro  TIMESTAMP DEFAULT SYSTIMESTAMP,
    fecha_checkin   TIMESTAMP,
    observaciones   VARCHAR2(300),
    -- Evitar solapamientos: un médico no puede tener dos citas en el mismo horario
    CONSTRAINT uq_cita_medico_horario UNIQUE (id_medico, fecha_cita, hora_inicio)
);
```

#### Tabla: ATENCION
```sql
CREATE TABLE ATENCION (
    id_atencion       NUMBER PRIMARY KEY,
    id_cita           NUMBER UNIQUE NOT NULL REFERENCES CITA(id_cita),  -- 1:1
    id_historia       NUMBER NOT NULL REFERENCES HISTORIA_CLINICA(id_historia),
    id_medico         NUMBER NOT NULL REFERENCES MEDICO(id_medico),
    fecha_atencion    TIMESTAMP DEFAULT SYSTIMESTAMP,
    motivo_consulta   VARCHAR2(300),
    anamnesis         CLOB,
    examen_fisico     CLOB,
    diagnostico_cie10 VARCHAR2(10) REFERENCES DIAGNOSTICO_CIE10(codigo),
    diagnostico_desc  VARCHAR2(300),
    tratamiento       CLOB,
    observaciones     VARCHAR2(500),
    -- Signos vitales
    presion_arterial  VARCHAR2(20),
    frecuencia_cardiaca NUMBER,
    temperatura       NUMBER(4,1),
    peso_kg           NUMBER(5,2),
    talla_cm          NUMBER(5,2),
    saturacion_o2     NUMBER(3),
    -- Firma electrónica
    firmado_por       NUMBER REFERENCES USUARIO(id_usuario),
    fecha_firma       TIMESTAMP
);
```

### 4.4 Grupo: Prescripción y Exámenes

#### Tabla: RECETA
```sql
CREATE TABLE RECETA (
    id_receta    NUMBER PRIMARY KEY,
    id_atencion  NUMBER NOT NULL REFERENCES ATENCION(id_atencion) ON DELETE CASCADE,
    nro_receta   VARCHAR2(20) UNIQUE NOT NULL,
    fecha_emision TIMESTAMP DEFAULT SYSTIMESTAMP,
    observaciones VARCHAR2(300)
);
```

#### Tabla: DETALLE_RECETA
```sql
CREATE TABLE DETALLE_RECETA (
    id_detalle          NUMBER PRIMARY KEY,
    id_receta           NUMBER NOT NULL REFERENCES RECETA(id_receta) ON DELETE CASCADE,
    nombre_generico     VARCHAR2(200) NOT NULL,    -- Obligatorio DIGEMID (RN-15)
    nombre_comercial    VARCHAR2(200),
    presentacion        VARCHAR2(100),
    dosis               VARCHAR2(100) NOT NULL,
    frecuencia          VARCHAR2(100) NOT NULL,
    duracion_dias       NUMBER NOT NULL,
    via_administracion  VARCHAR2(50),
    indicaciones        VARCHAR2(300)
);
```

#### Tabla: ORDEN_EXAMEN
```sql
CREATE TABLE ORDEN_EXAMEN (
    id_orden        NUMBER PRIMARY KEY,
    id_atencion     NUMBER NOT NULL REFERENCES ATENCION(id_atencion) ON DELETE CASCADE,
    tipo_examen     VARCHAR2(50) NOT NULL,   -- LABORATORIO, IMAGEN
    nombre_examen   VARCHAR2(200) NOT NULL,
    indicaciones    CLOB,
    estado          VARCHAR2(20) DEFAULT 'SOLICITADO',
    fecha_solicitud TIMESTAMP DEFAULT SYSTIMESTAMP,
    fecha_resultado TIMESTAMP
);
```

#### Tabla: DIAGNOSTICO_CIE10
```sql
CREATE TABLE DIAGNOSTICO_CIE10 (
    codigo      VARCHAR2(10) PRIMARY KEY,
    descripcion VARCHAR2(300) NOT NULL,
    capitulo    VARCHAR2(100),
    activo      NUMBER(1) DEFAULT 1
);
```

### 4.5 Grupo: Cobranza y Trazabilidad

#### Tabla: PAGO
```sql
CREATE TABLE PAGO (
    id_pago         NUMBER PRIMARY KEY,
    id_cita         NUMBER NOT NULL REFERENCES CITA(id_cita),
    monto           NUMBER(10,2) NOT NULL,
    metodo_pago     VARCHAR2(20) NOT NULL,
    CONSTRAINT chk_metodo CHECK (metodo_pago IN ('EFECTIVO','TARJETA','YAPE','PLIN','TRANSFERENCIA')),
    estado          VARCHAR2(20) DEFAULT 'PENDIENTE',
    CONSTRAINT chk_estado_pago CHECK (estado IN ('PENDIENTE','PAGADO','ANULADO','REEMBOLSADO')),
    nro_comprobante VARCHAR2(50),
    tipo_comprobante VARCHAR2(20),  -- BOLETA, FACTURA
    fecha_pago      TIMESTAMP,
    fecha_registro  TIMESTAMP DEFAULT SYSTIMESTAMP
);
```

### 4.6 Secuencias y Triggers (patrón Oracle XE)

```sql
-- Ejemplo para USUARIO (replicar para todas las tablas)
CREATE SEQUENCE SEQ_USUARIO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE OR REPLACE TRIGGER TRG_USUARIO_BI
BEFORE INSERT ON USUARIO
FOR EACH ROW
BEGIN
    IF :NEW.id_usuario IS NULL THEN
        SELECT SEQ_USUARIO.NEXTVAL INTO :NEW.id_usuario FROM DUAL;
    END IF;
END;
/
```

---

## 5. Módulos del Sistema

| Módulo | Descripción | Roles con acceso |
|--------|------------|-----------------|
| **Autenticación** | Login, recuperación de contraseña, cierre automático de sesión a 15 min | Todos |
| **Portal Paciente** | Registro, agendamiento 24/7, cancelación/reprogramación, historial, resultados | Paciente |
| **Admisión** | Check-in por QR/DNI, asignación de turno, registro presencial | Recepcionista |
| **Citas** | Gestión completa de agenda, disponibilidad en tiempo real, estados | Recepcionista, Médico |
| **Atención Médica** | Historia clínica electrónica, diagnóstico CIE-10, receta digital, órdenes de examen | Médico |
| **Médicos y Horarios** | CRUD de médicos, configuración de agenda semanal, especialidades | Administrador |
| **Pagos** | Registro de cobros en caja, pago en línea, emisión de comprobante | Recepcionista, Sistema |
| **Reportes / Dashboard** | KPIs en tiempo real, reportes mensuales, exportación PDF/Excel | Gerente |
| **Administración** | Gestión de usuarios, roles y permisos, auditoría | Administrador |
| **Notificaciones** | Confirmación automática por email/SMS, recordatorios 24h antes, alertas no-show | Sistema (scheduler) |

---

## 6. Reglas de Negocio

| Código | Regla |
|--------|-------|
| RN-01 | Todo paciente debe registrarse con DNI antes de ser atendido (excepción: emergencias críticas) |
| RN-02 | No pueden existir dos registros activos con el mismo DNI; el sistema debe consolidarlos |
| RN-03 | La consulta se paga antes de la atención, salvo emergencias o convenios vigentes |
| RN-04 | Menores de edad requieren padre/madre/tutor legal que firme autorización |
| RN-05 | La historia clínica debe estar disponible para el médico antes de iniciar la atención |
| RN-06 | Atención preferencial según Ley N° 28683 (gestantes, adultos mayores, personas con discapacidad) |
| RN-07 | Una cita solo puede asignarse en horarios disponibles del médico, sin solapamientos |
| RN-08 | El paciente puede cancelar o reprogramar hasta 24 horas antes sin penalidad |
| RN-09 | Duración por defecto de cita: 20 minutos (configurable por especialidad) |
| RN-10 | Un paciente no puede tener más de una cita activa con el mismo especialista en 7 días, salvo control derivado |
| RN-11 | Si el paciente no hace check-in en 30 minutos, la cita se marca automáticamente como `NO_ASISTIDA` y el cupo se libera |
| RN-12 | Tres inasistencias seguidas sin aviso = restricción de reserva online por 30 días |
| RN-13 | Toda atención debe registrarse en la historia clínica electrónica el mismo día |
| RN-14 | Los diagnósticos se codifican según CIE-10 |
| RN-15 | Las recetas incluyen obligatoriamente: nombre genérico, dosis, frecuencia, duración (normativa DIGEMID) |
| RN-16 | Solo personal de salud autorizado accede a información clínica (Ley N° 29733) |
| RN-17 | Cada atención queda firmada con nombre, CMP y fecha-hora del médico responsable |
| RN-18 | Procedimientos en menores o pacientes con condiciones especiales requieren consentimiento informado documentado |

---

## 7. Requerimientos Funcionales

### Módulo: Autenticación

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-01 | Login con usuario y contraseña validado contra BD | Alta |
| RF-02 | Control de acceso diferenciado por rol (paciente, recepcionista, médico, administrador, gerente) | Alta |
| RF-03 | Recuperación de contraseña por correo electrónico | Media |

### Módulo: Pacientes

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-04 | Registrar nuevos pacientes con datos personales (DNI, nombres, fecha de nacimiento, teléfono, etc.) | Alta |
| RF-05 | Validar que no existan pacientes duplicados por DNI | Alta |
| RF-06 | El paciente puede actualizar sus datos de contacto desde su portal | Media |

### Módulo: Citas

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-07 | Mostrar disponibilidad de médicos en tiempo real, filtrable por especialidad y fecha | Alta |
| RF-08 | Agendar cita desde portal web 24/7 eligiendo médico, fecha y hora | Alta |
| RF-09 | Enviar confirmación de cita por email y SMS con código QR | Alta |
| RF-10 | Enviar recordatorio automático 24 horas antes de la cita | Alta |
| RF-11 | Cancelar o reprogramar cita hasta 24 horas antes sin penalidad | Alta |
| RF-12 | Marcar automáticamente la cita como `NO_ASISTIDA` a los 30 minutos si no hubo check-in | Media |

### Módulo: Admisión

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-13 | Check-in del paciente escaneando QR o DNI | Alta |
| RF-14 | Asignar número de turno y notificar al consultorio del médico | Alta |

### Módulo: Pagos

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-15 | Pago de consulta en línea (tarjeta) o registro de cobro en caja | Alta |
| RF-16 | Emitir comprobante de pago electrónico al paciente | Media |

### Módulo: Historia Clínica

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-17 | El médico consulta la historia clínica electrónica al iniciar la atención | Alta |
| RF-18 | Registrar la atención: motivo, anamnesis, examen físico, diagnóstico CIE-10 y tratamiento | Alta |
| RF-19 | Emitir recetas médicas digitales con campos DIGEMID obligatorios | Alta |
| RF-20 | Solicitar exámenes auxiliares (laboratorio, imágenes) desde la atención | Media |

### Módulo: Médicos y Horarios

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-21 | El administrador registra y gestiona médicos con CMP, especialidad y horarios | Alta |
| RF-22 | El médico ve su agenda diaria con los pacientes del día | Alta |
| RF-23 | El administrador configura los horarios de atención por médico y día de la semana | Alta |
| RF-24 | El administrador gestiona el catálogo de especialidades y precios | Media |

### Módulo: Reportes / Dashboard

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-25 | Dashboard con KPIs principales (citas atendidas, no-show, ingresos, productividad por médico) | Alta |
| RF-26 | Reportes mensuales de productividad, ingresos por especialidad y satisfacción | Media |
| RF-27 | Exportar reportes en PDF y Excel | Media |

### Módulo: Auditoría y Notificaciones

| ID | Descripción | Prioridad |
|----|-------------|-----------|
| RF-28 | Registrar todas las acciones críticas con usuario y fecha-hora | Alta |
| RF-29 | El paciente consulta sus citas pasadas y futuras desde su portal | Media |
| RF-30 | El paciente ve los resultados de exámenes disponibles desde su portal | Media |

---

## 8. Requerimientos No Funcionales

| ID | Categoría | Descripción |
|----|-----------|-------------|
| RNF-01 | Rendimiento | Respuesta en operaciones comunes (login, listar citas, agendar) < 3 segundos |
| RNF-02 | Rendimiento | Soporte de al menos 100 usuarios concurrentes sin degradación |
| RNF-03 | Disponibilidad | Sistema disponible ≥ 99% del tiempo |
| RNF-04 | Disponibilidad | Portal de pacientes accesible 24/7, todos los días del año |
| RNF-05 | Seguridad | Contraseñas almacenadas con BCrypt |
| RNF-06 | Seguridad | Comunicación cliente-servidor por HTTPS (TLS 1.2+) |
| RNF-07 | Seguridad | Cumplimiento Ley N° 29733 (Protección de Datos Personales) |
| RNF-08 | Seguridad | Cumplimiento Ley N° 30024 (Historias Clínicas Electrónicas) |
| RNF-09 | Seguridad | Cierre automático de sesión tras 15 minutos de inactividad |
| RNF-10 | Usabilidad | Interfaces en español, manejables sin formación técnica |
| RNF-11 | Usabilidad | Sistema responsive: computadoras, tablets y celulares |
| RNF-12 | Compatibilidad | Soporte en Chrome, Firefox, Edge y Safari (últimas 2 versiones) |
| RNF-13 | Mantenibilidad | Código en arquitectura en capas, con comentarios y nombres descriptivos |
| RNF-14 | Mantenibilidad | Logs de errores y eventos importantes |
| RNF-15 | Escalabilidad | Arquitectura que permita escalado horizontal |
| RNF-16 | Confiabilidad | Respaldos automáticos diarios de la BD |
| RNF-17 | Confiabilidad | Mecanismo de recuperación ante caída del servidor |
| RNF-18 | Tecnológico | Java Spring Boot + HTML5/CSS3/JS + Oracle Database 21c |
| RNF-19 | Tecnológico | Servidor Apache Tomcat 10 (embebido en Spring Boot) |
| RNF-20 | Legal | Recetas digitales según normativa DIGEMID |

---

## 9. Plan de Desarrollo por Semanas

> El plan respeta el cronograma del sílabo de Marcos de Desarrollo Web (UTP 2026-1) y el cronograma interno del proyecto Trinidad (16 semanas de desarrollo).

---

### SEMANA 1–2 | Unidad 1: Frontend — Introducción y Bootstrap base
**Sílabo:** HTML, CSS, JavaScript, conceptos de frameworks de front end, introducción a Bootstrap (CDN, grid, navbar, tablas, íconos)
**Entregable del proyecto:** Estructura base del proyecto Spring Boot + layout general con Bootstrap

#### Tareas

- [ ] Crear proyecto Spring Boot con las dependencias del pom.xml (Web, Thymeleaf, Security, JPA, Oracle)
- [ ] Configurar `application.properties` con conexión a Oracle Database 21c XE
- [ ] Definir estructura de carpetas del proyecto
- [ ] Crear layout base en Thymeleaf con Bootstrap 5 CDN: navbar, sidebar, footer
- [ ] Implementar página de inicio (landing page pública) con información de la clínica
- [ ] Crear página de login responsive con Bootstrap
- [ ] Configurar Bootstrap vía CDN en el `<head>` de los templates Thymeleaf

#### Código de referencia — Layout base Thymeleaf (fragments/layout.html)

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity6">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${pageTitle} + ' | Clínica Trinidad'">Clínica Trinidad</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">
    <!-- CSS personalizado -->
    <link th:href="@{/css/trinidad.css}" rel="stylesheet">
</head>
<body>
    <!-- Navbar -->
    <nav th:replace="~{fragments/navbar :: navbar}"></nav>

    <!-- Contenido principal -->
    <main class="container-fluid mt-4">
        <th:block th:replace="${content}"></th:block>
    </main>

    <!-- Footer -->
    <footer th:replace="~{fragments/footer :: footer}"></footer>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script th:src="@{/js/trinidad.js}"></script>
</body>
</html>
```

---

### SEMANA 3–4 | Unidad 1: Frontend — Bootstrap avanzado
**Sílabo:** Ventanas modales, formularios, elementos web avanzados con Bootstrap. Integración del framework de front end completo.
**Entregable del proyecto (APF1 — Semana 4):** Prototipos funcionales de las pantallas principales con Bootstrap

#### Tareas

- [ ] Crear formulario de registro de paciente con validación HTML5 + Bootstrap
- [ ] Crear formulario de agendamiento de cita con selector de especialidad, médico, fecha y hora
- [ ] Implementar modal de confirmación de cita (Bootstrap Modal)
- [ ] Crear tabla de listado de citas con paginación Bootstrap
- [ ] Diseñar dashboard con tarjetas KPI (Bootstrap Cards + Grid)
- [ ] Implementar barra de navegación diferenciada por rol (usando variables Thymeleaf)
- [ ] Validar diseño responsive en mobile (≥ 375px) y desktop

#### Componentes Bootstrap clave a utilizar

```html
<!-- Modal de confirmación de cita -->
<div class="modal fade" id="modalConfirmarCita" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header bg-primary text-white">
        <h5 class="modal-title">Confirmar Cita Médica</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <!-- Detalle de la cita -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-primary" id="btnConfirmar">Confirmar</button>
      </div>
    </div>
  </div>
</div>

<!-- Tabla de citas responsive -->
<div class="table-responsive">
  <table class="table table-hover table-striped align-middle">
    <thead class="table-primary">
      <tr>
        <th>N°</th>
        <th>Paciente</th>
        <th>Médico</th>
        <th>Especialidad</th>
        <th>Fecha y Hora</th>
        <th>Estado</th>
        <th>Acciones</th>
      </tr>
    </thead>
    <tbody th:each="cita, stat : ${citas}">
      <tr>
        <td th:text="${stat.count}"></td>
        <td th:text="${cita.paciente.nombres + ' ' + cita.paciente.apellidoPaterno}"></td>
        <td th:text="${cita.medico.nombres}"></td>
        <td th:text="${cita.especialidad.nombre}"></td>
        <td th:text="${#temporals.format(cita.fechaCita, 'dd/MM/yyyy HH:mm')}"></td>
        <td>
          <span class="badge"
                th:classappend="${cita.estado == 'PROGRAMADA'} ? 'bg-info' :
                                (${cita.estado == 'ATENDIDA'} ? 'bg-success' :
                                (${cita.estado == 'CANCELADA'} ? 'bg-danger' : 'bg-warning'))"
                th:text="${cita.estado}"></span>
        </td>
        <td>
          <a th:href="@{/citas/{id}(id=${cita.idCita})}" class="btn btn-sm btn-outline-primary">
            <i class="bi bi-eye"></i>
          </a>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

---

### SEMANA 5–6 | Unidad 2: Introducción a Spring Boot
**Sílabo:** Definición de back end, Spring Boot (configuración, creación de proyecto), Spring Web
**Entregable del proyecto:** Controladores MVC básicos + rutas protegidas, entidades JPA mapeadas a Oracle

#### Tareas

- [ ] Crear entidades JPA: `Usuario`, `Rol`, `Paciente`, `Especialidad`
- [ ] Configurar `spring.jpa.hibernate.ddl-auto=validate` (el DDL ya existe en Oracle)
- [ ] Implementar `HomeController` y `AuthController` (GET de login)
- [ ] Implementar `PacienteController` con listar y detalle (sin seguridad aún)
- [ ] Crear repositorios: `UsuarioRepository`, `PacienteRepository`, `EspecialidadRepository`
- [ ] Crear servicios: `PacienteService`
- [ ] Configurar manejo global de excepciones (`@ControllerAdvice`)

#### Entidad JPA — Paciente

```java
@Entity
@Table(name = "PACIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @Column(name = "DNI", unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "NOMBRES", nullable = false, length = 100)
    private String nombres;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50)
    private String apellidoMaterno;

    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "SEXO", nullable = false, length = 1)
    private String sexo;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ACTIVO")
    private Integer activo = 1;

    @Column(name = "FECHA_REGISTRO")
    private LocalDateTime fechaRegistro;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private HistoriaClinica historiaClinica;

    @OneToMany(mappedBy = "paciente")
    private List<Cita> citas;
}
```

#### Controlador MVC básico

```java
@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteService.findAll());
        model.addAttribute("pageTitle", "Gestión de Pacientes");
        return "pacientes/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));
        model.addAttribute("paciente", paciente);
        model.addAttribute("pageTitle", "Detalle del Paciente");
        return "pacientes/detalle";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("pageTitle", "Registrar Paciente");
        return "pacientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Paciente paciente,
                          BindingResult result,
                          RedirectAttributes attrs) {
        if (result.hasErrors()) return "pacientes/formulario";
        pacienteService.save(paciente);
        attrs.addFlashAttribute("mensaje", "Paciente registrado correctamente");
        return "redirect:/pacientes";
    }
}
```

---

### SEMANA 7–8 | Unidad 2: Thymeleaf + Spring Web
**Sílabo:** Thymeleaf (definición, usos, ventajas), integración Spring Web + Thymeleaf.
**Entregable del proyecto (APF2 — Semana 8):** Módulos de pacientes y citas con Thymeleaf completamente funcionales (CRUD sin seguridad)

#### Tareas

- [ ] Crear todas las vistas Thymeleaf para Pacientes (lista, detalle, formulario)
- [ ] Crear vistas para Médicos y Especialidades (lista, formulario)
- [ ] Implementar vista de agendamiento de cita (calendario de disponibilidad)
- [ ] Usar `th:each`, `th:if`, `th:href`, `th:action`, `th:object`, `th:field` correctamente
- [ ] Implementar fragmentos reutilizables: navbar, sidebar, footer, alertas
- [ ] Integrar validación de formularios: `BindingResult` en controlador + mensajes de error en Thymeleaf
- [ ] Implementar búsqueda de disponibilidad de médico en tiempo real (fetch API → endpoint REST)

#### Thymeleaf — Formulario con validación

```html
<form th:action="@{/pacientes/guardar}" th:object="${paciente}" method="post" class="needs-validation">
    <input type="hidden" th:field="*{idPaciente}">

    <div class="row g-3">
        <div class="col-md-4">
            <label class="form-label">DNI <span class="text-danger">*</span></label>
            <input type="text" class="form-control" th:field="*{dni}"
                   th:classappend="${#fields.hasErrors('dni')} ? 'is-invalid'"
                   maxlength="8" pattern="\d{8}" required>
            <div class="invalid-feedback" th:errors="*{dni}">Error en DNI</div>
        </div>

        <div class="col-md-4">
            <label class="form-label">Nombres <span class="text-danger">*</span></label>
            <input type="text" class="form-control" th:field="*{nombres}"
                   th:classappend="${#fields.hasErrors('nombres')} ? 'is-invalid'" required>
            <div class="invalid-feedback" th:errors="*{nombres}">Error en nombres</div>
        </div>

        <div class="col-md-4">
            <label class="form-label">Especialidad <span class="text-danger">*</span></label>
            <select class="form-select" th:field="*{especialidad.idEspecialidad}" required>
                <option value="">-- Seleccionar --</option>
                <option th:each="esp : ${especialidades}"
                        th:value="${esp.idEspecialidad}"
                        th:text="${esp.nombre}"></option>
            </select>
        </div>
    </div>

    <div class="mt-4 d-flex gap-2">
        <button type="submit" class="btn btn-primary">
            <i class="bi bi-save"></i> Guardar
        </button>
        <a th:href="@{/pacientes}" class="btn btn-secondary">Cancelar</a>
    </div>
</form>
```

---

### SEMANA 9–10 | Unidad 3: Spring con Base de Datos — ORM y JPA
**Sílabo:** ORM, Hibernate, Spring Data JPA con MySQL (en el proyecto se usa Oracle, mismos conceptos). CRUD completo con JPA.
**Entregable del proyecto:** Todas las entidades mapeadas a Oracle + CRUD completo de módulos core

#### Tareas

- [ ] Completar mapeo JPA de las 16 tablas del esquema Oracle
- [ ] Implementar repositorios con queries personalizadas (`@Query` JPQL/nativo)
- [ ] Implementar lógica de validación de disponibilidad de citas (RN-07: sin solapamientos)
- [ ] Implementar regla RN-02: validación de DNI único antes de registrar paciente
- [ ] Crear `CitaService` con método `verificarDisponibilidad(idMedico, fechaCita, horaInicio, horaFin)`
- [ ] Implementar `HistoriaClinicaService`: crear HC automáticamente al registrar paciente
- [ ] Configurar relaciones JPA: `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`

#### Repositorio con queries personalizadas

```java
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Citas de un médico en una fecha (para verificar disponibilidad)
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita = :fecha " +
           "AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIDA') " +
           "AND (c.horaInicio < :horaFin AND c.horaFin > :horaInicio)")
    List<Cita> findCitasSolapadas(@Param("idMedico") Long idMedico,
                                   @Param("fecha") LocalDate fecha,
                                   @Param("horaInicio") LocalTime horaInicio,
                                   @Param("horaFin") LocalTime horaFin);

    // Citas de un paciente (historial)
    List<Cita> findByPacienteIdPacienteOrderByFechaCitaDesc(Long idPaciente);

    // Citas del día para un médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita = :hoy " +
           "AND c.estado NOT IN ('CANCELADA') ORDER BY c.horaInicio")
    List<Cita> findAgendaDiaria(@Param("idMedico") Long idMedico,
                                 @Param("hoy") LocalDate hoy);

    // Citas sin check-in pasados 30 minutos (para job de no-show)
    @Query("SELECT c FROM Cita c WHERE c.estado = 'CONFIRMADA' " +
           "AND c.fechaCita = :hoy " +
           "AND c.horaInicio < :limite " +
           "AND c.fechaCheckin IS NULL")
    List<Cita> findCitasNoAsistidas(@Param("hoy") LocalDate hoy,
                                     @Param("limite") LocalTime limite);
}
```

#### Servicio con validación de disponibilidad

```java
@Service
@RequiredArgsConstructor
@Transactional
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final AuditoriaService auditoriaService;

    public Cita agendarCita(CitaDTO dto) {
        // RN-07: verificar disponibilidad
        List<Cita> solapadas = citaRepository.findCitasSolapadas(
            dto.getIdMedico(), dto.getFechaCita(),
            dto.getHoraInicio(), dto.getHoraFin());
        if (!solapadas.isEmpty()) {
            throw new DisponibilidadException("El médico no está disponible en ese horario");
        }

        // RN-10: verificar cita activa con mismo especialista en 7 días
        // (implementar query adicional)

        Cita cita = new Cita();
        cita.setPaciente(pacienteRepository.findById(dto.getIdPaciente()).orElseThrow());
        cita.setMedico(medicoRepository.findById(dto.getIdMedico()).orElseThrow());
        cita.setFechaCita(dto.getFechaCita());
        cita.setHoraInicio(dto.getHoraInicio());
        cita.setHoraFin(dto.getHoraFin());
        cita.setEstado("PROGRAMADA");
        cita.setCanalReserva(dto.getCanalReserva());
        cita.setCodigoQr(generarCodigoQR()); // UUID

        Cita saved = citaRepository.save(cita);
        auditoriaService.registrar("INSERT", "CITA", saved.getIdCita().toString(), "Cita agendada");
        return saved;
    }

    public void cancelarCita(Long idCita, String motivo) {
        Cita cita = citaRepository.findById(idCita).orElseThrow();
        // RN-08: verificar 24 horas de anticipación
        LocalDateTime limiteCancel = LocalDateTime.of(cita.getFechaCita(), cita.getHoraInicio())
                                                   .minusHours(24);
        if (LocalDateTime.now().isAfter(limiteCancel)) {
            throw new BusinessException("No se puede cancelar con menos de 24 horas de anticipación");
        }
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }

    private String generarCodigoQR() {
        return java.util.UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 12);
    }
}
```

---

### SEMANA 11–12 | Unidad 3: Spring Validator + JPA avanzado
**Sílabo:** Spring Validator, relaciones entre tablas, validación de datos, restricciones, CRUD con validación.
**Entregable del proyecto (APF3 — Semana 12):** Todos los módulos CRUD validados + jobs automáticos funcionando

#### Tareas

- [ ] Implementar Bean Validation (`@NotBlank`, `@Size`, `@Pattern`, `@NotNull`) en todas las entidades
- [ ] Crear validadores customizados: `@ValidDni`, `@FechaFutura`, `@HorarioDisponible`
- [ ] Implementar `@Scheduled` jobs: recordatorios 24h + marcado automático de no-show (RN-11, RN-12)
- [ ] Completar módulo de Historia Clínica: registro de atención + diagnóstico CIE-10
- [ ] Completar módulo de Recetas: emisión con campos DIGEMID (RN-15)
- [ ] Implementar módulo de Órdenes de Examen
- [ ] Implementar módulo de Pagos: registro en caja + integración básica en línea
- [ ] Implementar API REST para el agendamiento (consume desde JavaScript del portal)

#### Validador customizado — @ValidDni

```java
@Documented
@Constraint(validatedBy = DniValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDni {
    String message() default "El DNI debe contener exactamente 8 dígitos numéricos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class DniValidator implements ConstraintValidator<ValidDni, String> {
    @Override
    public boolean isValid(String dni, ConstraintValidatorContext ctx) {
        if (dni == null) return false;
        return dni.matches("^\\d{8}$");
    }
}
```

#### Scheduler — Jobs automáticos

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class CitaScheduler {

    private final CitaRepository citaRepository;
    private final NotificacionService notificacionService;

    // RN-11: marcar NO_ASISTIDA 30 minutos después de la hora programada
    @Scheduled(fixedRate = 60000) // cada minuto
    public void marcarNoAsistidas() {
        LocalDate hoy = LocalDate.now();
        LocalTime limite = LocalTime.now().minusMinutes(30);
        List<Cita> pendientes = citaRepository.findCitasNoAsistidas(hoy, limite);
        pendientes.forEach(cita -> {
            cita.setEstado("NO_ASISTIDA");
            citaRepository.save(cita);
            log.info("Cita {} marcada como NO_ASISTIDA", cita.getIdCita());
        });
    }

    // RN-10 (recordatorio 24h antes): se ejecuta a las 8:00 AM todos los días
    @Scheduled(cron = "0 0 8 * * *")
    public void enviarRecordatorios() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Cita> citasManana = citaRepository.findByFechaCitaAndEstado(manana, "CONFIRMADA");
        citasManana.forEach(cita ->
            notificacionService.enviarRecordatorio(cita));
        log.info("Recordatorios enviados: {} citas", citasManana.size());
    }
}
```

#### REST Controller — Disponibilidad de agenda (para el portal)

```java
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaRestController {

    private final CitaService citaService;
    private final HorarioMedicoService horarioService;

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<SlotDisponibleDTO>> getDisponibilidad(
            @RequestParam Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<SlotDisponibleDTO> slots = horarioService.calcularSlots(idMedico, fecha);
        return ResponseEntity.ok(slots);
    }

    @PostMapping
    public ResponseEntity<CitaDTO> agendar(@Valid @RequestBody CitaDTO dto) {
        Cita cita = citaService.agendarCita(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CitaDTO.fromEntity(cita));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id,
                                          @RequestBody Map<String,String> body) {
        citaService.cancelarCita(id, body.get("motivo"));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<CitaDTO> checkin(@PathVariable Long id,
                                            @RequestParam String codigoQr) {
        Cita cita = citaService.realizarCheckin(id, codigoQr);
        return ResponseEntity.ok(CitaDTO.fromEntity(cita));
    }
}
```

---

### SEMANA 13–14 | Unidad 4: Spring Security — Autenticación y Autorización
**Sílabo:** Introducción a Spring Security, tipos de validación, autenticación y autorización por roles
**Entregable del proyecto:** Login funcional con Spring Security, acceso controlado por rol en todas las rutas

#### Tareas

- [ ] Configurar `SecurityConfig` con reglas de acceso por rol para cada ruta
- [ ] Implementar `UserDetailsServiceImpl` que carga el usuario desde Oracle
- [ ] Implementar encriptación BCrypt para contraseñas (RNF-05)
- [ ] Proteger endpoints REST y vistas MVC según rol
- [ ] Implementar registro de intentos fallidos y bloqueo de cuenta
- [ ] Implementar cierre de sesión automático a los 15 minutos (RNF-09)
- [ ] Usar `sec:authorize` en Thymeleaf para mostrar/ocultar elementos según rol

#### SecurityConfig

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/img/**").permitAll()
                // Portal de pacientes
                .requestMatchers("/portal/**").hasRole("PACIENTE")
                // Recepción
                .requestMatchers("/admision/**", "/citas/**").hasAnyRole("RECEPCIONISTA", "ADMINISTRADOR")
                // Médicos
                .requestMatchers("/agenda/**", "/atencion/**").hasAnyRole("MEDICO", "ADMINISTRADOR")
                // Administración
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                // Gerencia
                .requestMatchers("/reportes/**", "/dashboard/**").hasAnyRole("GERENTE", "ADMINISTRADOR")
                // API REST (JWT)
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/inicio", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            )
            .rememberMe(me -> me.tokenValiditySeconds(86400)); // 24h
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder())
                   .and().build();
    }
}
```

#### UserDetailsServiceImpl

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (usuario.getBloqueado() == 1) {
            throw new LockedException("Cuenta bloqueada por múltiples intentos fallidos");
        }

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(),
            usuario.getPasswordHash(),
            usuario.getActivo() == 1,
            true, true,
            usuario.getBloqueado() == 0,
            authorities
        );
    }
}
```

#### Thymeleaf con Spring Security

```html
<!-- Mostrar sección solo si el usuario tiene rol MEDICO -->
<div sec:authorize="hasRole('MEDICO')" class="card">
    <div class="card-header">Mi Agenda</div>
    <!-- contenido -->
</div>

<!-- Mostrar nombre del usuario autenticado -->
<span class="navbar-text" sec:authentication="name"></span>

<!-- Formulario de logout -->
<form th:action="@{/logout}" method="post">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
    <button type="submit" class="btn btn-outline-danger btn-sm">
        <i class="bi bi-box-arrow-right"></i> Cerrar sesión
    </button>
</form>
```

---

### SEMANA 15–16 | Unidad 4: JWT + Integración Frontend-Backend
**Sílabo:** JWT (definición, usos, implementación con Spring Security), integración completa frameworks frontend y backend
**Entregable del proyecto:** Portal de pacientes con agendamiento vía JWT + checkin QR + notificaciones

#### Tareas

- [ ] Implementar JWT para las APIs REST consumidas desde el portal de pacientes
- [ ] Crear `JwtFilter` que intercepta y valida el token en cada petición a `/api/**`
- [ ] Implementar endpoint `POST /api/auth/login` que devuelve el token JWT
- [ ] Integrar JavaScript (Fetch API) en el portal de pacientes para consumir la API de citas
- [ ] Implementar lectura de QR en el módulo de admisión (librería JS: `html5-qrcode`)
- [ ] Implementar `NotificacionService` con envío de email (Spring Mail / Jakarta Mail)
- [ ] Integrar mapa de disponibilidad del médico (JS dinámico con AJAX → `/api/citas/disponibilidad`)
- [ ] Implementar generación de PDF para recetas (iText o JasperReports)

#### JWT Filter

```java
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(req, res);
    }
}
```

#### JavaScript — Agendamiento con Fetch API

```javascript
// portal/agenda.js
async function cargarDisponibilidad(idMedico, fecha) {
    const token = localStorage.getItem('jwt_token');
    const response = await fetch(`/api/citas/disponibilidad?idMedico=${idMedico}&fecha=${fecha}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!response.ok) throw new Error('Error al cargar disponibilidad');
    const slots = await response.json();
    renderizarSlots(slots);
}

function renderizarSlots(slots) {
    const container = document.getElementById('slotsContainer');
    container.innerHTML = '';
    slots.forEach(slot => {
        const btn = document.createElement('button');
        btn.className = `btn btn-sm ${slot.disponible ? 'btn-outline-primary' : 'btn-secondary disabled'} m-1`;
        btn.textContent = slot.hora;
        if (slot.disponible) {
            btn.onclick = () => seleccionarSlot(slot);
        }
        container.appendChild(btn);
    });
}

async function confirmarCita(dto) {
    const token = localStorage.getItem('jwt_token');
    const response = await fetch('/api/citas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(dto)
    });
    if (response.ok) {
        const cita = await response.json();
        mostrarConfirmacion(cita);
    } else {
        const error = await response.json();
        mostrarError(error.mensaje);
    }
}
```

---

### SEMANA 17–18 | Proyecto Final
**Sílabo:** Presentación del Proyecto Final (semanas 17 y 18)
**Entregable del proyecto:** Sistema completo funcionando + documentación técnica + exposición

#### Checklist de entrega final

**Módulos implementados**
- [ ] Autenticación con Spring Security + JWT (login, roles, sesión)
- [ ] Módulo Pacientes: registro, búsqueda, perfil (CRUD completo)
- [ ] Módulo Citas: agendamiento web 24/7, cancelación, reprogramación, check-in QR
- [ ] Módulo Admisión: recepción con check-in DNI/QR, asignación de turno
- [ ] Módulo Atención Médica: historia clínica electrónica, diagnóstico CIE-10, receta digital
- [ ] Módulo Médicos y Horarios: gestión de agenda semanal
- [ ] Módulo Pagos: registro en caja y pago en línea
- [ ] Módulo Dashboard Gerencial: KPIs en tiempo real, exportación PDF/Excel
- [ ] Módulo Auditoría: log completo de acciones críticas (RF-28)
- [ ] Jobs automáticos: recordatorios 24h, marcado de no-show, restricción por ausencias

**Validaciones implementadas**
- [ ] Todos los formularios con Bean Validation + mensajes en español
- [ ] Unicidad de DNI (RN-02)
- [ ] Verificación de solapamiento de citas (RN-07)
- [ ] Validación DIGEMID en recetas (RN-15)
- [ ] Control de acceso por roles en todas las rutas

**Seguridad**
- [ ] BCrypt para contraseñas (RNF-05)
- [ ] HTTPS configurado (RNF-06)
- [ ] Cierre de sesión a 15 minutos (RNF-09)
- [ ] CSRF protection habilitado
- [ ] Log de auditoría completo

**Calidad de código**
- [ ] Arquitectura en capas respetada (Controller → Service → Repository → Entity)
- [ ] No hay lógica de negocio en los controladores
- [ ] DTOs separados de las entidades JPA
- [ ] Manejo global de excepciones con `@ControllerAdvice`
- [ ] Comentarios JavaDoc en clases de servicio

---

## 10. Historias de Usuario

| ID | Como... | Quiero... | Para... | Criterios de aceptación |
|----|---------|-----------|---------|------------------------|
| HU-01 | Paciente | Agendar cita desde la web a cualquier hora | No ir presencialmente ni llamar | Ver especialidades → médicos → horarios libres → confirmar en < 3 clics → recibir confirmación por email/SMS con QR |
| HU-02 | Paciente | Recibir recordatorio automático antes de mi cita | No olvidarme ni llegar tarde | Email + SMS 24h antes con nombre del médico, fecha, hora y consultorio; opción de cancelar desde el recordatorio |
| HU-03 | Paciente | Cancelar o reprogramar desde el portal | No quedar mal si me surge algo | Ver citas próximas → cancelar/reprogramar hasta 24h antes sin penalidad → cupo liberado automáticamente → confirmación |
| HU-04 | Paciente | Hacer check-in escaneando QR o DNI | No hacer cola en recepción | Sistema me identifica → asigna número de turno → indica consultorio → si no tengo cita, deriva a recepción |
| HU-05 | Médico | Ver mi agenda del día al iniciar sesión | Preparar las atenciones con anticipación | Lista de pacientes del día con nombre, hora y motivo → clic en paciente abre su HC completa |
| HU-06 | Médico | Registrar la atención y emitir receta digital | Que todo quede documentado sin papeles | Registrar motivo, anamnesis, examen físico, diagnóstico CIE-10, tratamiento → emitir receta con campos DIGEMID → firma automática con CMP y fecha-hora |
| HU-07 | Gerente | Ver dashboard con KPIs del centro | Tomar decisiones con datos reales | KPIs de citas atendidas, no-show, ingresos, productividad → actualizados en tiempo real → filtrables → exportables a PDF/Excel |
| HU-08 | Administrador | Gestionar usuarios y permisos | Que cada quien acceda solo a lo que necesita | CRUD de usuarios → asignación de roles → contraseñas en BCrypt → log de cambios del administrador |

---

## 11. Estructura de Carpetas del Proyecto

```
trinidad-medical/
├── src/
│   ├── main/
│   │   ├── java/com/trinidad/medical/
│   │   │   ├── TrinidadMedicalApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   └── SchedulingConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── PacienteController.java
│   │   │   │   ├── CitaController.java
│   │   │   │   ├── AtencionController.java
│   │   │   │   ├── MedicoController.java
│   │   │   │   ├── AdministradorController.java
│   │   │   │   ├── GerenciaController.java
│   │   │   │   └── rest/
│   │   │   │       ├── CitaRestController.java
│   │   │   │       ├── AuthRestController.java
│   │   │   │       └── DisponibilidadRestController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── PacienteService.java
│   │   │   │   ├── CitaService.java
│   │   │   │   ├── AtencionService.java
│   │   │   │   ├── MedicoService.java
│   │   │   │   ├── HorarioService.java
│   │   │   │   ├── HistoriaClinicaService.java
│   │   │   │   ├── RecetaService.java
│   │   │   │   ├── PagoService.java
│   │   │   │   ├── NotificacionService.java
│   │   │   │   ├── ReporteService.java
│   │   │   │   ├── AuditoriaService.java
│   │   │   │   └── JwtService.java
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── PacienteRepository.java
│   │   │   │   ├── CitaRepository.java
│   │   │   │   ├── MedicoRepository.java
│   │   │   │   ├── EspecialidadRepository.java
│   │   │   │   ├── HorarioMedicoRepository.java
│   │   │   │   ├── HistoriaClinicaRepository.java
│   │   │   │   ├── AtencionRepository.java
│   │   │   │   ├── RecetaRepository.java
│   │   │   │   ├── PagoRepository.java
│   │   │   │   └── AuditoriaLogRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Paciente.java
│   │   │   │   ├── Medico.java
│   │   │   │   ├── Especialidad.java
│   │   │   │   ├── HorarioMedico.java
│   │   │   │   ├── HistoriaClinica.java
│   │   │   │   ├── Cita.java
│   │   │   │   ├── Atencion.java
│   │   │   │   ├── Receta.java
│   │   │   │   ├── DetalleReceta.java
│   │   │   │   ├── OrdenExamen.java
│   │   │   │   ├── DiagnosticoCie10.java
│   │   │   │   ├── Pago.java
│   │   │   │   └── AuditoriaLog.java
│   │   │   ├── dto/
│   │   │   │   ├── CitaDTO.java
│   │   │   │   ├── PacienteDTO.java
│   │   │   │   ├── MedicoDTO.java
│   │   │   │   ├── AtencionDTO.java
│   │   │   │   ├── SlotDisponibleDTO.java
│   │   │   │   ├── DashboardKpiDTO.java
│   │   │   │   └── JwtResponseDTO.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   ├── scheduler/
│   │   │   │   └── CitaScheduler.java
│   │   │   ├── validation/
│   │   │   │   ├── ValidDni.java
│   │   │   │   └── DniValidator.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── BusinessException.java
│   │   │       ├── DisponibilidadException.java
│   │   │       └── EntityNotFoundException.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── sql/
│   │       │   ├── 01_create_tables.sql
│   │       │   ├── 02_sequences_triggers.sql
│   │       │   └── 03_data_inicial.sql
│   │       ├── templates/
│   │       │   ├── fragments/
│   │       │   │   ├── layout.html
│   │       │   │   ├── navbar.html
│   │       │   │   ├── sidebar.html
│   │       │   │   └── footer.html
│   │       │   ├── auth/
│   │       │   │   └── login.html
│   │       │   ├── pacientes/
│   │       │   │   ├── lista.html
│   │       │   │   ├── detalle.html
│   │       │   │   └── formulario.html
│   │       │   ├── citas/
│   │       │   │   ├── lista.html
│   │       │   │   ├── agendar.html
│   │       │   │   └── detalle.html
│   │       │   ├── atencion/
│   │       │   │   ├── formulario.html
│   │       │   │   └── receta.html
│   │       │   ├── medicos/
│   │       │   │   ├── lista.html
│   │       │   │   ├── formulario.html
│   │       │   │   └── agenda.html
│   │       │   ├── portal/
│   │       │   │   ├── inicio.html
│   │       │   │   ├── mis-citas.html
│   │       │   │   └── nueva-cita.html
│   │       │   ├── admision/
│   │       │   │   └── checkin.html
│   │       │   ├── dashboard/
│   │       │   │   └── gerencia.html
│   │       │   └── error/
│   │       │       ├── 403.html
│   │       │       └── 404.html
│   │       └── static/
│   │           ├── css/
│   │           │   └── trinidad.css
│   │           ├── js/
│   │           │   ├── trinidad.js
│   │           │   ├── agenda.js
│   │           │   ├── checkin.js
│   │           │   └── dashboard.js
│   │           └── img/
│   │               └── logo-trinidad.png
│   └── test/
│       └── java/com/trinidad/medical/
│           ├── service/
│           │   ├── CitaServiceTest.java
│           │   └── PacienteServiceTest.java
│           └── controller/
│               └── CitaControllerTest.java
└── pom.xml
```

---

## 12. Convenciones de Código

### Nomenclatura

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| Clases Java | PascalCase | `CitaService`, `PacienteController` |
| Métodos Java | camelCase | `agendarCita()`, `findById()` |
| Variables | camelCase | `idPaciente`, `fechaCita` |
| Constantes | UPPER_SNAKE_CASE | `MAX_INTENTOS_LOGIN = 3` |
| Tablas Oracle | UPPER_SNAKE_CASE | `HISTORIA_CLINICA`, `DETALLE_RECETA` |
| Columnas Oracle | UPPER_SNAKE_CASE | `ID_PACIENTE`, `FECHA_ATENCION` |
| URLs (rutas web) | kebab-case | `/mis-citas`, `/nueva-cita` |
| Templates Thymeleaf | kebab-case | `nueva-cita.html`, `lista-pacientes.html` |

### Reglas de arquitectura

1. Los **controladores** solo llaman a servicios, nunca a repositorios directamente.
2. Los **servicios** contienen toda la lógica de negocio y las validaciones de las reglas de negocio (RN-01 a RN-18).
3. Los **repositorios** solo contienen queries. Nada de lógica.
4. Las **entidades JPA** no se pasan directamente a las vistas Thymeleaf: usar DTOs.
5. Todo acceso a datos clínicos sensibles queda registrado en `AUDITORIA_LOG`.
6. Los **tests unitarios** cubren al menos todos los métodos de `CitaService` y `PacienteService`.

### Commits de Git

```
feat: implementar agendamiento de cita desde portal web
fix: corregir validación de solapamiento de horarios
docs: agregar JavaDoc a CitaService
test: agregar pruebas unitarias para cancelarCita
style: aplicar formato Bootstrap en formulario de paciente
refactor: extraer lógica de notificaciones a NotificacionService
```

---

*Proyecto académico — Marcos de Desarrollo Web (100000S52T) | UTP 2026 - Ciclo 1*
*Docente: Melquiades Efraín Melgarejo Graciano | Grupo 4*
