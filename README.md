# 🏥 Trinidad - Sistema de Gestión de Citas Médicas

Sistema de gestión de citas médicas para **Trinidad & Especialidades Médicas S.A.C.** (Tarapoto, San Martín - Perú).

---

## 🚀 Cómo levantar el proyecto (RÁPIDO - sin Oracle)

El proyecto trae configurado un perfil **`dev`** con base de datos **H2 en memoria** que se crea y se llena automáticamente al arrancar. **No necesitas instalar Oracle** para empezar a probarlo.

### Requisitos previos

- **Java 17** (JDK)
- **Maven 3.9+** (o usar el wrapper `./mvnw` incluido)
- **Visual Studio Code** con las extensiones:
  - Extension Pack for Java
  - Spring Boot Extension Pack

### Pasos

1. **Cambiar el perfil a H2** en `src/main/resources/application.properties`:
   ```properties
   spring.profiles.active=dev,api,web
   ```
   *(el perfil por defecto usa Oracle; cámbialo a `dev` para usar H2)*

2. **Abrir el proyecto en VS Code:** `File > Open Folder...` y selecciona la carpeta `trinidad-citas-medicas`.
3. **Esperar a que Maven descargue las dependencias** (verás el spinner de "Resolving Maven dependencies" abajo a la derecha; tarda 1-3 minutos la primera vez).
4. **Ejecutar la aplicación** de cualquiera de estas formas:

   **Opción A - Desde VS Code:**
   - Abre `TrinidadCitasApplication.java`
   - Click derecho > `Run Java`

   **Opción B - Desde terminal:**
   ```bash
   mvn spring-boot:run
   ```

4. **Abrir en el navegador:**
   - 🌐 Aplicación: http://localhost:8081/trinidad
   - 🛠️ Consola H2 (BD en memoria): http://localhost:8081/trinidad/h2-console
     - JDBC URL: `jdbc:h2:mem:trinidaddb`
     - Usuario: `sa`
     - Sin contraseña

---

## 👥 Usuarios de prueba (cargados automáticamente)

| Usuario | Password | Rol |
|---|---|---|
| `admin` | `admin123` | ADMINISTRADOR |
| `gerente` | `admin123` | GERENTE |
| `recepcion` | `admin123` | RECEPCIONISTA |
| `dr.garcia` | `admin123` | MEDICO (Medicina General) |
| `dra.lopez` | `admin123` | MEDICO (Pediatría) |
| `paciente1` | `admin123` | PACIENTE |

---

## 🔄 Cambiar a Oracle Database 21c XE

Cuando tengas Oracle instalado, cambia el perfil activo:

### 1. Crear el usuario y schema en Oracle

Conéctate a Oracle como `SYS` o `SYSTEM` (en SQL Developer) y ejecuta:

```sql
@src/main/resources/db/oracle/00_create_user.sql
```

### 2. Crear las tablas y datos

Conéctate ahora como **`TRINIDAD_DB`** y ejecuta en orden:

```sql
@src/main/resources/db/oracle/01_schema.sql
@src/main/resources/db/oracle/02_seed.sql
```

### 3. Activar el perfil `oracle`

Edita `src/main/resources/application.properties` y cambia:

```properties
spring.profiles.active=oracle
```

Si tu password u host de Oracle es distinto, ajústalo en `src/main/resources/application-oracle.properties`.

### 4. Volver a ejecutar

```bash
mvn spring-boot:run
```

---

## 📁 Estructura del proyecto

```
trinidad-citas-medicas/
├── pom.xml                        # Dependencias Maven
├── src/main/
│   ├── java/com/trinidad/citas/
│   │   ├── TrinidadCitasApplication.java   # Main
│   │   ├── config/                # Spring Security + JWT + WebMvc
│   │   ├── model/                 # 17 entidades JPA
│   │   ├── repository/            # JpaRepository interfaces
│   │   ├── service/               # Lógica de negocio (@Service)
│   │   ├── controller/web/        # Controllers Thymeleaf (@Controller)
│   │   ├── controller/api/        # REST API (@RestController)
│   │   ├── dto/                   # DTOs
│   │   └── exception/             # Excepciones + GlobalExceptionHandler
│   └── resources/
│       ├── application.properties           # Config principal
│       ├── application-dev.properties       # Perfil dev (H2)
│       ├── application-oracle.properties    # Perfil oracle
│       ├── db/h2/                 # Scripts SQL para H2
│       ├── db/oracle/             # Scripts SQL para Oracle
│       ├── static/                # CSS, JS, imágenes
│       └── templates/             # Vistas Thymeleaf
└── src/test/                      # Tests
```

---

## 🌐 Endpoints disponibles

### Web (Thymeleaf)

| URL | Descripción | Acceso |
|---|---|---|
| `/` | Landing page | Público |
| `/login` | Inicio de sesión | Público |
| `/dashboard/kpis` | Dashboard con KPIs | Autenticado |
| `/pacientes` | Lista de pacientes | Autenticado |
| `/pacientes/nuevo` | Registrar paciente | Autenticado |
| `/medicos` | Lista de médicos | Autenticado |
| `/citas` | Calendario de citas | Autenticado |
| `/citas/agendar` | Agendar nueva cita | Autenticado |
| `/h2-console` | Consola H2 (perfil dev) | Público |

### REST API (JSON + JWT)

| Método | URL | Descripción |
|---|---|---|
| `POST` | `/api/auth/login` | Login y obtener JWT |
| `GET` | `/api/v1/pacientes` | Listar pacientes |
| `POST` | `/api/v1/pacientes` | Crear paciente |
| `GET/PUT/DELETE` | `/api/v1/pacientes/{id}` | Operaciones por id |
| `GET` | `/api/v1/citas` | Listar citas (param `?fecha=YYYY-MM-DD`) |
| `POST` | `/api/v1/citas` | Agendar cita |
| `POST` | `/api/v1/citas/{id}/cancelar` | Cancelar cita |
| `GET` | `/api/v1/especialidades` | Listar especialidades |
| `GET` | `/api/v1/reportes/kpis` | KPIs del dashboard |

**Ejemplo de login con curl:**
```bash
curl -X POST http://localhost:8081/trinidad/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Luego usa el `token` recibido como header en las llamadas:
```bash
curl http://localhost:8081/trinidad/api/v1/pacientes \
  -H "Authorization: Bearer eyJhbGc..."
```

---

## 🛠 Stack técnico

- **Java 17** + **Spring Boot 3.2.5**
- **Spring Web** + **Thymeleaf** (vistas server-side)
- **Spring Data JPA** + **Hibernate**
- **Spring Security** + **JWT (jjwt 0.12.5)**
- **Bootstrap 5.3** + **Bootstrap Icons** (vía WebJars)
- **Oracle 21c XE** (`ojdbc11`) en producción / **H2** en desarrollo
- **Lombok** para reducir boilerplate
- **Maven** para gestión de dependencias

---

## ❗ Solución de problemas comunes

**"No se descargan las dependencias en VS Code":**
Click derecho sobre `pom.xml` > `Reload Project` o ejecuta `mvn clean install` en terminal.

**"Port 8080 already in use":**
Cambia el puerto en `application.properties`: `server.port=8081`

**"Cannot connect to Oracle":**
Verifica que el listener esté arriba: `lsnrctl status`. El servicio debe ser `XE`.

**Lombok no funciona:**
Instala el plugin de Lombok en VS Code y habilita "Enable annotation processing" en preferencias de Java.

---

## 📚 Documentación adicional

Ver `docs/PROYECTO_TRINIDAD_COPILOT.md` para detalles del dominio, modelo de datos completo y roadmap por unidad del sílabo.
