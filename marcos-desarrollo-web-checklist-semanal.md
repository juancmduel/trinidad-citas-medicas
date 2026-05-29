# Marcos de Desarrollo Web — Checklist semanal

> Curso: Marcos de Desarrollo Web (100000S52T) · 2026 Ciclo 1 Marzo
> Stack del curso: Bootstrap (front) + Spring Boot + Thymeleaf + JPA/Hibernate + MySQL + Spring Security/JWT.
> Uso: comparar cada semana contra tu proyecto. Marca `[x]` lo que ya tengas.

**Fórmula de nota:** `20% APF1 + 20% APF2 + 20% APF3 + 40% PROY` · Mínima aprobatoria: **12** · Sin rezagado, sin reemplazo de notas · Evaluaciones flexibles (individual o grupal a tu elección).

---

## Unidad 1 — Frameworks de front-end (Semanas 1–4)

### Semana 1 — Fundamentos front end
**Temas:** HTML, CSS, JavaScript; conceptos y frameworks de front end. Presentación del curso.

- [ ] Estructura HTML semántica de al menos una vista
- [ ] CSS base aplicado (layout, tipografía)
- [ ] JavaScript funcional en alguna interacción
- [ ] Entiendes qué problema resuelve un framework de front end vs HTML/CSS/JS puro

### Semana 2 — Introducción a Bootstrap
**Temas:** definición e instalación vía CDN, contenedores, sistema de columnas/filas (responsivo), navbar, imágenes, tablas e íconos. Página con header, body y footer responsiva.

- [ ] Bootstrap cargado por CDN
- [ ] Layout con sistema de grid (rows/cols) que responde a distintos anchos
- [ ] Navbar funcional
- [ ] Página con encabezado, cuerpo y pie de página
- [ ] Uso de tablas, imágenes e íconos de Bootstrap

### Semana 3 — Aplicación de Bootstrap
**Temas:** elementos web, ventanas modales, estilos de párrafo, formularios. Página con formularios y ventanas flotantes.

- [ ] Al menos un formulario maquetado con clases de Bootstrap
- [ ] Una ventana modal funcional
- [ ] Validación visual básica de formulario

### Semana 4 — Integración front-end · 🎯 APF1
**Temas:** integrar el framework de front-end, configurar Bootstrap, GUI con ventanas flotantes, formularios y otros elementos.

- [ ] GUI integrada y navegable (varias vistas conectadas)
- [ ] Diseño responsivo coherente en todo el front
- [ ] **Entregable APF1 (Avance de Proyecto Final 1):** front-end del proyecto funcionando con Bootstrap — vistas principales, formularios y modales, todo responsivo. Decide individual o grupal.

---

## Unidad 2 — Introducción a Spring (Semanas 5–8)

### Semana 5 — Java back-end + Spring Boot
**Temas:** qué es el back-end, lenguajes y frameworks de back-end, ventajas. Spring Boot: definición, ventajas, importancia; configurar entorno y crear proyecto.

- [ ] Entorno Java + Spring Boot instalado (JDK, IDE/Maven o Gradle)
- [ ] Proyecto Spring Boot creado y arrancando (`localhost`)
- [ ] Estructura de paquetes definida (controller, service, model)

### Semana 6 — Spring Web
**Temas:** definiciones, usos, ventajas y aplicaciones de Spring Web.

- [ ] Controlador con rutas que responden HTTP
- [ ] Una ruta que devuelve datos/vista correctamente

### Semana 7 — Thymeleaf
**Temas:** definiciones, usos, ventajas y aplicaciones del motor de plantillas.

- [ ] Thymeleaf configurado en el proyecto
- [ ] Una plantilla que renderiza datos del modelo (`${...}`)
- [ ] Fragmentos/layout reutilizable (header/footer)

### Semana 8 — Integración Spring Web + Thymeleaf · 🎯 APF2
**Temas:** implementar Spring Web junto con Thymeleaf.

- [ ] Front (Bootstrap) ya servido desde Thymeleaf, no como HTML suelto
- [ ] Navegación entre vistas servidas por el back-end
- [ ] **Entregable APF2:** sitio web estático funcionando con Spring Boot + Thymeleaf, mostrando las vistas de Bootstrap integradas. Individual o grupal.

---

## Unidad 3 — Spring con bases de datos (Semanas 9–12)

### Semana 9 — ORM y Spring Data
**Temas:** ORM, Hibernate, Spring Data; configuración de Spring Data con MySQL.

- [ ] MySQL instalado y base de datos creada
- [ ] Conexión Spring ↔ MySQL configurada (`application.properties`)
- [ ] Hibernate/Spring Data funcionando (entidad mapeada a tabla)

### Semana 10 — JPA y CRUD
**Temas:** JPA: definición, usos; construcción de proyectos web y REST con Spring para CRUD sobre MySQL.

- [ ] Entidades JPA con `@Entity`, `@Id`
- [ ] Repositorio (`JpaRepository`) por entidad
- [ ] CRUD completo (crear, leer, actualizar, borrar) funcionando contra MySQL

### Semana 11 — Spring Validator y relaciones
**Temas:** relaciones entre tablas, validación de datos, restricciones; app web con validación para CRUD.

- [ ] Relaciones entre tablas (`@OneToMany`/`@ManyToOne`, etc.)
- [ ] Validaciones de datos (`@NotNull`, `@Size`, etc.) con mensajes de error
- [ ] Restricciones a nivel de tabla aplicadas

### Semana 12 — Integración JPA + Hibernate · 🎯 APF3
**Temas:** implementar JPA con Hibernate en Spring Web para CRUD en BD.

- [ ] CRUD completo desde la interfaz (no solo por API)
- [ ] Datos persistiendo correctamente con validación activa
- [ ] **Entregable APF3:** sitio web **dinámico** con CRUD completo conectado a MySQL, relaciones y validaciones. Individual o grupal.

---

## Unidad 4 — Spring Security (Semanas 13–18)

### Semana 13 — Introducción a Spring Security
**Temas:** definiciones, tipos de validación, configuración de Spring Security en un proyecto web.

- [ ] Spring Security agregado como dependencia
- [ ] Configuración de seguridad básica (rutas públicas vs protegidas)

### Semana 14 — Autenticación y autorización
**Temas:** conceptos, ventajas, usos; construcción de proyecto con autenticación y autorización de usuarios.

- [ ] Login funcional (autenticación)
- [ ] Roles/permisos diferenciados (autorización)
- [ ] Usuarios persistidos en BD

### Semana 15 — JWT
**Temas:** definición, usos, ventajas, importancia; implementación de Spring Security con JWT.

- [ ] Generación de token JWT al autenticar
- [ ] Validación de JWT en rutas protegidas

### Semana 16 — Integración total front + back
**Temas:** proyecto web con conexión a BD, validación y seguridad mediante Spring + Thymeleaf + Bootstrap.

- [ ] Front (Bootstrap/Thymeleaf) + back (Spring) + BD (MySQL) + seguridad, todo integrado
- [ ] Flujo completo: login → operar CRUD según rol → logout

### Semana 17 — Presentación del proyecto final
**Temas:** presentación del proyecto final del curso.

- [ ] Proyecto desplegable/ejecutable sin errores
- [ ] Documentación mínima y guion de demo listos

### Semana 18 — 🎯 PROYECTO FINAL (40%)
- [ ] **Entregable PROY:** aplicación web integral — front responsivo (Bootstrap), back Spring Boot + Thymeleaf, persistencia MySQL con JPA/Hibernate, relaciones y validaciones, y seguridad con Spring Security + JWT. Es el 40% de la nota.

---

## Resumen de hitos

| Hito | Semana | Peso | Qué entregar |
|------|--------|------|--------------|
| APF1 | 4  | 20% | Front-end con Bootstrap (vistas, formularios, modales, responsivo) |
| APF2 | 8  | 20% | Sitio estático con Spring Boot + Thymeleaf |
| APF3 | 12 | 20% | Sitio dinámico con CRUD + MySQL + validaciones |
| PROY | 18 | 40% | App integral con seguridad (Spring Security + JWT) |
