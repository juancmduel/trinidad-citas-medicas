# 🏥 Trinidad Citas Médicas

Sistema integral de gestión de citas médicas desarrollado para **Trinidad y Especialidades Médicas S.A.C.**

El sistema cubre todo el flujo de atención: desde que el paciente agenda su cita por web, recibe un correo de confirmación con código QR, hasta que es atendido por el médico, recibe su diagnóstico (CIE-10), receta electrónica y órdenes de examen. Incluye recordatorios automáticos por correo, triaje, pagos, reportes gerenciales y más.

---

## ✨ Funcionalidades

| Módulo | Descripción |
|---|---|
| **Citas Médicas** | Agenda, confirma, cancela y reprograma citas. Validación de horarios disponibles. |
| **Portal Paciente** | Los pacientes se registran, agendan sus citas y ven su historial. |
| **Triaje** | Enfermería registra signos vitales y nivel de urgencia. |
| **Atención Médica** | El médico registra diagnóstico, tratamiento, recetas y órdenes. |
| **Recetas Digitales** | Receta electrónica con medicamentos, dosis y duración. |
| **Pagos** | Registro de pagos en efectivo, tarjeta, Yape, Plin, transferencia. |
| **Reportes** | Dashboards con estadísticas de citas, ingresos, inasistencias. |
| **Correos Automáticos** | Confirmación al agendar, recordatorio 24h antes, recuperación de contraseña. |
| **Código QR** | Cada cita genera un QR que el paciente muestra en recepción. |
| **Seguridad** | JWT para API, sesiones para web, roles granulares, bloqueo por intentos fallidos. |
| **Modo Oscuro** | Tema claro/oscuro con persistencia en localStorage. |
| **Auditoría** | Registro de cada acción importante del sistema. |

---

## 🛠️ Tecnologías

| Capa | Tecnología |
|---|---|
| **Backend** | Java 21+, Spring Boot 3.2.6, Spring Security 6, Spring Data JPA |
| **Base de datos** | SQL Server (principal), Oracle 21c XE, H2 (desarrollo) |
| **Frontend** | Thymeleaf, Bootstrap 5, CSS nativo con variables, modo oscuro |
| **API** | REST con JWT (jjwt), documentación OpenAPI/Swagger |
| **Construcción** | Maven + Wrapper (mvnw.cmd) |
| **Correo** | Spring Mail + Gmail SMTP, HTML responsivo, códigos QR (ZXing) |
| **Extras** | Lombok, BCrypt, ZXing, Jackson, HikariCP |

---

## ⚙️ Requisitos

- **Java** 21 o superior (probado con Java 25)
- **Maven** 3.9+ (o usa el wrapper `.\mvnw.cmd`)
- **SQL Server** (o alternativamente Oracle / H2)

---

## 🚀 Instalación Rápida

### 1. Clonar y preparar variables de entorno

Copia el archivo `.env` (ya incluido en el proyecto) y ajústalo:

```ini
DB_PASSWORD=TuPasswordDeSQLServer
MAIL_USERNAME=tu-correo@gmail.com
MAIL_PASSWORD=tu-contraseña-de-aplicacion
JWT_SECRET=openssl rand -base64 32  # genera una clave de 32 bytes
TRINIDAD_SEED_USER_PASSWORD=Trinidad2026
CORS_ALLOWED_ORIGINS=
```

> ⚠️ Para Gmail necesitas una **Contraseña de Aplicación** (no tu contraseña normal). Se genera en: https://myaccount.google.com/apppasswords

### 2. Preparar la base de datos

Ejecuta en **SSMS** o **sqlcmd** en este orden:

```sql
CREATE DATABASE clinica_trinidad;
```

Luego ejecuta los scripts:
1. `src/main/resources/db/sqlserver/01_schema.sql` → crea 21 tablas + secuencias
2. `src/main/resources/db/sqlserver/02_seed.sql` → carga datos de prueba

### 3. Levantar el proyecto

```powershell
.\run.ps1
```

Esto carga automáticamente las variables del `.env` y ejecuta `mvnw.cmd spring-boot:run`.

### 4. Acceder

| Recurso | URL |
|---|---|
| **Web App** | http://localhost:8070/trinidad |
| **Login** | http://localhost:8070/trinidad/login |
| **API Swagger** | http://localhost:8070/trinidad/swagger-ui.html |
| **H2 Console** | http://localhost:8070/trinidad/h2-console |

---

## 🔑 Usuarios de Prueba (seed data)

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Trinidad2026` | ADMINISTRADOR |
| `gerente` | `Trinidad2026` | GERENTE |
| `recepcion` | `Trinidad2026` | RECEPCIONISTA |
| `enfermera` | `Trinidad2026` | ENFERMERA |
| `dr.garcia` | `Trinidad2026` | MEDICO (Medicina General) |
| `dra.lopez` | `Trinidad2026` | MEDICO (Pediatría) |
| `paciente1` | `Trinidad2026` | PACIENTE |

La contraseña se define desde la variable de entorno `TRINIDAD_SEED_USER_PASSWORD`.

---

## 🌐 Perfiles de Base de Datos

El proyecto soporta 3 perfiles intercambiables:

| Perfil | BD | Cómo activarlo |
|---|---|---|
| **sqlserver** ✅ (default) | SQL Server | `spring.profiles.active=sqlserver,api,web` |
| **oracle** | Oracle 21c XE | `spring.profiles.active=oracle,api,web` |
| **dev** | H2 (archivo local) | `spring.profiles.active=dev,api,web` |

Para cambiar de perfil, edita `application.properties` línea 18.

---

## 📧 Sistema de Correos

| Tipo | Cuándo se envía | Método |
|---|---|---|
| **Confirmación de cita** | Al agendar | `EmailService.enviarConfirmacionCita()` |
| **Recordatorio 24h** | Diario a las 8:00 AM | `CitaScheduler` (cron) |
| **Recuperación de contraseña** | Al solicitar "Olvidé mi contraseña" | `PasswordResetService` |

Los correos incluyen diseño HTML responsivo con la imagen de Trinidad Salud y código QR.

---

## 🔒 Seguridad

- **Bloqueo de cuentas**: 5 intentos fallidos en 30 minutos → la cuenta se bloquea automáticamente
- **Desbloqueo**: Un administrador puede desbloquear desde `Usuarios → 🔓` o el usuario se desbloquea solo al iniciar sesión correctamente
- **Contraseñas**: encriptadas con BCrypt (fuerza 10)
- **JWT**: tokens firmados con HMAC-SHA256, expiran en 1 hora (refresh en 24h)
- **CORS**: configurable desde variable de entorno
- **Rate limiting**: filtro que limita peticiones a la API
- **Auditoría**: cada login (exitoso o fallido) se registra en `INTENTO_LOGIN`

---

## 🏗️ Estructura del Proyecto

```
trinidad-citas-medicas/
├── run.ps1                          # Script para levantar con variables de entorno
├── .env                              # Variables de entorno locales
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/trinidad/citas/
    │   │   ├── TrinidadCitasApplication.java
    │   │   ├── audit/                # Anotación @Auditable + aspecto
    │   │   ├── config/               # Security, JWT, CORS, DataInitializer
    │   │   ├── controller/
    │   │   │   ├── api/              # 23 controladores REST (JSON)
    │   │   │   └── web/              # Controladores MVC (Thymeleaf)
    │   │   ├── dto/                  # Data Transfer Objects
    │   │   ├── exception/            # Manejador global de errores
    │   │   ├── model/                # 22 entidades JPA
    │   │   ├── repository/           # 21 repositorios Spring Data
    │   │   ├── scheduler/            # Tareas programadas (recordatorios)
    │   │   ├── security/             # UserPrincipal, RateLimitingFilter
    │   │   ├── service/              # 27 servicios de negocio
    │   │   └── validation/           # Validaciones personalizadas
    │   └── resources/
    │       ├── application.properties
    │       ├── application-{perfil}.properties
    │       ├── db/
    │       │   ├── sqlserver/        # Schema + seed para SQL Server
    │       │   ├── oracle/           # Schema + seed para Oracle
    │       │   └── h2/               # Schema + seed para H2
    │       ├── static/
    │       │   ├── css/              # system.css, trinidad.css, login.css
    │       │   └── js/               # login-bg.js
    │       └── templates/
    │           ├── auth/             # login, registro, olvide-password
    │           ├── portal/           # Portal del paciente
    │           └── ...               # Vistas por módulo
    └── test/
        └── java/com/trinidad/citas/
            ├── controller/           # Tests de controladores
            └── service/              # Tests de servicios
```

---

## 📋 Estados de una Cita

```
PROGRAMADA → CONFIRMADA → EN_TRIAGE → EN_ATENCION → ATENDIDA
                                  ↘            ↘
                                  CANCELADA    NO_ASISTIO
```

---

## 🐳 Perfiles Disponibles para Arranque Rápido

```powershell
# SQL Server (default)
.\run.ps1

# Con Maven wrapper directamente:
.\mvnw.cmd spring-boot:run

# Con perfil específico:
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev,api,web
```
