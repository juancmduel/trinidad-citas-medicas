# 📅 Plan de Desarrollo por Semana
## Sistema Trinidad Citas Médicas - UTP 2026-I

**Curso:** Marcos de Desarrollo Web (100000S52T)  
**Ciclo:** 2026-I Marzo | 18 Semanas  
**Proyecto:** Sistema de Citas Médicas Trinidad  
**Evaluación:** APF1(S4) 20% + APF2(S8) 20% + APF3(S12) 20% + PROYECTO FINAL(S18) 40%

---

## Semana 1 - Front-end Fundamentos
**Tema Syllabus:** HTML, CSS, JavaScript - Conceptos y Frameworks de Front-end

### Programación a Realizar
- [ ] Estructura HTML5 de páginas principales
- [ ] CSS base con variables y estilos globales
- [ ] JavaScript vanilla para interactividad básica

### Archivos a Crear/Modificar
```
src/main/resources/templates/
├── index.html (landing page)
├── login.html
├── register.html
└── layout.html (base template)

static/
├── css/
│   ├── main.css (estilos base)
│   ├── variables.css (colores, tipografía)
│   └── responsive.css (media queries)
└── js/
    └── main.js (scripts básicos)
```

### Componentes Específicos del Proyecto
- [ ] Página de inicio - Información de clínica
- [ ] Estructura HTML para login
- [ ] Estructura HTML para registro
- [ ] Navbar básica
- [ ] Footer básico

### Tecnologías
- HTML5 semántico
- CSS3 (sin Bootstrap aún)
- JavaScript vanilla

### Checklist de Completitud
- [ ] Código HTML válido y semántico
- [ ] Estilos CSS organizados
- [ ] Responsive básico con media queries
- [ ] Sin errores en consola del navegador

---

## Semana 2 - Bootstrap Fundamentos
**Tema Syllabus:** Introducción a Bootstrap - CDN, Grid, Navbar, Tablas, Íconos

### Programación a Realizar
- [ ] Integrar Bootstrap 5 via WebJars (Spring)
- [ ] Implementar grid system en páginas
- [ ] Crear navbar responsive
- [ ] Componentes de tablas Bootstrap

### Archivos a Crear/Modificar
```
src/main/resources/templates/
├── fragments/
│   ├── header.html (navbar, meta tags)
│   ├── footer.html (pie de página)
│   └── navigation.html (menú)
├── pages/
│   ├── especialidades.html
│   └── medicos.html

pom.xml
├── spring-boot-starter-webjar-bootstrap
└── bootstrap-icons
```

### Componentes Específicos del Proyecto
**trinidad-citas-medicas:**
- [ ] Página de Especialidades - Grid responsive
- [ ] Página de Médicos - Tablas Bootstrap con cards
- [ ] Navbar con logo y menú collapsible
- [ ] Footer con información de contacto

### Checklist de Completitud
- [ ] Bootstrap integrado correctamente
- [ ] Breakpoints responsive (xs, sm, md, lg, xl)
- [ ] Navbar funcional en móvil y desktop
- [ ] Tablas con estilos Bootstrap

---

## Semana 3 - Bootstrap Componentes Avanzados
**Tema Syllabus:** Bootstrap Aplicación - Modales, Formularios, Estilos Párrafo

### Programación a Realizar
- [ ] Formularios con validación Bootstrap
- [ ] Modales para confirmaciones
- [ ] Cards y componentes visuales
- [ ] Alertas y tooltips

### Archivos a Crear/Modificar
```
src/main/resources/templates/
├── forms/
│   ├── form-cita.html
│   ├── form-usuario.html
│   └── form-medico.html
├── modals/
│   ├── modal-confirmacion.html
│   └── modal-error.html

static/css/
└── forms.css (estilos de formularios)
```

### Componentes Específicos del Proyecto
**trinidad-citas-medicas:**
- [ ] Formulario de Agendar Cita (campos: paciente, médico, fecha, hora)
- [ ] Formulario de Registro Usuario (validación visual)
- [ ] Formulario de Login (email, password)
- [ ] Modal de confirmación antes de agendar
- [ ] Modal de error para validaciones

### Checklist de Completitud
- [ ] Todos los formularios con Bootstrap
- [ ] Validación visual en formularios
- [ ] Modales funcionales
- [ ] Componentes Cards implementados
- [ ] Diseño consistente

---

## Semana 4 ⭐ **APF1 - Frontend 20%**
**Tema Syllabus:** Integración de Temas Front-end

### Tareas a Completar
- [ ] Todas las páginas responsivas 100%
- [ ] Formularios funcionales (sin BD)
- [ ] Interfaz atractiva y profesional
- [ ] Navegación completa

### Funcionalidades a Demostrar
- [ ] Página responsive en móvil (320px), tablet (768px), desktop (1024px)
- [ ] Navegación entre todas las páginas
- [ ] Formularios Bootstrap con estilos
- [ ] Íconos visibles (Bootstrap Icons / FontAwesome)
- [ ] Tablas con datos de ejemplo
- [ ] Footer visible en todas las páginas

### Documentación APF1
- [ ] Descripción de estructura HTML
- [ ] Explicación de grid Bootstrap usado
- [ ] Screenshots de responsive design
- [ ] Listado de componentes Bootstrap utilizados

### Evaluación: Frontend Completamente Funcional ✅

---

## Semana 5 - Spring Boot Fundamentos
**Tema Syllabus:** Spring Boot - Definición, Ventajas, Configuración, Creación Proyecto

### Programación a Realizar
- [ ] Crear proyecto Spring Boot 3.2.5
- [ ] Configurar `application.properties`
- [ ] Estructura MVC (controller, service, repository, model)
- [ ] Primera ruta GET funcional

### Archivos a Crear
```
src/main/java/com/citas/
├── TrinidadCitasApplication.java (main)
├── config/
│   └── WebConfig.java
├── controller/
│   └── HomeController.java
├── service/
├── repository/
├── model/
└── dto/

src/main/resources/
├── application.properties
```

### Configuración Spring Boot
```properties
spring.application.name=trinidad-citas-medicas
server.port=8081
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.mode=HTML
spring.thymeleaf.cache=false
spring.jpa.show-sql=true
```

### Checklist
- [ ] Proyecto Spring Boot corre en puerto 8081
- [ ] Primera página visible en http://localhost:8081/
- [ ] Estructura MVC implementada
- [ ] Sin errores en logs

---

## Semana 6 - Spring Web (Controllers y Rutas)
**Tema Syllabus:** Spring Web - Definiciones, Usos, Ventajas, Aplicaciones

### Programación a Realizar
- [ ] Crear @Controller para páginas web
- [ ] Implementar @GetMapping y @PostMapping
- [ ] Pasar datos a vistas con Model
- [ ] Manejo de RedirectAttributes

### Archivos a Crear/Modificar
```
src/main/java/com/citas/controller/
├── HomeController.java
├── UsuarioWebController.java
├── MedicoWebController.java
├── EspecialidadWebController.java
└── CitaWebController.java
```

### Rutas del Proyecto
```
GET  / → index.html
GET  /login → login.html
GET  /usuarios → lista
GET  /medicos → lista
GET  /citas → lista
POST /citas → agendar
```

### Checklist
- [ ] Controllers para 4-5 módulos
- [ ] @GetMapping y @PostMapping funcionando
- [ ] Model pasando datos a vistas
- [ ] Redirecciones con mensajes flash

---

## Semana 7 - Thymeleaf (Motor de Plantillas)
**Tema Syllabus:** Thymeleaf - Definiciones, Usos, Ventajas, Aplicaciones

### Programación a Realizar
- [ ] Crear templates Thymeleaf dinámicos
- [ ] Expresiones y variables (th:text, th:if, th:each)
- [ ] Fragmentos reutilizables
- [ ] Formularios con th:object y th:field

### Archivos a Crear
```
src/main/resources/templates/
├── fragments/
│   ├── header.html
│   ├── footer.html
│   └── sidebar.html
├── usuarios/lista.html
├── medicos/lista.html
└── citas/formulario.html
```

### Checklist
- [ ] Fragmentos reutilizables (header, footer)
- [ ] Variables dinámicas en vistas
- [ ] Iteraciones funcionales
- [ ] URLs dinámicas con @{}
- [ ] Formularios con th:object y th:field

---

## Semana 8 ⭐ **APF2 - Spring Web + Thymeleaf 20%**
**Tema Syllabus:** Integración Spring Web + Thymeleaf

### Tareas a Completar
- [ ] Controllers para 5+ módulos
- [ ] Todas las vistas con Thymeleaf dinámicas
- [ ] Fragmentos reutilizables funcionando
- [ ] Formularios conectados a controllers
- [ ] Navegación entre vistas fluida

### Funcionalidades a Demostrar
- [ ] Página inicio → Menú navega a secciones
- [ ] Listado de usuarios (datos mock)
- [ ] Listado de médicos (datos mock)
- [ ] Formulario de agendar cita (sin guardar aún)
- [ ] Fragmentos compartidos en todas las páginas

### Evaluación: Backend + Vistas Funcionales ✅

---

## Semana 9 - ORM y Spring Data
**Tema Syllabus:** ORM - Hibernate, Spring Data, Configuración BD

### Programación a Realizar
- [ ] Configurar datasource (Oracle/MySQL)
- [ ] Crear entidades JPA con anotaciones
- [ ] Configurar Hibernate
- [ ] Primera tabla generada automáticamente

### Archivos a Crear
```
src/main/java/com/citas/model/
├── Usuario.java
├── Medico.java
├── Especialidad.java
├── Cita.java
└── Rol.java
```

### Configuración BD
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trinidad_citas
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

### Checklist
- [ ] Datasource configurado y conectado
- [ ] Anotaciones JPA en entidades
- [ ] Tablas creadas automáticamente
- [ ] Sin errores de SQL

---

## Semana 10 - JPA y CRUD Completo
**Tema Syllabus:** JPA - CRUD Web y REST, Operaciones contra BD

### Programación a Realizar
- [ ] Crear Repositories JpaRepository
- [ ] Implementar Servicios con lógica CRUD
- [ ] Conectar Controllers a BD
- [ ] CRUD Web funcional

### Archivos a Crear
```
src/main/java/com/citas/
├── repository/
│   ├── UsuarioRepository.java
│   ├── CitaRepository.java
│   └── MedicoRepository.java
└── service/
    ├── UsuarioService.java
    └── CitaService.java
```

### Checklist
- [ ] Repositories creados para todas las entidades
- [ ] Servicios con lógica CRUD
- [ ] CRUD Web funcionando contra BD
- [ ] Datos persistidos en BD

---

## Semana 11 - Spring Validator y Relaciones
**Tema Syllabus:** Spring Validator - Validación, Relaciones entre Tablas

### Programación a Realizar
- [ ] Validaciones con anotaciones Bean Validation
- [ ] Manejo de errores en formularios
- [ ] Relaciones OneToMany, ManyToOne
- [ ] Restricciones en BD

### Anotaciones en Entidades
```
@NotBlank, @NotNull, @Size, @Email
@Valid en controllers
@ManyToOne, @OneToMany en entidades
BindingResult para errores
```

### Checklist
- [ ] Validaciones en todas las entidades
- [ ] @Valid en controllers
- [ ] BindingResult manejado
- [ ] Relaciones funcionales
- [ ] Errores mostrados en vistas

---

## Semana 12 ⭐ **APF3 - BD + CRUD Completo 20%**
**Tema Syllabus:** Integración JPA + Hibernate - CRUD completo BD

### Tareas a Completar
- [ ] CRUD completo para 5+ entidades
- [ ] Todas las relaciones funcionando
- [ ] Validaciones en todos los endpoints
- [ ] Restricciones en BD implementadas
- [ ] Datos reales en BD

### Funcionalidades a Demostrar
- **CRUD Usuarios:** Crear, Listar, Editar, Eliminar
- **CRUD Citas:** Agendar, Listar, Editar, Cancelar
- **Relaciones:** Usuario ↔ Citas, Cita ↔ Médico
- **Validaciones:** Email único, Fechas válidas, etc

### Evaluación: Base de Datos Completamente Operacional ✅

---

## Semana 13 - Spring Security Fundamentos
**Tema Syllabus:** Spring Security - Introducción, Configuración

### Programación a Realizar
- [ ] Integrar Spring Security
- [ ] Crear SecurityConfig
- [ ] Cifrar contraseñas con BCrypt
- [ ] Crear login personalizado
- [ ] Proteger rutas

### Archivos a Crear
```
src/main/java/com/citas/config/
├── SecurityConfig.java
└── PasswordEncoderConfig.java

src/main/java/com/citas/security/
└── CustomUserDetailsService.java

src/main/resources/templates/
└── login.html
```

### Checklist
- [ ] Spring Security integrado
- [ ] PasswordEncoder configurado
- [ ] Login/Logout funcionando
- [ ] Rutas públicas vs privadas
- [ ] UserDetailsService implementado

---

## Semana 14 - Autenticación y Autorización
**Tema Syllabus:** Autorización, Autenticación, Roles, Permisos

### Programación a Realizar
- [ ] Crear tabla Rol en BD
- [ ] Relación Usuario ↔ Rol
- [ ] Implementar autorización por rol
- [ ] Proteger rutas con @PreAuthorize
- [ ] Mostrar/ocultar elementos por rol en vistas

### Checklist
- [ ] Tabla Rol creada en BD
- [ ] Relación Usuario ↔ Rol
- [ ] @PreAuthorize funcionando
- [ ] Menú adaptado según rol
- [ ] Rutas protegidas por rol

---

## Semana 15 - JWT (JSON Web Tokens)
**Tema Syllabus:** JWT - Definición, Usos, Ventajas, Implementación

### Programación a Realizar
- [ ] Integrar librería JWT (jjwt)
- [ ] Crear JwtService
- [ ] Implementar JwtAuthFilter
- [ ] Endpoint /api/auth/login con token
- [ ] Proteger endpoints REST con JWT

### Dependencia pom.xml
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
```

### Checklist
- [ ] JWT librería integrada
- [ ] JwtService creado
- [ ] JwtAuthFilter implementado
- [ ] Endpoint /api/auth/login devuelve token
- [ ] API REST protegida con JWT
- [ ] Token validado en cada request

---

## Semana 16 - Integración Full Stack
**Tema Syllabus:** Integración Total - Frontend + Backend + BD + Security

### Tareas a Completar
- [ ] Frontend + Backend completamente integrados
- [ ] Seguridad en todos los niveles
- [ ] BD sincronizada y operacional
- [ ] Validaciones cliente + servidor
- [ ] Manejo de errores completo

### Stack Completo Verificado
```
✅ FRONTEND: Bootstrap 5, HTML5, CSS3, JavaScript
✅ BACKEND: Spring Boot, Controllers, Services, Repositories
✅ BD: 10+ tablas relacionadas, Constraints
✅ SEGURIDAD: Login, Roles, JWT
```

### Funcionalidades Integradas
- Registro y Login seguros
- Dashboard según rol
- CRUD de citas con validación
- API REST con JWT
- Responsive design completo

---

## Semana 17 - Presentación del Proyecto Final
**Tema Syllabus:** Presentación del Proyecto Final

### Preparación Presentación
- [ ] Diapositivas explicativas
- [ ] Demo en vivo del sistema
- [ ] Explicación de arquitectura
- [ ] Respuestas a preguntas técnicas

### Guion Presentación
```
1. Introducción (2 min)
2. Demo en vivo (5-7 min)
3. Arquitectura (3 min)
4. Tecnologías (2 min)
5. Desafíos y soluciones (2 min)
6. Conclusiones (1 min)
```

---

## Semana 18 ⭐ **PROYECTO FINAL - 40%**
**Tema Syllabus:** Evaluación Final

### Criterios de Evaluación
- Funcionalidad (30%)
- Seguridad (25%)
- Base de Datos (20%)
- Interfaz/UX (15%)
- Código/Documentación (10%)

### Entrega Final
- [ ] Código en GitHub con README
- [ ] BD exportada (SQL script)
- [ ] Instrucciones para ejecutar
- [ ] Presentación realizada
- [ ] Demo funcional demostrada

### Nota Final
```
NOTA FINAL = (APF1×0.20) + (APF2×0.20) + (APF3×0.20) + (PROYECTO_FINAL×0.40)
Nota Mínima Aprobatoria: 12.0
```

---

*Plan actualizado: 2026-05-29 | Trinidad Citas Médicas — UTP Marcos de Desarrollo Web*
