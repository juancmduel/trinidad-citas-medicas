# 🎨 DIAGRAMA Y ESTRUCTURA DETALLADA

## DIAGRAMA COMPLETO: FLUJO DE USUARIO

```
┌────────────────────────────────────────────────────────────────────┐
│                     TRINIDAD CITAS MÉDICAS                         │
│                  Sistema de Gestión Médica v1.0.0                 │
└────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│        VISITANTE (NO AUTENTICADO)       │
└─────────────────────────────────────────┘
         ↓
         ├─→ /                  (landing)
         ├─→ /login             (login form)
         ├─→ /registro          (signup form)
         └─→ /api/v1/auth/**    (JWT endpoints)
                    │
                    ├─→ POST /login        (credentials)
                    │    ↓
                    │    JwtService.generateToken()
                    │    ↓
                    │    ✅ JWT returned (1 hour expiration)
                    │
                    └─→ POST /register     (create account)
                         ↓
                         UsuarioService.registrar()
                         ├─ Hash password (BCrypt)
                         ├─ Assign role (PACIENTE/MEDICO)
                         ├─ INSERT USUARIO
                         └─ AuditoriaLog
                              
         Authorization: Bearer <JWT_TOKEN>
                    ↓
                    ✅ AUTENTICADO
                    
┌─────────────────────────────────────────────────────────┐
│         PACIENTE AUTENTICADO                             │
└─────────────────────────────────────────────────────────┘
    ├─→ /dashboard              (KPIs: próximas citas, historial)
    │   
    ├─→ /citas/agendar          (NEW FLOW: AGENDAR CITA)
    │    ├─ Selecciona especialidad
    │    ├─ Selecciona médico (según especialidad)
    │    ├─ Calendario: ve horarios disponibles
    │    │  └─ HorarioMedicoService.obtenerDisponibles()
    │    ├─ Selecciona fecha + hora
    │    ├─ Confirma
    │    └─ POST /api/v1/citas (agendar)
    │         ↓
    │         CitaService.crearCita()
    │         ├─ Validar paciente existe
    │         ├─ Validar médico existe
    │         ├─ Validar disponibilidad (NO overlap)
    │         ├─ INSERT CITA (estado: PROGRAMADA)
    │         ├─ EmailService.notificarAgendamiento()
    │         ├─ EmailService.notificarMedico()
    │         └─ AuditoriaLog.registrar("CREAR_CITA", "CITA", id, "...")
    │              ✅ Cita agendada
    │    
    ├─→ /citas/lista             (VER CITAS)
    │    └─ Filtros: por estado, fecha, médico
    │       └─ GET /api/v1/citas?estado=PROGRAMADA&fecha=2026-06-15
    │    
    ├─→ /citas/{id}/detalle      (VER DETALLE CITA)
    │    └─ Ver: fecha, hora, médico, especialidad, estado, motivo, QR
    │    
    ├─→ /historia-clinica        (VER EXPEDIENTE)
    │    └─ GET /api/v1/historia-clinica/{pacienteId}
    │         └─ Ver: citas pasadas, consultas, recetas, órdenes
    │    
    ├─→ /recetas/{id}            (VER RECETA MÉDICA)
    │    └─ Medicamentos con dosis, frecuencia, indicaciones
    │    
    ├─→ /pagos                   (PAGAR CITA)
    │    └─ POST /api/v1/pagos
    │         ├─ Selecciona cita
    │         ├─ Ingresa monto
    │         ├─ Elige método (EFECTIVO, TARJETA, YAPE, PLIN, TRANSFER)
    │         └─ PagoService.procesar()
    │              ├─ Validar monto
    │              ├─ INSERT PAGO (estado: PAGADO)
    │              ├─ Generar comprobante
    │              └─ AuditoriaLog
    │                   ✅ Pago registrado
    │    
    └─→ /logout                  (SALIR)
         └─ Invalida sesión, elimina JWT


┌─────────────────────────────────────────────────────────┐
│         MÉDICO AUTENTICADO                               │
└─────────────────────────────────────────────────────────┘
    ├─→ /dashboard              (KPIs: citas hoy, pacientes, ingresos)
    │   
    ├─→ /citas/lista             (VER CITAS DEL DÍA)
    │    └─ Filtrar por: estado, especialidad, hora
    │    
    ├─→ /horarios                (GESTIONAR DISPONIBILIDAD)
    │    ├─ VER horarios (lunes-domingo, hora inicio/fin)
    │    └─ CREAR/EDITAR horarios
    │         └─ POST /api/v1/horarios-medico
    │              ├─ medicoId, diaSemana, horaInicio, horaFin
    │              └─ HorarioMedicoService.guardar()
    │    
    ├─→ /atenciones/form         (REGISTRAR CONSULTA) ⭐ IMPORTANTE
    │    └─ POST /api/v1/atenciones
    │         ├─ citaId (de cita previamente agendada)
    │         ├─ Vital signs:
    │         │  ├─ presionArterial (TA): "120/80"
    │         │  ├─ frecuenciaCardiaca: 72
    │         │  ├─ temperatura: 36.5
    │         │  ├─ pesoKg: 75.5
    │         │  ├─ tallaM: 1.75
    │         │  └─ saturacionO2: 98
    │         │
    │         ├─ Campos CLOB (large text):
    │         │  ├─ anamnesis (historia del paciente)
    │         │  ├─ examenFisico (hallazgos)
    │         │  └─ tratamiento (plan)
    │         │
    │         ├─ diagnosticoCie10 (lookup table)
    │         ├─ diagnosticoDesc (descripción libre)
    │         │
    │         └─ AtencionService.registrar()
    │              ├─ INSERT ATENCION
    │              ├─ UPDATE CITA estado = "ATENDIDA"
    │              ├─ IF receta needed:
    │              │  └─ RecetaService.crear()
    │              │     └─ POST /api/v1/recetas
    │              │        ├─ nroReceta (unique)
    │              │        └─ DetalleReceta[] (medicamentos)
    │              │
    │              ├─ IF examen needed:
    │              │  └─ OrdenExamenService.crear()
    │              │     └─ POST /api/v1/ordenes-examen
    │              │        ├─ tipoExamen
    │              │        ├─ nombreExamen
    │              │        └─ indicaciones
    │              │
    │              ├─ Médico firma:
    │              │  ├─ PUT /api/v1/atenciones/{id}/firmar
    │              │  └─ UPDATE ATENCION firmadoPor = userId, fechaFirma = now
    │              │
    │              └─ AuditoriaLog.registrar()
    │    
    ├─→ /recetas/lista           (VER RECETAS CREADAS)
    │    └─ Filtrar: paciente, fecha, estado
    │    
    ├─→ /recetas/{id}/editar     (EDITAR RECETA)
    │    ├─ DetalleRecetaService.agregar()
    │    └─ DetalleRecetaService.eliminar()
    │    
    ├─→ /ordenes-examen/lista    (VER ÓRDENES SOLICITADAS)
    │    └─ Filtrar: estado (SOLICITADO, RESULTANTE), fecha
    │    
    ├─→ /pacientes/lista         (VER LISTA PACIENTES)
    │    └─ GET /api/v1/pacientes
    │    
    ├─→ /pacientes/{id}/detalle  (VER PACIENTE)
    │    ├─ Datos demográficos: DNI, nombres, apellidos, etc.
    │    └─ Historia clínica: citas, consultas, diagnósticos
    │    
    └─→ /logout                  (SALIR)


┌─────────────────────────────────────────────────────────┐
│         ADMIN AUTENTICADO                                │
└─────────────────────────────────────────────────────────┘
    ├─→ /dashboard              (KPIs globales)
    │   
    ├─→ /pacientes              (CRUD PACIENTES)
    │    ├─ GET /api/v1/pacientes
    │    ├─ POST /api/v1/pacientes (crear)
    │    ├─ PUT /api/v1/pacientes/{id} (editar)
    │    └─ DELETE /api/v1/pacientes/{id} (eliminar)
    │    
    ├─→ /medicos                (CRUD MÉDICOS)
    │    ├─ POST /api/v1/medicos
    │    │  ├─ nombres, apellidos, DNI (único), CMP (único)
    │    │  ├─ especialidad, email, teléfono
    │    │  └─ MedicoService.guardar()
    │    │
    │    ├─ PUT /api/v1/medicos/{id}
    │    └─ DELETE /api/v1/medicos/{id}
    │    
    ├─→ /especialidades         (CRUD ESPECIALIDADES)
    │    ├─ POST /api/v1/especialidades
    │    │  ├─ nombre (único), descripción
    │    │  ├─ precioConsulta, duracionMinutos (default 20)
    │    │  └─ EspecialidadService.guardar()
    │    │
    │    ├─ PUT /api/v1/especialidades/{id}
    │    └─ DELETE /api/v1/especialidades/{id}
    │    
    ├─→ /usuarios               (CRUD USUARIOS)
    │    ├─ POST /api/v1/usuarios
    │    │  ├─ username (único), email (único)
    │    │  ├─ password (auto-generated o ingresado)
    │    │  ├─ UsuarioService.guardar()
    │    │  └─ EmailService.enviar (credenciales iniciales)
    │    │
    │    ├─ PUT /api/v1/usuarios/{id}
    │    ├─ DELETE /api/v1/usuarios/{id}
    │    │
    │    └─ POST /api/v1/usuarios/{id}/roles
    │         ├─ Asignar rol (ADMIN, MEDICO, PACIENTE, RECEPCION)
    │         └─ INSERT USUARIO_ROL
    │    
    ├─→ /roles                  (CRUD ROLES)
    │    ├─ POST /api/v1/roles
    │    ├─ PUT /api/v1/roles/{id}
    │    └─ DELETE /api/v1/roles/{id}
    │    
    ├─→ /reportes               (ANALYTICS) ⭐ IMPORTANTE
    │    ├─ GET /api/v1/reportes/pacientes
    │    │  └─ Total por mes, especialidad, activos, nuevos
    │    │
    │    ├─ GET /api/v1/reportes/citas
    │    │  └─ Total, completadas, canceladas, no-asistencia
    │    │
    │    ├─ GET /api/v1/reportes/ingresos
    │    │  └─ Por mes, médico, especialidad, método pago
    │    │
    │    ├─ GET /api/v1/reportes/medicos-top
    │    │  └─ Top médicos por citas, ingresos, satisfacción
    │    │
    │    ├─ GET /api/v1/reportes/citas-por-especialidad
    │    │  └─ Distribución
    │    │
    │    └─ GET /api/v1/reportes/horas-pico
    │         └─ Franjas horarias más concurridas
    │    
    ├─→ /auditoria               (AUDIT TRAIL) 🔍
    │    ├─ GET /api/v1/auditoria
    │    │  └─ Listar todos los cambios (paginado)
    │    │     ├─ usuario, acción, entidad, id_entidad
    │    │     ├─ detalles, IP, fecha_hora
    │    │     └─ Filtrar: rango fechas, usuario, acción
    │    │
    │    ├─ GET /api/v1/auditoria/{id}
    │    │  └─ Ver detalles de un cambio
    │    │
    │    └─ GET /api/v1/auditoria/usuario/{userId}
    │         └─ Todos los cambios de un usuario
    │    
    ├─→ /configuracion          (SYSTEM CONFIG)
    │    ├─ Parámetros sistema:
    │    │  ├─ JWT secret (cambiar en prod!)
    │    │  ├─ Hora max upload
    │    │  ├─ Timeout sesión
    │    │  ├─ Email settings (SMTP)
    │    │  └─ Backup schedule
    │    │
    │    └─ POST /api/v1/configuracion/cambiar-settings
    │         └─ UPDATE application settings
    │    
    └─→ /logout                 (SALIR)
```

---

## 🗂️ ESTRUCTURA DE ARCHIVOS JAVA

```
src/main/java/com/trinidad/citas/
│
├── TrinidadCitasApplication.java
│   └── @SpringBootApplication
│       public static void main(String[] args)
│
├── config/ (5 clases)
│   ├── SecurityConfig.java
│   │   └── @Configuration
│   │       - PasswordEncoder(BCrypt)
│   │       - DaoAuthenticationProvider
│   │       - SecurityFilterChain (JWT + form)
│   │       - Rutas públicas/privadas
│   │
│   ├── JwtService.java
│   │   └── @Service
│   │       - generateToken(usuario) → JWT
│   │       - validateToken(token) → boolean
│   │       - extractUsername(token) → username
│   │       - extractExpiration(token) → Date
│   │
│   ├── JwtAuthFilter.java
│   │   └── extends OncePerRequestFilter
│   │       - doFilterInternal() → intercepta todas requests
│   │       - Extrae JWT del header Authorization
│   │       - Valida y seteea SecurityContext
│   │
│   ├── CustomUserDetailsService.java
│   │   └── @Service implements UserDetailsService
│   │       - loadUserByUsername(username) → UserDetails
│   │       - Busca en DB y retorna roles
│   │
│   └── WebMvcConfig.java
│       └── @Configuration
│           - CORS config
│           - Static resources mapping
│
├── model/ (17 entidades)
│   ├── Usuario.java
│   │   └── @Entity @Table(name="USUARIO")
│   │       - id, username, passwordHash, email, bloqueado, intentosFallidos
│   │       - @OneToMany USUARIO_ROL
│   │
│   ├── Rol.java
│   │   └── @Entity
│   │       - id, nombre (unique), descripcion, activo
│   │
│   ├── Paciente.java
│   │   └── @Entity
│   │       - id, usuario (FK), dni (unique), nombres, apellidos, fnac
│   │       - sexo, telefono, email, direccion, tipoSangre, alergias
│   │       - @OneToOne HistoriaClinica
│   │
│   ├── Medico.java
│   │   └── @Entity
│   │       - id, usuario (FK), especialidad (FK), cmp (unique)
│   │       - nombres, apellidos, dni, telefono, consultorio
│   │       - @OneToMany HorarioMedico
│   │
│   ├── Especialidad.java
│   │   └── @Entity
│   │       - id, nombre (unique), descripcion, precioConsulta, duracionMinutos
│   │       - @OneToMany Medico
│   │
│   ├── HorarioMedico.java
│   │   └── @Entity
│   │       - id, medico (FK), diaSemana (1-7), horaInicio, horaFin, activo
│   │       - @Unique medico+diaSemana+horaInicio
│   │
│   ├── HistoriaClinica.java
│   │   └── @Entity
│   │       - id, paciente (FK, 1:1), nroHistoria (unique), fechaApertura
│   │       - observaciones, activo
│   │
│   ├── Cita.java
│   │   └── @Entity
│   │       - id, paciente (FK), medico (FK), especialidad (FK)
│   │       - fechaCita, horaInicio, horaFin, estado (7 valores)
│   │       - motivo, canalReserva (WEB/TELEFONO/PRESENCIAL/APP)
│   │       - codigoQr, numeroTurno, fechaCheckin, observaciones
│   │       - @OneToOne Atencion (CASCADE)
│   │       - @OneToMany Pago
│   │
│   ├── Atencion.java
│   │   └── @Entity
│   │       - id, cita (FK, 1:1), historiClinica (FK), medico (FK)
│   │       - fechaAtencion, motivo, anamnesis (CLOB), examenFisico (CLOB)
│   │       - diagnosticoCie10 (FK), diagnosticoDesc, tratamiento (CLOB)
│   │       - vitalSigns: TA, FC, temp, peso, talla, o2
│   │       - firmadoPor (FK usuario), fechaFirma
│   │
│   ├── DiagnosticoCie10.java
│   │   └── @Entity
│   │       - codigo (PK), descripcion, capitulo, activo
│   │
│   ├── Receta.java
│   │   └── @Entity
│   │       - id, atencion (FK), nroReceta (unique), fechaEmision
│   │       - observaciones
│   │       - @OneToMany DetalleReceta (CASCADE)
│   │
│   ├── DetalleReceta.java
│   │   └── @Entity
│   │       - id, receta (FK), nombreGenerico, nombreComercial
│   │       - presentacion, dosis, frecuencia, duracionDias
│   │       - viaAdministracion, indicaciones
│   │
│   ├── OrdenExamen.java
│   │   └── @Entity
│   │       - id, atencion (FK), tipoExamen, nombreExamen
│   │       - indicaciones, estado (SOLICITADO/RESULTANTE)
│   │       - fechaSolicitud, fechaResultado
│   │
│   ├── Pago.java
│   │   └── @Entity
│   │       - id, cita (FK), monto, metodoPago (5 opciones)
│   │       - estado (PENDIENTE/PAGADO/ANULADO/REEMBOLSADO)
│   │       - nroComprobante, tipoComprobante, fechaPago
│   │
│   ├── AuditoriaLog.java
│   │   └── @Entity
│   │       - id, idUsuario, username, accion, entidad, idEntidad
│   │       - detalle (2000 chars), ipOrigen, fechaHora
│   │
│   ├── ConfiguracionSistema.java
│   │   └── @Entity
│   │       - id, parametro, valor, descripcion
│   │
│   └── ... (17 total)
│
├── repository/ (15 interfaces)
│   ├── UsuarioRepository extends JpaRepository<Usuario, Long>
│   ├── PacienteRepository extends JpaRepository<Paciente, Long>
│   ├── MedicoRepository extends JpaRepository<Medico, Long>
│   ├── CitaRepository extends JpaRepository<Cita, Long>
│   ├── AtencionRepository extends JpaRepository<Atencion, Long>
│   ├── RecetaRepository extends JpaRepository<Receta, Long>
│   ├── DetalleRecetaRepository extends JpaRepository<DetalleReceta, Long>
│   ├── PagoRepository extends JpaRepository<Pago, Long>
│   ├── OrdenExamenRepository extends JpaRepository<OrdenExamen, Long>
│   ├── EspecialidadRepository extends JpaRepository<Especialidad, Long>
│   ├── HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long>
│   ├── HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long>
│   ├── DiagnosticoCie10Repository extends JpaRepository<DiagnosticoCie10, String>
│   ├── RolRepository extends JpaRepository<Rol, Long>
│   ├── AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long>
│   └── ... + custom queries (@Query)
│
├── service/ (18 servicios)
│   ├── AuthService
│   │   └── @Service
│   │       - login(username, password) → JWT + UserDetails
│   │       - register(dto) → Usuario creado
│   │       - validateToken(token) → boolean
│   │
│   ├── UsuarioService
│   │   └── @Service
│   │       - crear(dto), actualizar(id, dto), eliminar(id)
│   │       - asignarRol(usuarioId, rolId)
│   │       - buscar(username), buscarPorEmail(email)
│   │
│   ├── PacienteService
│   │   └── @Service
│   │       - CRUD pacientes
│   │       - crearHistoriaClinica(pacienteId) automático
│   │       - validar DNI (8 dígitos, numeric)
│   │
│   ├── MedicoService
│   │   └── @Service
│   │       - CRUD médicos
│   │       - validar CMP (formato)
│   │       - buscar por especialidad
│   │
│   ├── CitaService ⭐ IMPORTANTE
│   │   └── @Service
│   │       - agendar(dto) → Cita creada
│   │       - verificarDisponibilidad(medico, fecha, hora) → boolean
│   │       - cambiarEstado(citaId, nuevoEstado)
│   │       - cancelar(citaId, motivo)
│   │       - obtenerProximas(usuarioId) → list
│   │       - obtenerHistorial(usuarioId) → list
│   │
│   ├── AtencionService ⭐ IMPORTANTE
│   │   └── @Service
│   │       - registrar(dto) → Atencion creada
│   │       - actualizar(id, dto)
│   │       - firmar(atencionId, usuarioId) → firma digital
│   │
│   ├── RecetaService
│   │   └── @Service
│   │       - crear(atencionId, medicamentos[])
│   │       - actualizar(recetaId, dto)
│   │       - obtenerDetalles(recetaId)
│   │
│   ├── PagoService
│   │   └── @Service
│   │       - procesar(dto) → Pago guardado
│   │       - validar monto vs especialidad precioConsulta
│   │       - generar comprobante
│   │       - reembolsar(pagoId)
│   │
│   ├── ReporteService
│   │   └── @Service
│   │       - reportePacientes(fechaInicio, fechaFin)
│   │       - reporteCitas(fechaInicio, fechaFin, estado)
│   │       - reporteIngresos(fechaInicio, fechaFin, agrupadoPor)
│   │       - reporteMedicosTop(limite)
│   │       - reporteEspecialidadesTop(limite)
│   │
│   ├── HorarioMedicoService
│   │   └── @Service
│   │       - crear/actualizar horarios
│   │       - obtenerDisponibles(medicoId, fecha)
│   │
│   ├── HistoriaClinicaService
│   │   └── @Service
│   │       - obtenerPorPaciente(pacienteId)
│   │       - obtenerCitasHistoricas(pacienteId)
│   │
│   ├── DiagnosticoCie10Service
│   │   └── @Service
│   │       - buscar(codigo o descripción)
│   │       - listar(paginado)
│   │
│   ├── OrdenExamenService
│   │   └── @Service
│   │       - crear(dto)
│   │       - cambiarEstado(ordenId, estado)
│   │
│   ├── DetalleRecetaService
│   │   └── @Service
│   │       - agregar(recetaId, dto)
│   │       - eliminar(detalleId)
│   │
│   ├── EmailService
│   │   └── @Service
│   │       - notificarAgendamiento(cita)
│   │       - notificarCancelacion(cita)
│   │       - notificarConsultaCompletada(atencion)
│   │       - enviar(destinatario, asunto, cuerpo, esHTML)
│   │
│   ├── AuditoriaLogService
│   │   └── @Service
│   │       - registrar(accion, entidad, idEntidad, detalles, ipOrigen)
│   │       - buscar(filtros) → list
│   │
│   ├── JwtService (config/)
│   │   └── @Service
│   │       - generateToken(usuario) → JWT string
│   │       - validateToken(token) → boolean
│   │       - extractClaims(token) → Map
│   │
│   └── CustomUserDetailsService (config/)
│       └── @Service implements UserDetailsService
│           - loadUserByUsername(username) → UserDetails con roles
│
├── controller/
│   ├── web/ (13 @Controller - Thymeleaf)
│   │   ├── HomeController
│   │   ├── PacienteWebController
│   │   ├── MedicoWebController
│   │   ├── CitaWebController ⭐ agendar/calendario
│   │   ├── AtencionWebController ⭐ registrar consulta form
│   │   ├── RecetaWebController
│   │   ├── PagoWebController
│   │   ├── HistoriaClinicaWebController
│   │   ├── OrdenExamenWebController
│   │   ├── HorarioWebController
│   │   ├── ReportesWebController
│   │   ├── AuditoriaWebController
│   │   └── ... más
│   │
│   └── api/ (16 @RestController - JSON)
│       ├── AuthRestController
│       ├── PacienteRestController
│       ├── MedicoRestController
│       ├── CitaRestController
│       ├── AtencionRestController ⭐
│       ├── RecetaRestController
│       ├── PagoRestController
│       ├── UsuarioRestController
│       ├── RolRestController
│       ├── HistoriaClinicaRestController
│       ├── HorarioMedicoRestController
│       ├── OrdenExamenRestController
│       ├── DetalleRecetaRestController
│       ├── ReporteRestController ⭐
│       ├── DashboardRestController
│       └── AuditoriaLogRestController
│
├── dto/ (15+ Data Transfer Objects)
│   ├── UsuarioDTO
│   ├── PacienteDTO
│   ├── MedicoDTO
│   ├── EspecialidadDTO
│   ├── CitaDTO
│   ├── AtencionDTO
│   ├── RecetaDTO
│   ├── DetalleRecetaDTO
│   ├── PagoDTO
│   ├── HorarioMedicoDTO
│   ├── HistoriaClinicaDTO
│   ├── DiagnosticoCie10DTO
│   ├── OrdenExamenDTO
│   ├── RolDTO
│   ├── LoginRequest
│   ├── LoginResponse (con token JWT)
│   └── ... más
│
├── exception/
│   ├── GlobalExceptionHandler
│   │   └── @RestControllerAdvice
│   │       - @ExceptionHandler(EntityNotFoundException.class) → 404
│   │       - @ExceptionHandler(ValidationException.class) → 400
│   │       - @ExceptionHandler(Exception.class) → 500
│   │
│   ├── BusinessException extends RuntimeException
│   ├── ResourceNotFoundException extends RuntimeException
│   └── ... más custom exceptions
│
├── util/
│   ├── JwtUtil (deprecado, usar JwtService)
│   ├── DateUtil
│   ├── StringUtil
│   └── NumberUtil
│
├── validation/
│   ├── DNIValidator
│   │   └── Valida DNI: 8 dígitos, numeric, exists si tiene DNI dupl
│   │
│   ├── CMPValidator
│   │   └── Valida CMP médico: formato específico
│   │
│   └── ... más custom validators
│
└── scheduler/ (tareas programadas)
    ├── CitaReminderScheduler
    │   └── @Scheduled(cron = "...")
    │       - Enviar emails recordatorio 1 día antes
    │
    └── ReporteAutomaticoScheduler
        └── @Scheduled(cron = "...")
            - Generar reportes diarios/mensuales
```

---

## 📊 MAPA DE RELACIONES

```
USUARIO (1) ──────────────────M────→ USUARIO_ROL ←────────── ROL
   │ (1)                                                       
   │
   ├─ (M:1) PACIENTE
   │          │ (1:1)
   │          └──→ HISTORIA_CLINICA
   │
   ├─ (M:1) MEDICO
   │         │ (M:1)
   │         └──→ ESPECIALIDAD
   │         │ (1:M)
   │         └──→ HORARIO_MEDICO
   │
   └─ (1:M) AUDITORIA_LOG

PACIENTE (1)────────────────────M────→ CITA ←─────────────────── MEDICO
                              (1)  │    (M:1)
                                   ├────→ ESPECIALIDAD
                                   │ (1:1 CASCADE)
                                   └────→ ATENCION
                                          │ (1:M CASCADE)
                                          ├────→ RECETA
                                          │       │ (1:M CASCADE)
                                          │       └────→ DETALLE_RECETA
                                          │
                                          ├──(M:1)──→ DIAGNOSTICO_CIE10
                                          ├──(M:1)──→ HISTORIA_CLINICA
                                          └──(M:1)──→ USUARIO (firma)

CITA (1)────────────────────M────→ PAGO
```

