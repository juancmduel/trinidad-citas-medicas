# 🏥 ANÁLISIS COMPLETO - Trinidad Sistema de Citas Médicas
**Spring Boot 3.2.5 | Java 17 | Oracle 21c XE / H2 | JWT + MVC**

---

## 📊 VISTA GENERAL EJECUTIVA

### Objetivo
Sistema web completo de gestión de citas médicas para clínica "Trinidad y Especialidades Médicas S.A.C."
- Agendar citas médicas
- Registrar consultas clínicas
- Generar recetas y órdenes de examen
- Gestionar pagos
- Generar reportes

### Stack
```
BACKEND: Spring Boot 3.2.5 (Java 17)
├── Seguridad: Spring Security + JWT (0.12.5)
├── ORM: Spring Data JPA + Hibernate
├── Template: Thymeleaf
└── BD: Oracle 21c XE (prod) / H2 (dev)

FRONTEND: Bootstrap 5.3.3 + jQuery 3.7.1 + Thymeleaf
```

### Tamaño
- **17 Entidades JPA**
- **16 Tablas BD** (+ 2 intermedias)
- **18 Servicios**
- **29 Controllers** (13 web + 16 REST)
- **15 Repositorios**
- **50+ Vistas Thymeleaf**

---

## 🗄️ MODELO DE DATOS - 16 TABLAS

### Núcleo - Seguridad (3)
```
ROL ←─ USUARIO_ROL ─→ USUARIO (1:M)
       └─ password BCrypt, email único
       └─ intentos fallidos, bloqueado
```

### Gestión Médica (5)
```
ESPECIALIDAD (cardiología, pediatría, etc.)
   ↓
MEDICO (CMP único, FK→Especialidad)
   ↓
HORARIO_MEDICO (lunes-domingo, hora inicio/fin)

PACIENTE (DNI único, FK→Usuario)
   ↓
HISTORIA_CLINICA (1:1, expediente electrónico)
```

### Citas & Consultas (4)
```
CITA (paciente+medico+especialidad+fecha+hora)
├─ ESTADOS: PROGRAMADA → CONFIRMADA → EN_ATENCION → ATENDIDA / CANCELADA / NO_ASISTIO
├─ CANAL: WEB, TELEFONO, PRESENCIAL, APP
└─ FK→ PACIENTE, MEDICO, ESPECIALIDAD

ATENCION (realización de consulta, 1:1 con CITA)
├─ Vital signs: TA, FC, Temp, Peso, Talla, O2
├─ Anamnesis (CLOB)
├─ Examen físico (CLOB)
├─ Diagnóstico CIE-10 (FK)
└─ Tratamiento (CLOB)

DIAGNOSTICO_CIE10 (código PK, descripción)

RECETA (NRO_RECETA único, FK→ATENCION)
└─ DETALLE_RECETA (medicamento, dosis, frecuencia, duración, vía admin)
```

### Exámenes & Pagos (3)
```
ORDEN_EXAMEN (tipo, nombre, indicaciones, estado: SOLICITADO/RESULTANTE)
└─ FK→ ATENCION

PAGO (monto, método: EFECTIVO/TARJETA/YAPE/PLIN/TRANSFERENCIA)
├─ ESTADOS: PENDIENTE → PAGADO / ANULADO / REEMBOLSADO
├─ NRO_COMPROBANTE, TIPO_COMPROBANTE
└─ FK→ CITA

AUDITORIA_LOG (usuario, acción, entidad, detalles, IP, fecha_hora)
└─ Trazabilidad de cambios
```

### Diagram ER Simplificado
```
                          ┌─────────────┐
                          │   USUARIO   │
                          │             │
                     ┌────┤ ID_USUARIO  ├──────┐
                     │    │ USERNAME    │      │
                     │    │ PASSWORD    │      │
                     │    │ EMAIL       │      │
                     │    │ BLOQUEADO   │      │
                     │    │ INTENTOS    │      │
                     │    └─────────────┘      │
                     │                         │
                 ┌───▼────┐          ┌─────────▼───┐
                 │PACIENTE │          │    MEDICO   │
                 │         │          │             │
                 │DNI(uq) ─┼──────────┤CMP(uq)      │
                 │NOMBRES  │          │ESPECIALIDAD │
                 │HISTORIA ─┼──────┐   │HORARIOS    │
                 └────┬────┘       │   └─────┬──────┘
                      │           │         │
                      │     ┌─────▼─────┐   │
                      │     │HISTORIA   │   │
                      └─────┤CLINICA    │   │
                            │ID_HISTORIA───┐
                            └──────────┘   │
                                           │
                      ┌────────────────────▼──────────────┐
                      │           CITA                    │
                      │                                   │
                      │ ID_CITA, FECHA, HORA_INICIO/FIN  │
                      │ ESTADO, MOTIVO, QR, TURNO        │
                      │ FK→PACIENTE, MEDICO, ESPEC       │
                      │ FECHA_REGISTRO, FECHA_CHECKIN    │
                      └────────────────────┬──────────────┘
                                           │
                      ┌────────────────────▼──────────────┐
                      │       ATENCION (1:1 CITA)        │
                      │                                   │
                      │ VITAL_SIGNS: TA, FC, T°, O2      │
                      │ ANAMNESIS (CLOB)                 │
                      │ EXAMEN_FISICO (CLOB)             │
                      │ DIAGNOSTICO_CIE10                │
                      │ TRATAMIENTO (CLOB)               │
                      │ FIRMADO_POR, FECHA_FIRMA         │
                      └────────┬────────────┬─────────────┘
                               │            │
                    ┌──────────▼──┐    ┌────▼────────────┐
                    │   RECETA    │    │ ORDEN_EXAMEN    │
                    │             │    │                 │
                    │ NRO_RECETA ─┼─┐  │ TIPO_EXAMEN     │
                    │ FECHA_EMIS. │ │  │ ESTADO          │
                    │ OBS.        │ │  │ FECHA_RESULTADO │
                    └──────┬──────┘ │  └─────────────────┘
                           │        │
                    ┌──────▼──────┐ │
                    │DETALLE_REC. │ │
                    │             │◄┘
                    │ MEDICAMENTO │
                    │ DOSIS       │
                    │ FRECUENCIA  │
                    │ DURACION_D. │
                    │ VIA_ADMIN   │
                    │ INDICACION  │
                    └─────────────┘

              ┌──────────────────────────────────┐
              │          PAGO (FK→CITA)          │
              │                                  │
              │ MONTO, METODO_PAGO, ESTADO      │
              │ NRO_COMPROBANTE                 │
              │ FECHA_PAGO, FECHA_REGISTRO      │
              └──────────────────────────────────┘

              ┌──────────────────────────────────┐
              │       AUDITORIA_LOG              │
              │                                  │
              │ ID_USUARIO, ACCION              │
              │ ENTIDAD, ID_ENTIDAD             │
              │ DETALLE, IP_ORIGEN              │
              │ FECHA_HORA                      │
              └──────────────────────────────────┘
```

---

## 🎯 ESTRUCTURA JAVA

### Paquetes Principales
```
com.trinidad.citas/
├── config/               (5 clases)
│   ├── SecurityConfig          ← Spring Security + JWT
│   ├── JwtService              ← Token generation/validation
│   ├── JwtAuthFilter           ← JWT filter chain
│   ├── CustomUserDetailsService
│   └── WebMvcConfig
│
├── model/               (17 entidades JPA)
│   └── @Entity: Usuario, Paciente, Medico, Cita, Atencion, Receta, etc.
│
├── repository/          (15 interfaces)
│   └── @Repository extends JpaRepository<T, ID>
│
├── service/            (18 servicios)
│   ├── AuthService
│   ├── UsuarioService
│   ├── PacienteService
│   ├── MedicoService
│   ├── CitaService ← Lógica de agendar/cancelar/cambiar estado
│   ├── AtencionService ← Registrar consulta
│   ├── RecetaService
│   ├── PagoService
│   ├── ReporteService
│   └── ...
│
├── controller/
│   ├── web/            (13 web controllers)
│   │   └── @Controller + Thymeleaf (vistas HTML)
│   │
│   └── api/            (16 REST controllers)
│       └── @RestController (JSON endpoints)
│
├── dto/                (15+ DTOs)
│   └── @Data: PacienteDTO, MedicoDTO, CitaDTO, etc.
│
├── exception/
│   └── GlobalExceptionHandler (REST API error handling)
│
├── util/ & validation/
│   └── Custom validators, utilities
│
└── TrinidadCitasApplication
    └── @SpringBootApplication public static void main()
```

### Decoradores Spring Usados
```java
@Entity                    // JPA - mapeo a tabla
@Table(name = "xxx")      // nombre de tabla
@Repository               // DAO layer
@Service                  // Business logic
@Controller               // Web controller (Thymeleaf)
@RestController           // REST API
@Configuration            // Spring beans
@Bean                     // Bean definition
@Autowired                // Dependency injection
@PreAuthorize             // Method-level security
@PostMapping, @GetMapping // REST endpoints
@Valid                    // Bean validation
@NotBlank, @Size, etc.   // Validation constraints
```

---

## 🔐 SEGURIDAD - JWT + Form-Based Auth

### Autenticación
```
1. LOGIN
   POST /login (form)  o  POST /api/v1/auth/login (JSON)
   ↓
   AuthService.login(username, password)
   ↓
   UsuarioRepository.findByUsername()
   ↓
   BCryptPasswordEncoder.matches(password, stored_hash)
   ├─ ✅ VALID   → JwtService.generateToken()  → JWT cookie
   └─ ❌ INVALID → intentosFallidos++, posible bloqueo

2. REQUESTS POSTERIORES
   Request header: Authorization: Bearer <JWT_TOKEN>
   ↓
   JwtAuthFilter intercepta
   ↓
   JwtService.validateToken(token)
   ├─ ✅ VALID   → SecurityContext = authenticated user
   └─ ❌ INVALID → 401 Unauthorized

3. LOGOUT
   GET /logout
   ↓
   Invalida sesión
   ↓
   Elimina JSESSIONID cookie
```

### Rutas Públicas (sin login)
```
/                    landing page
/login               login page
/registro            new account
/css/**, /js/**, /img/**   assets
/webjars/**          Bootstrap, jQuery
/h2-console/**       H2 DB admin (dev)
/api/auth/**         auth endpoints
/error               error pages
```

### Rutas Autenticadas (requieren login + rol)
```
/dashboard/**        dashboard
/pacientes/**        manage patients
/medicos/**          manage doctors
/especialidades/**   manage specialties
/citas/**            book/manage appointments
/atenciones/**       medical consultations
/recetas/**          prescriptions
/pagos/**            payments
/reportes/**         reports
/auditoria/**        audit logs
/api/v1/**           all REST endpoints
```

### Encryption & Security
```
Password:        BCrypt(strength=10)
JWT Algorithm:   HS256 (JJWT)
Expiration:      1 hora (3600s)
Refresh:         24 horas (86400s)
CSRF:            Enabled (web routes), Disabled (API)
Frame Options:   SameOrigin (H2 console)
```

---

## 📝 CONFIGURACIÓN

### Environment Variables / Profiles

**Desarrollo (application-dev.properties)**
```properties
spring.datasource.url=jdbc:h2:mem:trinidaddb;MODE=Oracle;...
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
```

**Producción (application-oracle.properties)**
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=TRINIDAD_DB
spring.datasource.password=Trinidad2026
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=none  # IMPORTANTE: manual migrations!
```

**application.properties (aplicable a ambos)**
```properties
spring.application.name=trinidad-citas-medicas
spring.profiles.active=oracle,api,web
server.port=8081
server.servlet.context-path=/trinidad

trinidad.jwt.secret=CAMBIAR_ESTA_CLAVE_MUY_LARGA_Y_SEGURA_EN_PROD_2026...
trinidad.jwt.expiration-ms=3600000
trinidad.jwt.refresh-ms=86400000

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

---

## 📊 VISTAS THYMELEAF (50+ archivos)

### Organización
```
templates/
├── layout.html                    ← Base template (header, nav, footer)
├── index.html                     ← Landing
├── auth/login.html                ← Login form
├── dashboard/
│   └── index.html                 ← KPIs, gráficos, resumen
├── pacientes/                     ← CRUD pacientes
│   ├── lista.html                 tabla, filtros
│   ├── form.html                  crear/editar
│   └── detalle.html               ver detalles
├── medicos/                       ← CRUD médicos
├── especialidades/                ← CRUD especialidades
├── citas/                         ← agendar, listar, calendario
│   ├── agendar.html               formulario + calendario
│   ├── calendario.html            vista calendario interactiva
│   ├── lista.html
│   └── detalle.html
├── atenciones/                    ← registrar consultas
│   ├── lista.html
│   ├── form.html                  formulario consulta (vital signs, anamnesis, etc.)
│   └── detalle.html
├── recetas/                       ← crear recetas con medicamentos
├── detalle-recetas/               ← línea items medicamentos
├── historia-clinica/              ← expediente electrónico
├── ordenes-examen/                ← solicitud exámenes
├── pagos/                         ← registro pagos
├── horarios/                      ← disponibilidad médicos
├── diagnosticos/                  ← búsqueda CIE-10
├── roles/                         ← gestión roles
├── usuarios/                      ← CRUD usuarios
├── auditoria/                     ← logs de cambios
├── reportes/                      ← reportes (pacientes, médicos, citas, pagos)
├── configuracion/                 ← parámetros sistema
├── error/
│   ├── 403.html                   acceso denegado
│   └── 404.html                   no encontrado
└── fragments/
    └── header.html                ← fragmento reutilizable
```

---

## 🌐 API REST ENDPOINTS

### Autenticación
```
POST   /api/v1/auth/login          {username, password} → JWT
POST   /api/v1/auth/logout         → logout
POST   /api/v1/auth/register       {username, email, password} → create account
GET    /api/v1/auth/me             → current user info
```

### Pacientes
```
GET    /api/v1/pacientes           → listado (paginado)
GET    /api/v1/pacientes/{id}      → detalles
POST   /api/v1/pacientes           {datos} → crear
PUT    /api/v1/pacientes/{id}      {datos} → actualizar
DELETE /api/v1/pacientes/{id}      → eliminar
GET    /api/v1/pacientes/dni/{dni} → buscar por DNI
```

### Médicos
```
GET    /api/v1/medicos             → listado
GET    /api/v1/medicos/{id}        → detalles
POST   /api/v1/medicos             → crear
PUT    /api/v1/medicos/{id}        → actualizar
DELETE /api/v1/medicos/{id}        → eliminar
GET    /api/v1/medicos/especialidad/{espId}  → médicos por especialidad
```

### Citas
```
GET    /api/v1/citas               → listado (filtrar por estado, fecha, etc.)
GET    /api/v1/citas/{id}          → detalles
POST   /api/v1/citas               {pacienteId, medicoId, fecha, hora} → agendar
PUT    /api/v1/citas/{id}          {estado} → cambiar estado
DELETE /api/v1/citas/{id}          → cancelar
GET    /api/v1/citas/disponibilidad/{medicoId}/{fecha}  → slots disponibles
POST   /api/v1/citas/{id}/confirmar → confirmar cita
POST   /api/v1/citas/{id}/cancelar → cancelar con motivo
```

### Consultas Médicas (Atenciones)
```
GET    /api/v1/atenciones          → listado
GET    /api/v1/atenciones/{id}     → detalles
POST   /api/v1/atenciones          {citaId, vital_signs, anamnesis, etc.}  → registrar
PUT    /api/v1/atenciones/{id}     → actualizar
POST   /api/v1/atenciones/{id}/firmar → médico firma consulta
```

### Recetas
```
GET    /api/v1/recetas             → listado
GET    /api/v1/recetas/{id}        → detalles completos
POST   /api/v1/recetas             {atencionId, medicamentos[]} → crear
DELETE /api/v1/recetas/{id}        → eliminar

GET    /api/v1/detalle-recetas/{recetaId}  → medicamentos en receta
POST   /api/v1/detalle-recetas              → agregar medicamento
PUT    /api/v1/detalle-recetas/{id}        → actualizar medicamento
DELETE /api/v1/detalle-recetas/{id}        → eliminar medicamento
```

### Órdenes de Examen
```
GET    /api/v1/ordenes-examen           → listado
POST   /api/v1/ordenes-examen           → solicitar examen
PUT    /api/v1/ordenes-examen/{id}      → cambiar estado (SOLICITADO→RESULTANTE)
```

### Pagos
```
GET    /api/v1/pagos                    → listado pagos
POST   /api/v1/pagos                    {citaId, monto, metodo} → registrar pago
GET    /api/v1/pagos/cita/{citaId}     → pagos de una cita
PUT    /api/v1/pagos/{id}               → actualizar estado
```

### Usuarios & Roles
```
GET    /api/v1/usuarios                 → listado
POST   /api/v1/usuarios                 → crear usuario
PUT    /api/v1/usuarios/{id}            → actualizar
POST   /api/v1/usuarios/{id}/roles      → asignar rol

GET    /api/v1/roles                    → listado roles
POST   /api/v1/roles                    → crear rol
```

### Reportes
```
GET    /api/v1/reportes/pacientes              → resumen pacientes
GET    /api/v1/reportes/citas                  → estadísticas citas
GET    /api/v1/reportes/ingresos               → ingresos por mes/médico/especialidad
GET    /api/v1/reportes/citas-por-especialidad → distribución
GET    /api/v1/reportes/medicos-top            → médicos más activos
GET    /api/v1/reportes/horas-pico             → franjas horarias concurridas
```

### Dashboard
```
GET    /api/v1/dashboard/kpis           → {total_citas, citas_completadas, ingresos, pacientes}
GET    /api/v1/dashboard/proximas-citas → citas hoy, mañana
```

### Auditoría
```
GET    /api/v1/auditoria                → logs de cambios
GET    /api/v1/auditoria/{id}           → detalles log
GET    /api/v1/auditoria/usuario/{userId}  → logs por usuario
```

---

## 🏛️ PATRÓN DE DISEÑO

### Capas
```
HTTP Request
    ↓
┌─ CONTROLLER ─────────────────────────────────────┐
│ @RestController / @Controller                     │
│ @PostMapping("/api/v1/...")                      │
│ public ResponseEntity<T> method(@RequestBody ...) │
│                  ↓                                 │
│           Valida @Valid                          │
│                  ↓                                 │
└─────────────────────────────────────────────────┘
         ↓
┌─ SERVICE ─────────────────────────────────────┐
│ @Service                                       │
│ public T doBusinessLogic() {                  │
│   // Lógica de negocio                       │
│   // Validaciones                            │
│   // Transacciones                           │
└─────────────────────────────────────────────┘
         ↓
┌─ REPOSITORY ──────────────────────────────┐
│ @Repository extends JpaRepository<T, ID>   │
│ public T save(T entity) {                  │
│   // Delegado a Hibernate                 │
│ }                                          │
└─────────────────────────────────────────┘
         ↓
┌─ DATABASE ───────────────────────────────┐
│ Oracle 21c XE (prod) / H2 (dev)           │
│ SQL: INSERT, SELECT, UPDATE, DELETE      │
└─────────────────────────────────────────┘

HTTP Response (JSON / HTML)
```

### Transacciones (Spring Data)
```java
@Service
public class CitaService {
    @Transactional  // Rollback automático en excepción
    public void crearCita(CitaDTO dto) {
        // 1. Validar disponibilidad
        if (!medicoDisponible(dto.getMedicoId(), dto.getFecha())) {
            throw new BusinessException("Médico no disponible");
        }
        
        // 2. Crear cita
        Cita cita = new Cita();
        cita.setPaciente(pacienteRepository.findById(dto.getPacienteId()).orElseThrow());
        cita.setMedico(medicoRepository.findById(dto.getMedicoId()).orElseThrow());
        cita.setEstado("PROGRAMADA");
        
        // 3. Guardar (INSERT)
        citaRepository.save(cita);
        
        // 4. Notificar
        emailService.notificarCitaAgendada(cita);
        
        // 5. Log auditoria
        auditoriaService.registrar("CREAR_CITA", "CITA", cita.getId(), "...detalles...");
    }
}
```

---

## 🚀 FLUJOS DE NEGOCIOS PRINCIPALES

### 1. AGENDAR CITA
```
Paciente → Ingresa a /citas/agendar
         → Ve calendario con disponibilidad
         → Selecciona: Especialidad → Médico → Fecha → Hora
         → CitaService.verificarDisponibilidad()
              ├─ ✅ Disponible
              │   └─ crearCita()
              │       ├─ INSERT CITA (PROGRAMADA)
              │       ├─ emailService.notificar()
              │       └─ auditoriaService.log()
              └─ ❌ No disponible → error message
```

### 2. REGISTRAR CONSULTA MÉDICA
```
Médico → /atenciones/form (en tiempo de cita)
      → Completa:
         - Vital signs (TA, FC, Temp, Peso, Talla, O2)
         - Anamnesis (CLOB)
         - Examen físico (CLOB)
         - Diagnóstico (CIE-10 lookup)
         - Tratamiento (CLOB)
      → AtencionService.registrarAtencion()
         ├─ INSERT ATENCION
         ├─ UPDATE CITA estado = "ATENDIDA"
         ├─ [OPTIONAL] RecetaService.crearReceta()
         ├─ [OPTIONAL] OrdenExamenService.crearOrden()
         ├─ Médico firma digitalmente
         └─ auditoriaService.log()
```

### 3. PROCESAR PAGO
```
Paciente/Recepción → /pagos
                   → Selecciona cita
                   → Ingresa monto y método
                   → PagoService.procesarPago()
                      ├─ INSERT PAGO (PAGADO)
                      ├─ UPDATE CITA (si aplica)
                      ├─ Generar comprobante
                      └─ auditoriaService.log()
```

### 4. GENERAR REPORTE
```
Admin → /reportes
     → Selecciona tipo (pacientes, citas, ingresos, etc.)
     → Selecciona rango fechas
     → ReporteService.generarReporte()
        ├─ Query BD (SQL nativo o JPQL)
        ├─ Procesar datos
        ├─ Generar PDF/Excel (si aplica)
        └─ auditoriaService.log()
```

---

## 📈 PATRONES OBSERVADOS

### Architectural
1. **Layered** - Controller → Service → Repository → DB
2. **Repository Pattern** - JpaRepository abstracts persistence
3. **DTO Pattern** - DTOs decouple API contracts from entities
4. **Dependency Injection** - Constructor-based autowiring

### Security
1. **JWT for API** - Stateless authentication
2. **Form-based for Web** - Sessions + cookies
3. **BCrypt for passwords** - 10-round hashing
4. **Method-level security** - @PreAuthorize decorators

### Database
1. **Sequences** - Auto-generated IDs (SEQ_XXXXX)
2. **Triggers** - Oracle triggers on INSERT
3. **Constraints** - CHECK, UNIQUE, FK with CASCADE
4. **Temporal** - TIMESTAMP for audit trail

### Validation
1. **Bean Validation** - @NotBlank, @Size, @Email, @Pattern
2. **Custom Validators** - DNI format, CMP format
3. **Global Exception Handler** - Centralizes REST error responses

---

## 💡 PUNTOS CLAVE DE ARQUITECTURA

### 1. Escalabilidad
```
✅ Stateless API (JWT) → easy horizontal scaling
✅ Connection pooling (HikariCP) → efficient DB usage
✅ Lazy loading (FETCH.LAZY) → reduce N+1 queries
✅ Batch operations (hibernate.jdbc.batch_size=20)
```

### 2. Seguridad
```
✅ BCrypt password hashing
✅ JWT + Spring Security
✅ CSRF protection (web routes)
✅ Audit trail (AUDITORIA_LOG table)
✅ Method-level authorization
```

### 3. Mantenibilidad
```
✅ Clear layered structure
✅ Consistent naming conventions
✅ Central error handling
✅ Separation of concerns
```

### 4. Testing Ready
```
✅ Dependency injection (easy mocking)
✅ Repository abstraction (mockable)
✅ Service layer isolation
✅ DTOs for test fixtures
```

---

## 🔍 ESTADÍSTICAS FINALES

| Métrica | Valor |
|---------|-------|
| Entidades JPA | 17 |
| Tablas Base de Datos | 16 |
| Servicios | 18 |
| Web Controllers | 13 |
| REST Controllers | 16 |
| Repositorios | 15 |
| DTOs | 15+ |
| Vistas Thymeleaf | 50+ |
| REST Endpoints | 200+ |
| Líneas de Código Estimado | 30,000+ |
| Test Coverage Potencial | Alta (Spring Boot starter-test) |

---

## 🎓 TECNOLOGÍAS Y VERSIONES

```
Java                    17 (LTS)
Spring Boot             3.2.5
Spring Security         6.x (incluido)
Spring Data JPA         3.2.5
Hibernate               6.x (via Spring Data)
Thymeleaf               3.1.x
Bootstrap               5.3.3
jQuery                  3.7.1
JJWT                    0.12.5
Lombok                  1.18.38
Oracle JDBC             23.3.0.23.09
H2 Database             2.x
Maven                   3.x+
```

---

## 📝 DOCUMENTACIÓN POR ASPECTO

### BD
- Archivo: `src/main/resources/db/oracle/01_schema.sql`
- Contiene: CREATE TABLE, SEQUENCE, TRIGGER statements
- DDL completo para reproducir BD

### Configuración
- `src/main/resources/application.properties` (prod)
- `src/main/resources/application-dev.properties` (dev)
- `src/main/resources/application-oracle.properties` (Oracle-specific)

### Código Java
- Main: `TrinidadCitasApplication.java`
- Config: `config/SecurityConfig.java`, `JwtService.java`
- Entities: `model/` (17 clases)
- Services: `service/` (18 clases)
- Controllers: `controller/web/` + `controller/api/`

---

## ✅ CHECKLIST PARA DESARROLLADORES

- [ ] Entender flujo: Agendar Cita → Registrar Consulta → Receta → Pago
- [ ] Revisar `SecurityConfig.java` para entender JWT + rutas públicas/privadas
- [ ] Explorar servicios principales: CitaService, AtencionService, PagoService
- [ ] Familiarizarse con DTOs para API contracts
- [ ] Revisar Thymeleaf templates (especialmente agendar.html + form.html)
- [ ] Entender relaciones BD (FK, cascades)
- [ ] Estudiar patrones de validación (Bean Validation + custom)
- [ ] Configurar IDE para reconocer Lombok (@Data, @Service)
- [ ] Probar con H2 (dev profile) antes de conectar Oracle

