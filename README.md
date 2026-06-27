# Trinidad Citas Médicas

Sistema Integral de Gestión de Citas Médicas desarrollado para **Trinidad y Especialidades Médicas S.A.C.**

El sistema permite gestionar todo el flujo de atención médica: desde la programación de horarios de los médicos, pasando por la reserva de citas de los pacientes, hasta el registro del pago, la atención en consultorio, el diagnóstico (basado en CIE-10) y la generación de la receta médica. 

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.2.5
- **Persistencia**: Spring Data JPA, Hibernate, Microsoft SQL Server
- **Seguridad**: Spring Security 6, JWT (JSON Web Tokens) para API, Session Cookie para vistas MVC
- **Frontend (UI)**: Thymeleaf, Bootstrap 5 (WebJars), HTML5, CSS3 Nativo (Variables CSS, Soporte Light/Dark Theme)
- **Construcción**: Maven (`mvn` / `mvnd`)
- **Librerías Adicionales**: Lombok, ZXing (Código QR), JJWT

## ✨ Características Principales

*   **Arquitectura Limpia**: Separación en capas (Controllers, Services, Repositories, DTOs).
*   **Diseño Visual Moderno**: Sistema de estilos basado en clases de utilidad y componentes (Glassmorphism, Dark Mode, Cards) a través de `system.css` y `trinidad.css`.
*   **Sistema de Seguridad Robusto**:
    *   Soporte híbrido de autenticación (JWT para REST APIs y Cookies para la plataforma Web).
    *   Control de accesos basado en Roles: `ADMINISTRADOR`, `GERENTE`, `MEDICO`, `RECEPCIONISTA` y `PACIENTE`.
    *   Mecanismo de **bloqueo automático** de cuentas de usuario tras 5 intentos fallidos de inicio de sesión en un lapso de 30 minutos, incluyendo auditoría detallada de los intentos.
*   **Módulos del Sistema**:
    *   Gestión de Usuarios, Roles y Auditoría de Seguridad.
    *   Mantenimiento de Médicos, Especialidades y Horarios.
    *   Gestión de Pacientes.
    *   Reserva y confirmación de Citas Médicas.
    *   Módulo de Caja/Pagos.
    *   Módulo de Atención Médica, Diagnósticos CIE-10 y Recetas.

## ⚙️ Requisitos Previos

*   [Java Development Kit (JDK) 17](https://adoptium.net/)
*   [Apache Maven](https://maven.apache.org/) o [Maven Daemon (mvnd)](https://github.com/apache/maven-mvnd)
*   Microsoft SQL Server (Local o Remoto)

## 🚀 Instalación y Configuración

### 1. Preparar la Base de Datos

El proyecto **no delega** la creación del esquema completo a Hibernate por motivos de optimización y uso de características específicas del motor (como Secuencias y Constraints). 

Antes de levantar el proyecto, debes crear la base de datos y ejecutar los scripts SQL proporcionados.
Abre SSMS (SQL Server Management Studio) o usa `sqlcmd` y ejecuta:

1.  Crea la base de datos: `CREATE DATABASE clinica_trinidad;`
2.  Ejecuta el script de estructura: `src/main/resources/db/sqlserver/01_schema.sql`
3.  Ejecuta el script de datos semilla: `src/main/resources/db/sqlserver/02_seed.sql`

### 2. Configurar Variables de Entorno

El sistema requiere variables de entorno por seguridad, para no exponer credenciales en el código fuente:

*   `DB_PASSWORD`: La contraseña del usuario `sa` de tu SQL Server local.
*   `JWT_SECRET`: Una cadena de texto larga (al menos 32 caracteres) utilizada para firmar los tokens JWT.

### 3. Levantar la Aplicación

Abre tu terminal en la raíz del proyecto y ejecuta el siguiente comando (sustituye las variables de ejemplo por las tuyas):

**En Windows (CMD):**
```cmd
set DB_PASSWORD="su password" && mvnd spring-boot:run
```

**En Windows (PowerShell):**
```powershell
$env:DB_PASSWORD="su password"; mvnd spring-boot:run
```

### 4. Acceder al Sistema

Una vez la aplicación esté corriendo, abre tu navegador en:
`http://localhost:8081/trinidad`

## 🔑 Credenciales por Defecto (Seed Data)

Tras ejecutar `02_seed.sql`, se crean los siguientes usuarios de prueba (La contraseña para **todos** es `Trinidad2026`):

| Usuario | Rol | Descripción |
| :--- | :--- | :--- |
| `admin` | ADMINISTRADOR | Acceso total al sistema, auditorías, configuración. |
| `gerente` | GERENTE | Dashboards y reportes gerenciales. |
| `medico1` | MEDICO | Atenciones, recetas, historial clínico (Cardiología). |
| `recepcion` | RECEPCIONISTA | Gestión de citas y cobros. |
| `paciente1` | PACIENTE | Acceso al portal de pacientes. |

## 🏗️ Estructura del Proyecto

*   `/src/main/java/com/trinidad/citas/`
    *   `config/`: Configuraciones de Spring Security, JWT, Web MVC.
    *   `controller/`: 
        *   `api/`: Controladores REST (Retornan JSON).
        *   `web/`: Controladores Spring MVC (Retornan Vistas Thymeleaf).
    *   `dto/`: Data Transfer Objects para envío/recepción de datos limpios a las vistas.
    *   `exception/`: Manejo global de excepciones (`@ControllerAdvice`).
    *   `model/`: Entidades JPA.
    *   `repository/`: Interfaces de Spring Data JPA.
    *   `service/`: Capa de lógica de negocio transaccional.
*   `/src/main/resources/`
    *   `db/sqlserver/`: Scripts SQL de migración y seed.
    *   `static/css/`: Hojas de estilo modernas (`system.css`, `trinidad.css`, `login.css`, `landing.css`).
    *   `templates/`: Vistas de Thymeleaf organizadas por módulos.
