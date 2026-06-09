#!/bin/bash

# Crear estructura de datos con temas por semana
cat > temas.txt << 'THEMES'
SEMANA 1,HTML CSS JavaScript,Estructura HTML5 de páginas principales,CSS base con variables y estilos globales,JavaScript vanilla para interactividad básica
SEMANA 2,Bootstrap Fundamentos,Integrar Bootstrap 5 via WebJars,Implementar grid system en páginas,Crear navbar responsive
SEMANA 3,Bootstrap Avanzados,Formularios con validación Bootstrap,Modales para confirmaciones,Cards y componentes visuales
SEMANA 4,APF1 - Frontend 20%,Todas las páginas responsivas 100%,Formularios funcionales,Interfaz atractiva y profesional
SEMANA 5,Spring Boot Fundamentos,Crear proyecto Spring Boot 3.2.5,Configurar application.properties,Estructura MVC funcional
SEMANA 6,Spring Web Controllers,Crear @Controller para páginas web,Implementar @GetMapping y @PostMapping,Pasar datos a vistas con Model
SEMANA 7,Thymeleaf Plantillas,Crear templates Thymeleaf dinámicos,Expresiones y variables,Fragmentos reutilizables
SEMANA 8,APF2 - Spring Web 20%,Controllers para 5+ módulos,Todas las vistas con Thymeleaf dinámicas,Fragmentos reutilizables funcionando
SEMANA 9,ORM y Spring Data,Configurar datasource,Crear entidades JPA con anotaciones,Configurar Hibernate
SEMANA 10,JPA y CRUD Completo,Crear Repositories JpaRepository,Implementar Servicios con lógica CRUD,Conectar Controllers a BD
SEMANA 11,Spring Validator,Validaciones con anotaciones Bean,Manejo de errores en formularios,Relaciones OneToMany ManyToOne
SEMANA 12,APF3 - BD + CRUD 20%,CRUD completo para 5+ entidades,Todas las relaciones funcionando,Validaciones en todos endpoints
THEMES

# Crear archivo Word XML
cat > document.xml << 'DOCX'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <w:body>
    <w:p>
      <w:pPr>
        <w:pStyle w:val="Heading1"/>
        <w:jc w:val="center"/>
      </w:pPr>
      <w:r>
        <w:rPr>
          <w:b/>
          <w:sz w:val="48"/>
        </w:rPr>
        <w:t>PLAN DE DESARROLLO - TRINIDAD CITAS MEDICAS</w:t>
      </w:r>
    </w:p>
    <w:p>
      <w:pPr>
        <w:jc w:val="center"/>
      </w:pPr>
      <w:r>
        <w:t>UTP 2026-I | Marcos de Desarrollo Web</w:t>
      </w:r>
    </w:p>
    <w:p><w:br/></w:p>
DOCX

echo "Generando documento Word..."
