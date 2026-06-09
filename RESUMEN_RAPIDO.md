# 📋 ANÁLISIS EJECUTIVO - TRINIDAD CITAS MÉDICAS

## 🎯 RESUMEN ULTRA-COMPACTO (1 página)

### ¿QUÉ ES?
Sistema web de gestión de citas médicas: agendar → consultar → recetar → pagar → reportar

### TECNOLOGÍA
- **Backend**: Spring Boot 3.2.5 (Java 17)
- **BD**: Oracle 21c XE (Prod) / H2 (Dev)
- **Auth**: JWT + Spring Security (BCrypt)
- **Frontend**: Thymeleaf + Bootstrap 5 + jQuery

### ENTIDADES (17)
```
Usuario + Rol ← → Paciente ← → HistoriaClinica
Medico + Especialidad + HorarioMedico
Cita → Atencion → Receta → DetalleReceta
            ↓ → OrdenExamen
            ↓ → Pago
AuditoriaLog (trazabilidad)
```

### FUNCIONALIDADES CORE
1. **Agendar Cita**: seleccionar especialidad→médico→fecha→hora
2. **Consulta Médica**: vital signs + anamnesis + diagnóstico CIE-10 + tratamiento
3. **Recetas**: medicamentos con dosis, frecuencia, duración
4. **Exámenes**: solicitar laboratorio/imaging
5. **Pagos**: EFECTIVO/TARJETA/YAPE/PLIN/TRANSFERENCIA
6. **Reportes**: pacientes, citas, ingresos, médicos, especialidades

### CAPAS
```
Thymeleaf/REST
     ↓
Controllers (13 web + 16 REST)
     ↓
Services (18)
     ↓
Repositories (15)
     ↓
Oracle 21c XE / H2
```

### SEGURIDAD
- Login: JWT (API) + Form-based (web)
- Password: BCrypt 10 rounds
- Rutas públicas: /, /login, /registro, /api/auth/**
- Rutas privadas: /dashboard/**, /api/v1/**
- Auditoría: tabla AUDITORIA_LOG (usuario, acción, IP, timestamp)

### ENDPOINTS PRINCIPALES
```
POST   /api/v1/auth/login
GET    /api/v1/pacientes, /api/v1/medicos, /api/v1/citas
POST   /api/v1/citas (agendar)
POST   /api/v1/atenciones (consulta)
POST   /api/v1/recetas, /api/v1/pagos
GET    /api/v1/reportes/*
```

### ESTADÍSTICAS
| Aspecto | Cantidad |
|---------|----------|
| Clases Java | ~100+ |
| Entidades | 17 |
| Tablas BD | 16 |
| Servicios | 18 |
| Controllers | 29 |
| Vistas HTML | 50+ |
| Endpoints REST | 200+ |

### FLUJO USUARIO TÍPICO
```
1. Registro/Login → JWT
2. Dashboard → KPIs (citas, ingresos, pacientes)
3. Agendar Cita → calendario + slots disponibles
4. [Médico] Registra Consulta → vital signs + diagnóstico
5. [Médico] Genera Receta → medicamentos
6. Pago → métodos múltiples
7. Ver Historial → expediente electrónico
8. Reportes → analytics
```

### ARCHIVOS CLAVE
```
pom.xml                           ← dependencias
src/main/resources/
├── application.properties        ← config prod (Oracle)
├── application-dev.properties    ← config dev (H2)
└── db/
    ├── oracle/01_schema.sql      ← DDL Oracle (16 tablas)
    └── h2/01_schema_h2.sql       ← DDL H2 (test)
src/main/java/com/trinidad/citas/
├── config/                       ← seguridad + JWT
├── model/                        ← 17 entidades JPA
├── service/                      ← 18 servicios negocio
├── controller/web/ + api/        ← 29 controllers
└── repository/                   ← 15 JpaRepository
src/main/resources/templates/     ← 50+ vistas Thymeleaf
```

### PATRÓN ARQUITECTÓNICO
✅ **Layered Architecture** - Separation of concerns
✅ **Repository Pattern** - Abstracción datos
✅ **DTO Pattern** - Decouple API contracts
✅ **Service Layer** - Lógica centralizada
✅ **Dependency Injection** - Spring beans
✅ **Global Exception Handler** - Error centralized

### VALIDACIONES
- Bean Validation: @NotBlank, @Size, @Email, @Pattern
- Custom: DNI (8 dígitos), CMP (formato médico)
- BD: CHECK constraints, UNIQUE keys, FK cascades

### OPERACIONES TÍPICAS

#### AGENDAR CITA
```
POST /api/v1/citas
{
  "pacienteId": 1,
  "medicoId": 5,
  "fecha": "2026-06-15",
  "horaInicio": "09:00",
  "motivo": "Revisión general"
}
↓
CitaService.verificarDisponibilidad()
CitaService.crearCita()
INSERT CITA (estado: PROGRAMADA)
EmailService.notificar()
AuditoriaLog.registrar()
```

#### REGISTRAR CONSULTA
```
POST /api/v1/atenciones
{
  "citaId": 123,
  "presionArterial": "120/80",
  "frecuenciaCardiaca": 72,
  "temperatura": 36.5,
  "pesoKg": 75.5,
  "tallaM": 1.75,
  "anamnesis": "Paciente refiere...",
  "examenFisico": "Se aprecia...",
  "diagnosticoCie10": "K21",
  "tratamiento": "Se prescribe..."
}
↓
AtencionService.registrar()
INSERT ATENCION
UPDATE CITA estado = ATENDIDA
[Optional] RecetaService.crear() + OrdenExamenService.crear()
```

#### PROCESAR PAGO
```
POST /api/v1/pagos
{
  "citaId": 123,
  "monto": 150.00,
  "metodoPago": "TARJETA",
  "nroComprobante": "F001-000001"
}
↓
PagoService.procesar()
INSERT PAGO (estado: PAGADO)
AuditoriaLog.registrar()
```

### ESTADOS CITA
```
PROGRAMADA       ← recién agendada
    ↓
CONFIRMADA       ← paciente confirma
    ↓
EN_ATENCION      ← en consultorio
    ↓
ATENDIDA         ← consulta completada
    ↓ (alternativas)
CANCELADA        ← cancelada por paciente/médico
NO_ASISTIO       ← paciente no vino
REPROGRAMADA     ← reagendada
```

### MÉTODOS PAGO
- EFECTIVO
- TARJETA
- YAPE
- PLIN
- TRANSFERENCIA

### ROLES
- ADMIN - acceso total, reportes, auditoría
- MEDICO - agendar, consultas, recetas, órdenes
- PACIENTE - agendar, ver citas, ver recetas
- RECEPCION - gestionar citas, pagos

### CONFIGURACIÓN IMPORTANTE
```
puerto:             8081
contexto:           /trinidad
jwt-expiration:     1 hora (3600s)
jwt-refresh:        24 horas (86400s)
password-encoder:   BCrypt(strength=10)
max-upload-size:    10MB
db-pool-size:       10 (prod)
```

---

## 🔗 REFERENCIAS RÁPIDAS

### Para Entender el Código
1. Lee `SecurityConfig.java` → cómo funciona autenticación
2. Lee `CitaService.java` → lógica de agendar citas
3. Lee `AtencionService.java` → registrar consultas
4. Estudia `templates/citas/agendar.html` → frontend
5. Revisa `db/oracle/01_schema.sql` → estructura BD

### Para Agregar Nueva Funcionalidad
1. Crear entity en `model/`
2. Crear repository en `repository/`
3. Crear service en `service/`
4. Crear controller en `controller/api/`
5. Crear DTO en `dto/`
6. Crear vista en `templates/`
7. Registrar en tabla AUDITORIA

### Para Debuggear
1. Enable SQL logging: `spring.jpa.show-sql=true`
2. Enable SQL formatting: `spring.jpa.properties.hibernate.format_sql=true`
3. Check logs en `logs/` folder
4. H2 console: `http://localhost:8081/trinidad/h2-console/` (dev)

---

## ⚡ TL;DR

**Sistema de citas médicas completo en Spring Boot con 17 entidades, 16 tablas, JWT auth, Thymeleaf UI, 200+ endpoints REST y auditoría total.**

