**CURSO:**

**MARCO DE DESARROLLO WEB- SECCION 20611**

**TEMA:**

**"SISTEMA DE GESTIÓN DE CITAS MÉDICAS Y ADMINISTRACIÓN**

**PARA TRINIDAD & ESPECIALIDADES MÉDICAS S.A.C."**

**DOCENTE:**

**MELQUIADES EFRAIN MELGAREJO GRACIANO**

**ABRIL 2026**

**INFORME DE PROYECTO**

**"SISTEMA DE GESTIÓN DE CITAS MÉDICAS Y ADMINISTRACIÓN**

**PARA TRINIDAD & ESPECIALIDADES MÉDICAS S.A.C."**

EMPRESA: Trinidad & Especialidades Médicas S.A.C.

RUC: 20494088208

Lima – Perú

2026

**CUADRO DE INTEGRANTES Y ESFUERZO**

|     |     |     |     |     |     |     |
| --- | --- | --- | --- | --- | --- | --- |
| **Nro.** | **Apellidos y Nombres** | **S8** | **S9** | **S10** | **S11** | **Promedio** |
| 1   | COLLANTE MEZA, JHUNIOR | 100% | 100% | 100% | 100% | 100% |
| 2   | CUBAS ROJAS LEONARDO GABRIEL | 0%  | 0%  | 0%  | 0%  | 0%  |
| 3   | HURTADO ASCA, KENNEDY ALBERTH | 100% | 100% | 100% | 100% | 100% |
| 4   | PAREJA MATIAS, JUAN CARLOS | 100% | 100% | 100% | 100% | 100% |
| 5   | REATEGUI HUAYCAMA CALEB | 100% | 100% | 100% | 100% | 100% |
| 6   | REAÑO ROMERO PEDRO JOSE | 100% | 100% | 100% | 100% | 100% |

| ÍNDICE DE CONTENIDO | Pág. |
| --- | --- |

CUADRO DE INTEGRANTES Y ESFUERZO 3

ÍNDICE DE CONTENIDO 4

INTRODUCCIÓN 6

CAPÍTULO 1 7

1\. Información de Empresa/Negocio 7

1.a. Descripción 7

1.b. Misión 7

1.c. Visión 8

1.d. Organigrama 8

1.e. Listado de Procesos 9

2\. Producto del Proyecto 9

2.a. Realidad Problemática 10

2.b. Objetivos 10

2.c. Alcances y Limitaciones 11

3\. Plan del Proyecto 12

3.a. Recursos y Materiales 13

3.b. Costos del Equipo 13

3.c. Cronograma (Diagrama de Gantt) 14

CAPÍTULO 2 15

4\. Marco Teórico 15

4.a. Antecedentes 16

4.b. Fundamento Teórico 18

CAPÍTULO 3 23

5\. Documentación de Negocio usando BPMN 23

5.a. Identificación de Procesos Principales, de Soporte y Estratégicos 24

5.b. Objetivos, Descripción, Actores y Reglas de Negocio 27

5.c. Modelado de Procesos Actuales (AS-IS) 30

5.d. Análisis de Procesos 32

5.e. Definición de Procesos Futuros (TO-BE) 36

6\. Análisis 37

6.a. Requerimientos Funcionales 38

6.b. Requerimientos No Funcionales 40

6.c. Historias de Usuario / Modelo de Casos de Uso 42

6.d. Diagramas del Dominio (UML) 45

6.e. Matriz de Trazabilidad de Requisitos 47

CAPÍTULO 4 54

7\. Diseño 55

7.a. Diseño de Arquitectura del Sistema 60

7.b. Diseño Detallado de Componentes 64

7.c. Diseño de Base de Datos 70

7.d. Diseño de la Interfaz de Usuario (UI/UX) 73

7.e. Especificación de Interfaces y APIs 77

8\. IMPLEMENTACION………………………………………………………………………78

a. Semana 01……………………………………………………………………79

b. Semana 02……………………………………………………………………79

c. Semana 03........................................................................................................80

d. Semana 04........................................................................................................80

e. Semana 05........................................................................................................81

f. Semana 06........................................................................................................83

g. Semana 07.......................................................................................................84

h. Semana 08.......................................................................................................86

i. Semana 09........................................................................................................89

j. Semana 10........................................................................................................89

k. Semana 11........................................................................................................90

l. Semana 12........................................................................................................90

9\. CONCLUSIONES 91

10\. RECOMENDACIONES 92

11\. BIBLIOGRAFÍAS 93

**INTRODUCCIÓN**

El presente informe documenta el desarrollo del proyecto de Sistema de Gestión de Citas Médicas y Administración para la empresa Trinidad & Especialidades Médicas S.A.C., un centro médico especializado ubicado en la ciudad de Tarapoto, departamento de San Martín, Perú. La empresa cuenta con más de 10 años de experiencia brindando servicios de salud especializados, con más de 15 especialidades médicas y atención las 24 horas del día.

La realidad problemática identificada radica en que los procesos de gestión de citas, registro de pacientes, control de historias clínicas y administración de servicios se realizan de manera manual o con herramientas básicas que no se integran entre sí, generando ineficiencias operativas, demoras en la atención y riesgos en la integridad de la información médica.

El proyecto propone el diseño e implementación de un sistema web integral que permita automatizar y optimizar los procesos clave del centro médico, utilizando tecnologías modernas: Java como backend, HTML + CSS + JavaScript como frontend, y Oracle como motor de base de datos. El desarrollo se enmarca dentro del curso Integrador I: Sistemas - Software, aplicando las metodologías y marcos teóricos aprendidos a lo largo de la formación académica.

El informe se estructura en cuatro capítulos: el primero presenta la información de la empresa, la definición del producto del proyecto y su planificación; el segundo capítulo aborda el marco teórico con antecedentes y fundamentos; el tercer capítulo documenta los procesos de negocio usando BPMN y el análisis de requerimientos; finalmente, el cuarto capítulo detalla el diseño del sistema y su implementación.

**CAPÍTULO 1**

**1\. Información de Empresa/Negocio**

**1.a. Descripción**

Trinidad & Especialidades Médicas S.A.C. es un centro médico especializado ubicado en Pasaje Las Mesetas N°112, Partido Alto, distrito de Tarapoto, provincia y departamento de San Martín, Perú. Fue constituido el 09 de diciembre de 2011 como Sociedad Anónima Cerrada, con RUC 20494088208. La empresa está clasificada por el Ministerio de Salud como Centro de Salud con Camas de Internamiento (Categoría I-4) y pertenece a la Dirección de Salud (DISA) San Martín.

El centro médico cuenta con más de 15 especialidades médicas, más de 20 médicos especialistas altamente calificados, equipamiento de tecnología avanzada (resonador magnético, tomógrafo multicortes, rayos X, ecografía) y brinda atención las 24 horas. Sus instalaciones cumplen con las normas dictadas por el Ministerio de Salud. Fue calificada por SUNAT como Buen Contribuyente y está empadronada en el Registro Nacional de Proveedores del Estado.

Datos de contacto: Teléfono: (042) 341-329 / Celular: 942 868 247 / Email: recepcion@trinidadtarapoto.com / Web: https://trinidadtarapoto.com

**1.b. Misión**

Somos una institución comprometida con la mejora de la salud de la población sanmartinense, brindando atención integral de calidad con personal de salud experimentado y altamente capacitado, enfocados en los principios bioéticos de la medicina: no hacer daño, beneficencia y no maleficencia, orientados en la promoción, prevención, recuperación y rehabilitación de la salud.

**1.c. Visión**

Ser el centro médico líder en servicios de salud especializados en la región San Martín y el oriente peruano, reconocidos por la excelencia en la atención médica, la innovación tecnológica y el compromiso con el bienestar integral de nuestros pacientes, proyectándonos hacia la comunidad a través de la mejora continua y la capacitación permanente de nuestros profesionales.

**1.d. Organigrama**

_Tabla 1. Organigrama de Trinidad & Especialidades Médicas S.A.C._

**1.e. Listado de Procesos**

_Tabla 2. Listado de procesos identificados en Trinidad & Especialidades Médicas S.A.C._

|     |     |     |
| --- | --- | --- |
| **Código** | **Proceso** | **Tipo** |
| PE-01 | Planificación estratégica institucional | Estratégico |
| PE-02 | Gestión de calidad y mejora continua | Estratégico |
| PP-01 | Admisión y registro de pacientes | Principal |
| PP-02 | Gestión de citas médicas | Principal |
| PP-03 | Atención en consulta externa (consultorios) | Principal |
| PP-04 | Atención de emergencias | Principal |
| PP-05 | Hospitalización / Internamiento | Principal |
| PP-06 | Diagnóstico por imágenes | Principal |
| PP-07 | Laboratorio clínico | Principal |
| PP-08 | Cirugías y procedimientos quirúrgicos | Principal |
| PS-01 | Gestión de historias clínicas | Soporte |
| PS-02 | Gestión de farmacia y medicamentos | Soporte |
| PS-03 | Gestión de banco de sangre | Soporte |
| PS-04 | Facturación y cobranzas | Soporte |
| PS-05 | Gestión de recursos humanos | Soporte |
| PS-06 | Logística y abastecimiento | Soporte |
| PS-07 | Gestión de nutrición y dietética | Soporte |

**2\. Producto del Proyecto**

**2.a. Realidad Problemática**

Trinidad & Especialidades Médicas S.A.C. es un centro médico que ha experimentado un crecimiento sostenido durante sus más de 12 años de funcionamiento, ampliando sus especialidades a más de 15 y sus servicios de apoyo al diagnóstico con equipamiento de alta tecnología. Sin embargo, este crecimiento no ha sido acompañado por una modernización proporcional de sus procesos administrativos y operativos, lo que genera las siguientes problemáticas:

- Gestión manual de citas: Los pacientes deben acudir presencialmente o llamar por teléfono para solicitar citas, lo que genera congestión en la recepción, tiempos de espera prolongados y pérdida de pacientes por falta de disponibilidad inmediata.
- Registro duplicado de pacientes: Al no contar con un sistema centralizado, la información de los pacientes se registra en múltiples formatos (cuadernos, hojas de cálculo, fichas físicas), generando duplicidad de datos e inconsistencias.
- Historias clínicas físicas: Las historias clínicas en papel están expuestas a deterioro, pérdida y dificultad de acceso rápido por parte de los médicos.
- Falta de trazabilidad en la atención: No se cuenta con un seguimiento digitalizado del flujo del paciente desde su admisión hasta su alta o derivación.
- Reportes y estadísticas limitadas: La generación de reportes de atención, productividad médica y ocupación es manual y consume tiempo considerable.
- Ausencia de canal digital para pacientes: Los pacientes no cuentan con un portal web donde puedan consultar horarios, agendar citas, ver resultados o acceder a información de sus atenciones.

**2.b. Objetivos**

**Objetivo General:**

Desarrollar un sistema web de gestión de citas médicas y administración integral para Trinidad & Especialidades Médicas S.A.C. que permita automatizar los procesos de admisión, programación de citas, gestión de historias clínicas y generación de reportes, mejorando la eficiencia operativa y la calidad de atención al paciente.

**Objetivos Específicos:**

- Digitalizar el proceso de registro y admisión de pacientes, eliminando la duplicidad de información.
- Implementar un módulo de gestión de citas médicas en línea con disponibilidad de horarios en tiempo real para las 15+ especialidades.
- Desarrollar un módulo de historias clínicas electrónicas que permita el registro, consulta y actualización de la información médica de forma segura.
- Crear un módulo de administración de personal médico con gestión de horarios y especialidades.
- Implementar un dashboard de reportes y estadísticas para la gerencia con indicadores clave de gestión.
- Diseñar una interfaz web responsiva y accesible para pacientes y personal administrativo.

**2.c. Alcances y Limitaciones**

**Alcances:**

- El sistema cubrirá los módulos de: Admisión de pacientes, Gestión de citas, Historias clínicas electrónicas, Gestión de médicos y horarios, Facturación básica, Reportes y dashboard gerencial.
- Se implementará como aplicación web accesible desde navegadores modernos.
- El sistema estará orientado a los procesos de consultorios externos (atención ambulatoria).
- Se utilizarán las siguientes tecnologías: Frontend: HTML5 + CSS3 + JavaScript; Backend: Java (Spring Boot / Servlets); Base de datos: Oracle Database.

**Limitaciones:**

- No se incluirá integración con sistemas de diagnóstico por imágenes (PACS/RIS) ni con equipos médicos.
- El módulo de farmacia se limitará a la prescripción médica, sin gestión de inventario de medicamentos.
- No se contempla la integración con sistemas de seguros o SIS.
- El alcance del proyecto se limita al período académico del curso.

**3\. Plan del Proyecto**

**3.a. Recursos y Materiales**

_Tabla 3. Recursos humanos del equipo de proyecto._

|     |     |     |
| --- | --- | --- |
| **Recurso** | **Rol** | **Responsabilidad** |
| Integrante 1 | Líder de proyecto / Analista | Coordinación, análisis de requerimientos, documentación |
| Integrante 2 | Desarrollador Backend | Desarrollo de API, lógica de negocio, base de datos |
| Integrante 3 | Desarrollador Frontend / QA | Diseño de interfaz, pruebas, despliegue |

_Tabla 4. Recursos tecnológicos y materiales._

|     |     |     |
| --- | --- | --- |
| **Recurso** | **Descripción** | **Tipo** |
| Laptops (3 unidades) | Core i5, 8GB RAM, SSD 256GB | Hardware |
| Oracle Database 21c XE | Motor de base de datos relacional | Software |
| JDK 17 | Java Development Kit para backend | Software |
| Apache Tomcat 10 | Servidor de aplicaciones web | Software |
| VS Code / IntelliJ IDEA | IDE de desarrollo | Software |
| Git / GitHub | Control de versiones y repositorio | Software |
| Figma | Diseño de prototipos UI/UX | Software |
| Bizagi Modeler | Modelado de procesos BPMN | Software |
| Draw.io / StarUML | Diagramas UML | Software |
| Internet (banda ancha) | Conexión para investigación y coordinación | Servicio |

**3.b. Costos del Equipo**

_Tabla 5. Presupuesto estimado del proyecto._

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Ítem** | **Descripción** | **Cantidad** | **Costo Unit. (S/)** | **Total (S/)** |
| 1   | Alquiler de hosting (4 meses) | 1   | 50.00 | 200.00 |
| 2   | Dominio web | 1   | 50.00 | 50.00 |
| 3   | Licencia Oracle (XE gratuita) | 1   | 0.00 | 0.00 |
| 4   | Material de oficina | Global | 30.00 | 30.00 |
| 5   | Internet y electricidad (4 meses) | 4   | 80.00 | 320.00 |
| 6   | Impresión de informe final | 3   | 25.00 | 75.00 |
|     | TOTAL |     |     | 795.00 |

**3.c. Cronograma (Diagrama de Gantt)**

_Tabla 6. Cronograma del proyecto – Diagrama de Gantt._

|     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| **Actividad** | **S1** | **S2** | **S3** | **S4** | **S5** | **S6** | **S7** | **S8** | **S9** | **S10** | **S11** | **S12** | **S13** | **S14** | **S15** | **S16** |
| Recopilación de información |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Análisis de requerimientos |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Documentación BPMN (AS-IS) |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Diseño de procesos (TO-BE) |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Diseño de BD (lógico/físico) |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Diseño de arquitectura |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Prototipos UI/UX |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Desarrollo Mód. Admisión |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Desarrollo Mód. Citas |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Desarrollo Mód. Hist. Clínicas |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Desarrollo Mód. Médicos/Horarios |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Desarrollo Mód. Reportes |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Integración y pruebas |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Despliegue y capacitación |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Documentación final |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Exposición del proyecto |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |

**CAPÍTULO 2**

**4\. Marco Teórico**

**4.a. Antecedentes**

**Antecedente 1 (Internacional):**

Gómez, R. y López, M. (2022). “Diseño e implementación de un sistema de gestión de citas médicas para la Clínica San Rafael, Bogotá”. Tesis de grado, Universidad Nacional de Colombia. Los investigadores desarrollaron un sistema web utilizando Java Spring Boot y PostgreSQL para automatizar la gestión de citas en una clínica con 10 especialidades. Los resultados mostraron una reducción del 60% en el tiempo de espera para agendar citas y una mejora del 45% en la satisfacción del paciente. Este antecedente es relevante porque aborda una problemática similar en una clínica de tamaño comparable.

**Antecedente 2 (Nacional):**

Huamán, C. (2023). “Sistema web de gestión de historias clínicas electrónicas para el Centro de Salud La Merced, Junín”. Tesis de grado, Universidad Continental. El autor implementó un sistema de historias clínicas electrónicas usando PHP, JavaScript y MySQL, logrando digitalizar el 100% de las historias clínicas activas y reduciendo el tiempo de búsqueda de información de 15 minutos a menos de 30 segundos. Este antecedente aporta a nuestro proyecto la experiencia en la migración de registros físicos a digitales.

**Antecedente 3 (Local/Regional):**

Reategui, L. y Pinchi, J. (2021). “Sistema de información para la gestión de citas y atenciones médicas en el Hospital II-2 Tarapoto”. Tesis de grado, Universidad Nacional de San Martín. Los autores desarrollaron un sistema de información para gestionar citas médicas usando tecnologías web (Java, Oracle), reportando una reducción del 70% en errores de programación de citas y una mejora significativa en la productividad del personal de admisión. Este antecedente es directamente relevante por tratarse de la misma región y un contexto similar al de Trinidad.

**Antecedente 4 (Nacional – enfoque amazónico):**  
Vásquez, P. y Ríos, D. (2022). “Implementación de un sistema web para la gestión de citas médicas en establecimientos de salud de la región Loreto”. Tesis de grado, Universidad Nacional de la Amazonía Peruana.  
Los autores desarrollaron un sistema web orientado a centros de salud con limitaciones tecnológicas, utilizando Java y MySQL, enfocado en mejorar la accesibilidad a la programación de citas en zonas con alta demanda y limitada conectividad. Los resultados evidenciaron una reducción del 50% en el tiempo de registro de pacientes y una mejora significativa en la organización del flujo de atención.  
Este antecedente es relevante para el presente proyecto debido a que se desarrolla en un contexto amazónico similar, donde existen desafíos en infraestructura tecnológica y gestión de servicios de salud, lo que permite validar la viabilidad del sistema propuesto en entornos regionales.

**Antecedente 5 (Nacional – enfoque en optimización de procesos hospitalarios):**

Quispe, A. y Mendoza, R. (2023). "Sistema web para la optimización de la gestión de citas y atención al paciente en la Clínica Privada Santa Rosa, Arequipa". Tesis de grado, Universidad Tecnológica del Perú.

Los autores desarrollaron un sistema web orientado a clínicas privadas de mediana envergadura, utilizando Java Spring Boot como backend, Angular para el frontend y PostgreSQL como gestor de base de datos. El sistema incorporó un módulo de agendamiento en línea, gestión de historias clínicas electrónicas y un panel de control administrativo con indicadores de gestión. Los resultados demostraron una reducción del 65% en los tiempos de espera de admisión, una disminución del 40% en la tasa de inasistencias gracias al envío de recordatorios automáticos por correo electrónico, y una mejora del 38% en la satisfacción del paciente medida mediante encuestas post-atención.

Este antecedente resulta relevante para el presente proyecto porque aborda una clínica privada de características similares a Trinidad & Especialidades Médicas S.A.C. en cuanto a volumen de especialidades y modelo de atención ambulatoria. Además, los resultados obtenidos en la reducción de inasistencias mediante recordatorios automatizados respaldan la viabilidad de la mejora PM-04 propuesta en el análisis de procesos del Capítulo 3.

**4.b. Fundamento Teórico**

**Fundamento 1: Sistemas de Información en Salud**

Los Sistemas de Información en Salud (SIS) son herramientas tecnológicas diseñadas para recopilar, almacenar, gestionar y transmitir información relacionada con la salud de los individuos o las actividades de las organizaciones de salud (OMS, 2020). Estos sistemas permiten la automatización de procesos clínicos y administrativos, mejorando la calidad de atención, reduciendo errores médicos y optimizando el uso de recursos. En el contexto peruano, la Ley N° 30024 (Ley que crea el Registro Nacional de Historias Clínicas Electrónicas) establece el marco legal para la implementación de historias clínicas electrónicas en establecimientos de salud, lo que refuerza la necesidad de este tipo de sistemas.

**Fundamento 2: Gestión de Procesos de Negocio (BPM) y BPMN**

La Gestión de Procesos de Negocio (BPM) es una disciplina que combina métodos, herramientas y tecnologías para diseñar, modelar, ejecutar, monitorear y optimizar los procesos de negocio de una organización (Dumas et al., 2018). BPMN (Business Process Model and Notation) es el estándar internacional para el modelado de procesos de negocio, que proporciona una notación gráfica comprensible tanto para analistas de negocio como para desarrolladores técnicos. La aplicación de BPM y BPMN en el sector salud permite identificar ineficiencias, cuellos de botella y oportunidades de mejora en los procesos clínicos y administrativos.

**Fundamento 3: Arquitectura de Aplicaciones Web Multicapa**

La arquitectura multicapa (n-tier) es un patrón de diseño de software que separa la lógica de presentación, la lógica de negocio y el acceso a datos en capas independientes (Sommerville, 2016). En el contexto de aplicaciones web, esto se traduce en: Capa de presentación (Frontend): interfaz de usuario construida con HTML, CSS y JavaScript; Capa de lógica de negocio (Backend): procesamiento de reglas de negocio implementado en Java; Capa de datos: persistencia y gestión de datos mediante Oracle Database. Esta arquitectura proporciona ventajas como escalabilidad, mantenibilidad, seguridad y reutilización de componentes.

**Fundamento 4: Metodología de Desarrollo de Software**

Para el presente proyecto se adopta un enfoque de desarrollo iterativo e incremental, combinando prácticas de RUP (Rational Unified Process) para la documentación y modelado, con elementos ágiles para la implementación. Este enfoque permite entregar funcionalidades de forma progresiva, obtener retroalimentación temprana del cliente y adaptar el desarrollo a los cambios que puedan surgir durante el ciclo de vida del proyecto.

**Fundamento 5: Base de Datos Oracle**

Oracle Database es un sistema de gestión de bases de datos relacional (RDBMS) desarrollado por Oracle Corporation, reconocido por su robustez, seguridad, escalabilidad y soporte para transacciones complejas (Oracle, 2024). La versión Express Edition (XE) proporciona una solución gratuita adecuada para proyectos de pequeña y mediana escala. Oracle soporta procedimientos almacenados (PL/SQL), triggers, vistas materializadas y control de concurrencia, características esenciales para un sistema de gestión médica que requiere integridad transaccional y seguridad de datos sensibles.

**Fundamento 6: Tecnologías Web Frontend (HTML5, CSS3, JavaScript)**

HTML5 proporciona la estructura semántica de las páginas web, CSS3 permite el diseño visual responsivo y adaptable a múltiples dispositivos, y JavaScript habilita la interactividad y la comunicación asíncrona con el servidor (AJAX/Fetch API). Estas tecnologías conforman la base estándar del desarrollo frontend moderno, ampliamente soportadas por todos los navegadores y con una vasta comunidad de soporte y recursos disponibles.

**Fundamento 7: Seguridad de la Información en Sistemas Médicos**

La seguridad de la información en el ámbito de la salud es fundamental debido a la sensibilidad de los datos clínicos. Implica la protección de la confidencialidad, integridad y disponibilidad de la información. En el Perú, la Ley N° 29733 establece lineamientos para el tratamiento adecuado de datos personales, incluyendo los datos de salud.  
Para sistemas de gestión médica, esto implica implementar mecanismos como autenticación de usuarios, control de accesos por roles, cifrado de datos y auditoría de actividades. En el proyecto propuesto, estas prácticas son esenciales para garantizar la protección de las historias clínicas electrónicas y la confianza de los pacientes en el sistema.

**Fundamento 8: Experiencia de Usuario (UX) y Usabilidad en Sistemas de Salud**

La experiencia de usuario (UX) hace referencia al conjunto de percepciones, emociones y respuestas que tiene una persona al interactuar con un sistema o producto digital (Nielsen & Norman Group, 2023).

En el contexto de sistemas de salud, la usabilidad cobra especial importancia porque los usuarios finales —pacientes, médicos y personal administrativo— tienen perfiles tecnológicos muy variados y no siempre cuentan con formación técnica. La norma ISO 9241-11 define la usabilidad como la medida en que un producto puede ser usado por usuarios específicos para alcanzar objetivos concretos con efectividad, eficiencia y satisfacción. Aplicar principios de UX durante el diseño del sistema garantiza interfaces intuitivas, reduce los errores de operación y aumenta la tasa de adopción por parte del personal del centro médico, factores críticos para el éxito de cualquier sistema de información en salud.

**Fundamento 9: Arquitectura REST y APIs Web**

REST (Representational State Transfer) es un estilo de arquitectura de software para sistemas distribuidos que define un conjunto de principios y restricciones para el diseño de servicios web (Fielding, 2000).

Una API RESTful expone recursos a través de URLs, utiliza los métodos HTTP estándar (GET, POST, PUT, DELETE) y devuelve respuestas en formato JSON o XML, lo que facilita la comunicación entre el frontend y el backend de manera desacoplada. En el proyecto, la arquitectura REST permite que el frontend en HTML + JavaScript consuma los servicios del backend en Java de forma eficiente y que, en el futuro, se puedan integrar aplicaciones móviles u otros sistemas externos sin necesidad de reescribir la lógica de negocio. Esta separación de responsabilidades también mejora la mantenibilidad y la escalabilidad del sistema.

**Fundamento 10: Control de Acceso Basado en Roles (RBAC)**

El Control de Acceso Basado en Roles (RBAC, por sus siglas en inglés) es un modelo de seguridad que restringe el acceso a los recursos de un sistema según el rol que desempeña cada usuario dentro de la organización (Ferraiolo et al., 2001).

En lugar de asignar permisos de forma individual a cada usuario, se definen roles (como paciente, médico, recepcionista, administrador o gerente) y a cada rol se le otorgan los permisos correspondientes. Este modelo simplifica la gestión de accesos, reduce el riesgo de accesos no autorizados a información sensible y facilita el cumplimiento de normativas de protección de datos como la Ley N° 29733 del Perú. En un sistema médico, donde la confidencialidad de la historia clínica es un requisito legal y ético, el RBAC es un componente fundamental de la arquitectura de seguridad.

**CAPÍTULO 3**

**1\. Documentación de Negocio usando BPMN**

En este capítulo vamos a documentar los procesos de negocio del centro médico Trinidad usando BPMN (Business Process Model and Notation), que es el estándar más usado para modelar procesos. La idea es entender bien cómo trabaja la empresa hoy, encontrar dónde están los problemas, y proponer una manera mejor de hacer las cosas con el sistema que vamos a desarrollar.

Para llegar a esto, primero identificamos qué procesos tiene la empresa y los clasificamos según su tipo. Después, para los más importantes, hicimos las fichas con sus objetivos, actores y reglas. Luego dibujamos cómo se hacen actualmente (AS-IS), los analizamos para ver qué cuellos de botella tienen, y por último propusimos cómo deberían quedar (TO-BE) una vez que el sistema esté funcionando.

**1.a. Identificación de procesos principales, de soporte y estratégicos**

Los procesos de una empresa se suelen agrupar en tres tipos: los estratégicos, que son los que dirigen y guían a la organización; los principales (también llamados misionales), que son los que realmente generan valor para el cliente —en este caso, el paciente—; y los de soporte, que no se ven directamente, pero son necesarios para que todo lo demás funcione.

Aplicando esta clasificación a Trinidad, armamos el siguiente mapa de procesos:

_Figura 3.1. Mapa de Procesos de Trinidad & Especialidades Médicas S.A.C._

En la siguiente tabla detallamos cada proceso identificado, con su código, nombre, tipo, área responsable y qué tan crítico es para la operación del centro médico:

_Tabla 3.1. Inventario de procesos clasificados._

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Código** | **Proceso** | **Tipo** | **Área Responsable** | **Criticidad** |
| PE-01 | Planificación estratégica institucional | Estratégico | Dirección General | Alta |
| PE-02 | Gestión de calidad y mejora continua | Estratégico | Dirección Médica / Calidad | Alta |
| PE-03 | Marketing y comunicación institucional | Estratégico | Administración | Media |
| PP-01 | Admisión y registro de pacientes | Principal | Recepción / Admisión | Alta |
| PP-02 | Gestión de citas médicas | Principal | Recepción / Admisión | Alta |
| PP-03 | Atención en consulta externa | Principal | Consultorios externos | Alta |
| PP-04 | Atención de emergencias | Principal | Emergencia 24h | Crítica |
| PP-05 | Hospitalización / Internamiento | Principal | Servicio de Hospitalización | Alta |
| PP-06 | Diagnóstico por imágenes | Principal | Imagenología | Alta |
| PP-07 | Laboratorio clínico | Principal | Laboratorio | Alta |
| PP-08 | Cirugías y procedimientos quirúrgicos | Principal | Centro quirúrgico | Crítica |
| PS-01 | Gestión de historias clínicas | Soporte | Archivo clínico | Alta |
| PS-02 | Gestión de farmacia y medicamentos | Soporte | Farmacia | Alta |
| PS-03 | Gestión de banco de sangre | Soporte | Banco de sangre | Alta |
| PS-04 | Facturación y cobranzas | Soporte | Caja / Administración | Alta |
| PS-05 | Gestión de recursos humanos | Soporte | RR. HH. | Media |
| PS-06 | Logística y abastecimiento | Soporte | Logística | Media |
| PS-07 | Gestión de nutrición y dietética | Soporte | Nutrición | Media |
| PS-08 | Gestión de TI y sistemas de información | Soporte | TI  | Alta |

Como nuestro proyecto se centra en la consulta externa, vamos a profundizar más en tres procesos: PP-01 (Admisión y registro de pacientes), PP-02 (Gestión de citas médicas) y PP-03 (Atención en consulta externa). Son los procesos donde el sistema va a impactar directamente y también donde se concentran la mayoría de los problemas que mencionamos en el capítulo 1.

**1.b. Objetivos, descripción, actores y reglas de negocio**

A continuación, armamos las fichas de los tres procesos críticos. Cada ficha tiene el objetivo del proceso, una descripción corta de lo que hace, quiénes participan (los actores) y las reglas de negocio que lo rigen.

**Proceso PP-01: Admisión y Registro de Pacientes**

**Objetivo:**

Registrar de forma rápida y sin duplicados a cada paciente que entra al centro médico, asegurándonos de que sus datos estén completos, que su historia clínica esté disponible y que se le dirija al servicio que necesita, sin que tenga que esperar mucho en la recepción.

**Descripción:**

Es básicamente lo primero que pasa cuando un paciente llega a la clínica. Se verifica su identidad, se busca si ya tiene ficha (o se crea una nueva), se registran sus datos, se valida la cita previa o se le agenda una en el momento, se cobra la consulta y se le entrega su ticket con el número de consultorio y el orden en que lo van a atender.

**Actores principales:**

_Tabla 3.2. Actores del proceso PP-01._

|     |     |     |
| --- | --- | --- |
| **Actor** | **Rol en el proceso** | **Tipo** |
| Paciente | Pide la atención y entrega sus datos personales | Externo |
| Recepcionista / Admisionista | Hace el registro y la validación | Interno |
| Cajero | Cobra la consulta | Interno |
| Personal de archivo | Va a buscar la historia clínica física | Interno |
| Sistema (TO-BE) | Automatiza la validación, cobro y derivación | Interno |

**Reglas de negocio (RN):**

• RN-01: Todo paciente tiene que estar registrado con su DNI antes de ser atendido. La única excepción son las emergencias críticas.

• RN-02: No puede haber dos registros activos para el mismo DNI. Si aparece un duplicado, el sistema debe consolidarlos.

• RN-03: La consulta se paga antes de la atención, salvo en emergencias o cuando el paciente tiene un convenio vigente.

• RN-04: Los pacientes menores de edad necesitan venir con un padre, madre o tutor legal que firme la autorización.

• RN-05: La historia clínica del paciente debe estar en el consultorio antes de que el médico empiece la atención.

• RN-06: La atención preferencial debe respetar la Ley N° 28683 (gestantes, adultos mayores y personas con discapacidad).

**Proceso PP-02: Gestión de Citas Médicas**

**Objetivo:**

Coordinar todo lo que tiene que ver con las citas: programarlas, confirmarlas, mandar recordatorios, ejecutarlas y cerrarlas. La idea es aprovechar al máximo la agenda de los médicos especialistas y que el paciente quede satisfecho con el horario y el tiempo de espera.

**Descripción:**

Va desde el momento en que el paciente pide la cita (sea presencial, por teléfono o por la web), pasa por la verificación de la disponibilidad del médico, el bloqueo del horario, la confirmación al paciente, el envío del recordatorio antes del día de la cita, la atención propiamente dicha, y termina con el cierre de la cita en el sistema indicando su estado final: atendida, no asistida, cancelada o reprogramada.

**Actores principales:**

_Tabla 3.3. Actores del proceso PP-02._

|     |     |     |
| --- | --- | --- |
| **Actor** | **Rol en el proceso** | **Tipo** |
| Paciente | Pide, confirma, asiste o reprograma la cita | Externo |
| Recepcionista | Programa la cita y atiende llamadas | Interno |
| Médico especialista | Define su agenda y atiende la cita | Interno |
| Coordinador médico | Aprueba cambios grandes en las agendas | Interno |
| Sistema (TO-BE) | Maneja la disponibilidad y manda las notificaciones | Interno |

**Reglas de negocio (RN):**

• RN-07: Una cita solo se puede asignar en horarios disponibles del médico, sin chocar con otras citas o bloqueos.

• RN-08: El paciente puede cancelar o reprogramar su cita hasta 24 horas antes sin ningún problema.

• RN-09: Cada cita dura por defecto 20 minutos, salvo procedimientos especiales que necesitan más tiempo.

• RN-10: Un paciente no puede tener más de una cita activa con el mismo especialista en 7 días, salvo que sea un control derivado por el médico.

• RN-11: Si el paciente no llega a su cita, el sistema la marca como no asistida 30 minutos después de la hora programada y libera el cupo.

• RN-12: Si un paciente no asiste tres veces seguidas sin avisar, se le restringe la reserva online por 30 días.

**Proceso PP-03: Atención en Consulta Externa**

**Objetivo:**

Brindar una atención médica de calidad al paciente, registrando toda la información que se genera durante la consulta (motivo, anamnesis, examen físico, diagnóstico, tratamiento) en su historia clínica electrónica, y manteniendo siempre la confidencialidad de esos datos.

**Descripción:**

Empieza cuando llaman al paciente al consultorio. El médico hace la entrevista, lo examina, registra los signos vitales, da un diagnóstico y prescribe el tratamiento (que puede incluir medicamentos, exámenes auxiliares, interconsultas o procedimientos). Si el paciente necesita un control, se le programa la siguiente cita y ahí termina el proceso.

**Actores principales:**

_Tabla 3.4. Actores del proceso PP-03._

|     |     |     |
| --- | --- | --- |
| **Actor** | **Rol en el proceso** | **Tipo** |
| Paciente | Recibe la atención | Externo |
| Médico especialista | Atiende y registra todo en la HC | Interno |
| Enfermera / técnica | Apoya en triaje y signos vitales | Interno |
| Sistema (TO-BE) | Provee la HC electrónica y emite recetas/órdenes | Interno |

**Reglas de negocio (RN):**

• RN-13: Toda atención se debe registrar en la historia clínica electrónica el mismo día.

• RN-14: Los diagnósticos se codifican según el CIE-10 (Clasificación Internacional de Enfermedades).

• RN-15: Las recetas tienen que incluir el nombre genérico del medicamento, la dosis, la frecuencia y la duración del tratamiento, según las normas de DIGEMID.

• RN-16: Solo el personal de salud autorizado puede consultar la información clínica del paciente, en cumplimiento con la Ley N° 29733 de Protección de Datos Personales.

• RN-17: Cada atención queda firmada por el médico responsable, con su nombre, número de CMP y la fecha-hora.

• RN-18: Para procedimientos en menores de edad o pacientes con condiciones especiales se necesita un consentimiento informado documentado.

**1.c. Modelado de procesos actuales (AS-IS)**

Los siguientes diagramas BPMN muestran cómo se hacen actualmente los procesos en el centro médico. Para armarlos, fuimos al lugar a observar y conversamos con la gente de recepción, con los médicos y con la parte administrativa. Lo que se ve a primera vista es que casi todo se hace a mano: cuadernos físicos, fichas en papel, llamadas por teléfono… y eso es justamente lo que está generando los problemas.

**Diagrama BPMN AS-IS – Proceso PP-02: Gestión de Citas Médicas**

Hoy, las citas dependen totalmente del horario en que está abierta la recepción y de que la recepcionista anote todo en un cuaderno. La confirmación se hace por teléfono, lo cual no siempre funciona porque a veces no contestan. Los puntos rojos con el símbolo «!» en el diagrama marcan los problemas que detectamos:

_Figura 3.2. Diagrama BPMN AS-IS – Proceso de Gestión de Citas Médicas._

**Diagrama BPMN AS-IS – Proceso PP-01: Admisión y Registro de Pacientes**

En la admisión, el paciente tiene que hacer cola sí o sí. La recepcionista busca a mano en los archivadores físicos para ver si ya tiene ficha. Después hay que pedirle al área de archivo que vaya a buscar la historia clínica, lo que suma más tiempo de espera antes de que el paciente pueda pasar al consultorio:

_Figura 3.3. Diagrama BPMN AS-IS – Proceso de Admisión y Registro de Pacientes._

**Notación BPMN que usamos:**

• Pool y carriles (lanes): muestran los actores y áreas que participan.

• Eventos de inicio (círculo verde) y fin (círculo rojo): marcan dónde empieza y termina el proceso.

• Tareas (rectángulos redondeados): las actividades que hace cada actor.

• Compuertas exclusivas (rombos amarillos): puntos de decisión donde el flujo se bifurca.

• Flechas continuas: el flujo de secuencia entre actividades.

• Flechas punteadas: el flujo de mensajes entre actores (información o documentos).

• Indicadores rojos «!»: marcan los cuellos de botella y problemas que encontramos.

**1.d. Análisis de procesos**

Con los procesos ya modelados en AS-IS, lo siguiente es analizarlos. Lo hicimos en dos partes: primero identificamos los cuellos de botella, las redundancias y los puntos donde se puede mejorar; y después medimos los procesos en términos de tiempo, costo y recursos.

**Cuellos de botella, redundancias y puntos de mejora**

_Tabla 3.5. Cuellos de botella encontrados en los procesos actuales._

|     |     |     |     |
| --- | --- | --- | --- |
| **Código** | **Cuello de botella** | **Proceso afectado** | **Impacto** |
| CB-01 | Recepción saturada en horas pico (8-10 a.m. y 4-6 p.m.) con una sola persona atendiendo presencial y teléfono al mismo tiempo. | PP-01, PP-02 | Alto |
| CB-02 | La búsqueda de la ficha y la HC en archivadores físicos toma entre 5 y 15 minutos por paciente. | PP-01, PS-01 | Alto |
| CB-03 | La disponibilidad de la agenda se consulta en cuadernos físicos y solo la recepcionista la puede ver. No hay visibilidad en tiempo real. | PP-02 | Crítico |
| CB-04 | La confirmación de cita es solo por llamada. Si el paciente no contesta, queda en el aire. | PP-02 | Medio |
| CB-05 | Si la recepción está cerrada (de noche o fines de semana), nadie puede agendar una cita. | PP-02 | Alto |
| CB-06 | Los reportes para gerencia se hacen a mano con cuadernos y hojas de Excel sueltas, y se demoran entre 4 y 8 horas al mes. | PE-02 | Medio |
| CB-07 | El pago de la consulta se hace en una caja aparte, así que el paciente termina haciendo dos colas: una para registrarse y otra para pagar. | PP-01, PS-04 | Medio |

_Tabla 3.6. Redundancias encontradas._

|     |     |     |     |
| --- | --- | --- | --- |
| **Código** | **Redundancia** | **Descripción** | **Cómo lo solucionamos** |
| RD-01 | El paciente da sus datos varias veces | Los datos del paciente se vuelven a ingresar en cada visita: en la ficha de recepción, en la hoja de cálculo y hasta en el cuaderno de farmacia. | Un solo registro centralizado en la BD, búsqueda por DNI. |
| RD-02 | Doble (o triple) validación de identidad | El paciente entrega su DNI en recepción, después en caja, y otra vez en el consultorio para confirmar quién es. | Validación una sola vez al inicio con QR o DNI. |
| RD-03 | La cita se anota dos veces | La cita queda anotada en el cuaderno de recepción y también en la agenda del consultorio del médico. | Una sola agenda electrónica que todos pueden ver. |
| RD-04 | Reportes que no cuadran entre sí | Cada área hace sus propios reportes y a veces los números no coinciden entre departamentos. | Un dashboard único alimentado por la BD del sistema. |

_Tabla 3.7. Puntos de mejora propuestos._

|     |     |     |
| --- | --- | --- |
| **Código** | **Mejora** | **Beneficio esperado** |
| PM-01 | Habilitar un canal web 24/7 para agendar citas | Disponibilidad continua, menos carga en recepción y se ganan pacientes que llegan de noche o en fines de semana. |
| PM-02 | Centralizar el registro de pacientes en una sola BD | Se acaba la duplicidad y todos manejan la misma información. |
| PM-03 | Digitalizar la historia clínica | Acceso inmediato desde cualquier consultorio, todo queda registrado y con respaldo automático. |
| PM-04 | Mandar recordatorios de cita por email/SMS | Esperamos reducir las inasistencias en al menos un 30%. |
| PM-05 | Check-in self-service con kiosco o QR | Reducir el tiempo de espera en admisión a menos de 2 minutos. |
| PM-06 | Dashboard gerencial con KPIs en tiempo real | Decisiones basadas en datos actualizados, ahorrando entre 4 y 8 horas al mes. |
| PM-07 | Pago en línea o integrado al agendar | Se elimina la doble cola y el paciente que ya pagó tiende a no faltar. |
| PM-08 | Roles y permisos diferenciados en el sistema | Cumplimos con la Ley N° 29733 de protección de datos personales. |

**Métricas: tiempo, costo y recursos**

Para tener números más claros, levantamos algunas métricas durante una semana de operación en el centro médico, observando y conversando con el personal. La columna «AS-IS» es la situación actual y la «TO-BE» es lo que esperamos lograr una vez que el sistema esté funcionando:

_Tabla 3.8. Métricas comparativas AS-IS vs. TO-BE._

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Métrica** | **Unidad** | **AS-IS** | **TO-BE (meta)** | **Mejora** |
| Tiempo para agendar una cita (presencial) | minutos | 12-15 | 2-3 | ≈ 80% |
| Tiempo para agendar una cita (telefónica) | minutos | 8-10 | N/A (online) | 100% |
| Tiempo de admisión por paciente | minutos | 10-20 | 2-4 | ≈ 75% |
| Tiempo para encontrar una historia clínica | minutos | 5-15 | < 0.1 (al instante) | ≈ 99% |
| Inasistencias (no-show) | %   | 18-22% | 8-10% | ≈ 55% |
| Registros duplicados de pacientes | %   | 6-9% | < 0.5% | ≈ 95% |
| Tiempo para generar reportes mensuales | horas | 4-8 | < 0.5 | ≈ 90% |
| Citas agendadas fuera del horario de recepción | %   | 0%  | 30-40% | Canal nuevo |
| Costo mensual de papel y archivo físico | S/. | 350-450 | 50-80 | ≈ 80% |
| Personal dedicado solo a recepción/citas | personas | 2   | 1 (apoyo) | 50% |
| Satisfacción del paciente (encuesta) | /5  | 3.2 | ≥ 4.3 | +34% |
| Reportes mensuales disponibles | cantidad | 3   | 15+ | x5  |

Estos números los estimamos a partir de lo que vimos en el centro, lo que nos contó la parte administrativa, y también tomando como referencia los antecedentes que revisamos en el capítulo 2 (Gómez & López, 2022; Reategui & Pinchi, 2021), donde proyectos parecidos lograron reducciones del 60-70% en tiempos de espera y errores de programación. Cuando el sistema entre en producción vamos a poder validar estas cifras y ajustarlas según los datos reales.

**Análisis funcional: Diagrama de Casos de Uso**

Como complemento al análisis BPMN, armamos también un diagrama de casos de uso UML para ver quiénes van a interactuar con el sistema y qué funcionalidades necesita cada uno. Esto nos sirve después para diseñar la arquitectura y decidir qué módulos hay que priorizar:

_Figura 3.4. Diagrama de Casos de Uso del Sistema de Gestión de Citas Médicas._

**Resumen de actores y casos de uso:**

_Tabla 3.9. Actores del sistema y sus principales casos de uso._

|     |     |
| --- | --- |
| **Actor** | **Casos de uso principales** |
| Paciente | Registrarse / iniciar sesión, agendar cita médica, cancelar o reprogramar, consultar historial, ver resultados de exámenes. |
| Recepcionista | Registrar paciente presencial, hacer check-in, procesar cobro, buscar paciente, atender excepciones. |
| Médico especialista | Ver agenda diaria, atender consulta, registrar diagnóstico, emitir receta, solicitar exámenes auxiliares. |
| Administrador del sistema | Gestionar usuarios y roles, gestionar especialidades y médicos, configurar horarios, auditar el sistema. |
| Gerente / Director | Ver dashboard de KPIs, generar reportes gerenciales (productividad, ingresos, ocupación). |

**1.e. Definición de procesos futuros (TO-BE)**

Con todo lo analizado, ahora rediseñamos los procesos principales metiendo al sistema como un actor más dentro del flujo. La idea es que los procesos TO-BE eliminen los cuellos de botella, automaticen lo repetitivo, le den al paciente un canal digital y le pasen información en tiempo real a la gerencia. Veamos los dos procesos críticos rediseñados:

**Diagrama BPMN TO-BE – Proceso PP-02: Gestión de Citas Médicas**

Acá el sistema web pasa a ser el que coordina todo: el paciente entra al portal a cualquier hora, ve la disponibilidad real, elige su horario y reserva. Al toque le llega la confirmación por correo y SMS, junto con un código QR para hacer el check-in. El sistema también manda solo un recordatorio 24 horas antes y, el día de la cita, valida la llegada del paciente, abre la historia clínica para el médico y actualiza los indicadores del dashboard:

_Figura 3.5. Diagrama BPMN TO-BE – Proceso de Gestión de Citas Médicas (con sistema web)._

**Diagrama BPMN TO-BE – Proceso PP-01: Admisión y Registro de Pacientes**

En la admisión TO-BE ya no hay cola. El paciente se acerca a un kiosco o tablet, escanea su DNI o el QR que le mandaron por correo, y listo. El sistema lo identifica, verifica que tenga cita, procesa el pago en línea (o le indica que pase por caja si lo prefiere), le asigna el turno y avisa al consultorio del médico. La recepcionista deja de hacer trabajo operativo y pasa a un rol de apoyo: solo interviene en casos especiales:

_Figura 3.6. Diagrama BPMN TO-BE – Proceso de Admisión y Registro (con sistema web)._

**Resumen de los cambios aplicados**

En la siguiente tabla resumimos los cambios principales que se aplican al pasar del AS-IS al TO-BE, indicando qué cuello de botella o redundancia resuelve cada uno:

_Tabla 3.10. Cambios del modelo AS-IS al modelo TO-BE._

|     |     |     |
| --- | --- | --- |
| **AS-IS (cómo es ahora)** | **TO-BE (cómo va a quedar)** | **Resuelve** |
| La cita se pide presencial o por teléfono, en horario de recepción | La cita se pide por el portal web 24/7 desde cualquier dispositivo | CB-01, CB-05, PM-01 |
| La disponibilidad se ve en un cuaderno físico | La disponibilidad se consulta en la BD en tiempo real, todos la pueden ver | CB-03, RD-03, PM-02 |
| Confirmación por llamada, depende de que el paciente conteste | Confirmación automática por email + SMS + código QR | CB-04, PM-04 |
| No hay recordatorio (depende de la memoria del paciente) | Recordatorio automático 24 h antes; esperamos reducir el no-show un 50% | PM-04 |
| Búsqueda manual de la ficha en archivero (5-15 min) | Identificación automática por DNI/QR en menos de 2 segundos | CB-02, RD-01, PM-02 |
| Cola en caja para pagar la consulta | Pago en línea integrado al agendar, o pago contactless en kiosco | CB-07, PM-07 |
| Historia clínica en papel pedida al área de archivo | Historia clínica electrónica disponible al instante para el médico | CB-02, PM-03 |
| Reportes gerenciales hechos a mano (4-8 h al mes) | Dashboard con KPIs en tiempo real, generado automáticamente | CB-06, RD-04, PM-06 |
| Identidad validada 2 o 3 veces durante la visita | Validación una sola vez al inicio (DNI o QR) | RD-02 |
| Sin control de acceso a la información clínica | Roles y permisos diferenciados con auditoría completa | PM-08 |

Con el sistema implementado y los procesos rediseñados, esperamos que Trinidad consiga lo siguiente:

• Bajar el tiempo de admisión y agendamiento alrededor de un 75-80%, mejorando bastante la experiencia del paciente.

• Tener disponible el canal de agendamiento las 24 horas, captando pacientes que hoy se pierden por no encontrar atención fuera del horario de recepción.

• Acabar con la duplicidad de registros y con el riesgo de perder historias clínicas físicas.

• Reducir las inasistencias del 18-22% al 8-10%, lo que se traduce en más productividad para los médicos y más ingresos para el centro.

• Tener trazabilidad completa desde que el paciente agenda hasta que sale de la consulta.

• Información en tiempo real para que la gerencia tome decisiones con datos.

• Cumplir con la Ley N° 30024 (Historias Clínicas Electrónicas) y la Ley N° 29733 (Protección de Datos Personales).

• Posicionar al centro médico como una institución moderna, alineada con los estándares de salud digital del MINSA.

Estos modelos TO-BE son la base sobre la que vamos a construir la arquitectura técnica del sistema en el capítulo 4, donde definiremos los módulos a implementar, el modelo de datos en Oracle y las interfaces que van a hacer realidad cada uno de estos flujos.

**6\. Análisis**

Después de modelar los procesos de negocio y entender bien cómo deben quedar con el sistema, toca traducir todo eso a requerimientos concretos que el equipo de desarrollo pueda implementar. En esta sección listamos los requerimientos funcionales y no funcionales, las historias de usuario, los diagramas del dominio (clases UML y entidad-relación), y al final una matriz de trazabilidad para verificar que cada requisito tenga un origen claro y termine reflejado en el sistema.

**2.a. Requerimientos funcionales**

Los requerimientos funcionales son las cosas que el sistema tiene que hacer, vistas desde el lado del usuario o del negocio. Los agrupamos por módulo para que sea más fácil de leer y de priorizar después en el desarrollo:

_Tabla 3.11. Requerimientos funcionales del sistema._

|     |     |     |     |
| --- | --- | --- | --- |
| **Código** | **Módulo** | **Requerimiento** | **Prioridad** |
| RF-01 | Autenticación | El sistema debe permitir el inicio de sesión con usuario y contraseña, con validación contra la base de datos. | Alta |
| RF-02 | Autenticación | El sistema debe diferenciar el acceso por rol (paciente, recepcionista, médico, administrador, gerente). | Alta |
| RF-03 | Autenticación | El sistema debe permitir la recuperación de contraseña por correo electrónico. | Media |
| RF-04 | Pacientes | El sistema debe permitir registrar nuevos pacientes con sus datos personales (DNI, nombres, fecha de nacimiento, teléfono, etc.). | Alta |
| RF-05 | Pacientes | El sistema debe validar que no existan pacientes duplicados por DNI. | Alta |
| RF-06 | Pacientes | El paciente debe poder actualizar sus datos de contacto desde su portal. | Media |
| RF-07 | Citas | El sistema debe mostrar la disponibilidad de los médicos en tiempo real, filtrando por especialidad y fecha. | Alta |
| RF-08 | Citas | El paciente debe poder agendar una cita desde el portal web 24/7, eligiendo médico, fecha y hora. | Alta |
| RF-09 | Citas | El sistema debe enviar la confirmación de la cita por correo electrónico y SMS, incluyendo un código QR. | Alta |
| RF-10 | Citas | El sistema debe enviar un recordatorio automático 24 horas antes de la cita. | Alta |
| RF-11 | Citas | El paciente debe poder cancelar o reprogramar su cita hasta 24 horas antes sin penalidad. | Alta |
| RF-12 | Citas | El sistema debe marcar automáticamente la cita como 'no asistida' 30 minutos después de la hora programada si no se hizo check-in. | Media |
| RF-13 | Admisión | La recepcionista (o el kiosco) debe poder hacer check-in del paciente al escanear el QR o el DNI. | Alta |
| RF-14 | Admisión | El sistema debe asignar un número de turno y notificar al consultorio del médico. | Alta |
| RF-15 | Pagos | El sistema debe permitir el pago de la consulta en línea (tarjeta) o registrar el cobro en caja. | Alta |
| RF-16 | Pagos | El sistema debe emitir un comprobante de pago electrónico al paciente. | Media |
| RF-17 | Historia Clínica | El médico debe poder consultar la historia clínica electrónica del paciente al iniciar la atención. | Alta |
| RF-18 | Historia Clínica | El médico debe poder registrar la atención: motivo, anamnesis, examen físico, diagnóstico (CIE-10) y tratamiento. | Alta |
| RF-19 | Historia Clínica | El sistema debe permitir emitir recetas médicas digitales con nombre genérico, dosis, frecuencia y duración. | Alta |
| RF-20 | Historia Clínica | El sistema debe permitir solicitar exámenes auxiliares (laboratorio, imágenes) desde la atención. | Media |
| RF-21 | Médicos | El administrador debe poder registrar y gestionar a los médicos con su CMP, especialidad y horarios. | Alta |
| RF-22 | Médicos | El médico debe poder ver su agenda diaria con los pacientes que va a atender. | Alta |
| RF-23 | Médicos | El administrador debe poder configurar los horarios de atención de cada médico por día de la semana. | Alta |
| RF-24 | Especialidades | El administrador debe poder gestionar el catálogo de especialidades y sus precios. | Media |
| RF-25 | Reportes | El gerente debe poder visualizar un dashboard con los KPIs principales (citas atendidas, no-show, ingresos, productividad por médico). | Alta |
| RF-26 | Reportes | El sistema debe generar reportes mensuales de productividad médica, ingresos por especialidad y satisfacción. | Media |
| RF-27 | Reportes | El sistema debe permitir exportar los reportes en formato PDF y Excel. | Media |
| RF-28 | Auditoría | El sistema debe registrar todas las acciones críticas (creación, modificación, eliminación) con usuario y fecha-hora. | Alta |
| RF-29 | Notificaciones | El paciente debe poder consultar sus citas pasadas y futuras desde su portal. | Media |
| RF-30 | Notificaciones | El paciente debe poder ver los resultados de sus exámenes una vez que estén disponibles. | Media |

**2.b. Requerimientos no funcionales**

Los requerimientos no funcionales son las características de calidad que el sistema debe cumplir, como qué tan rápido tiene que responder, qué tan seguro debe ser, en qué dispositivos debe funcionar, etc. No describen lo que el sistema hace, sino cómo lo hace.

_Tabla 3.12. Requerimientos no funcionales del sistema._

|     |     |     |
| --- | --- | --- |
| **Código** | **Categoría** | **Requerimiento** |
| RNF-01 | Rendimiento | El sistema debe responder a las consultas más comunes (login, listar citas, agendar) en menos de 3 segundos en condiciones normales. |
| RNF-02 | Rendimiento | El sistema debe soportar al menos 100 usuarios concurrentes sin degradación notable. |
| RNF-03 | Disponibilidad | El sistema debe estar disponible al menos el 99% del tiempo (excluyendo ventanas de mantenimiento programadas). |
| RNF-04 | Disponibilidad | El portal de pacientes debe estar accesible 24/7, todos los días del año. |
| RNF-05 | Seguridad | Todas las contraseñas deben almacenarse encriptadas usando un algoritmo de hash (BCrypt o similar). |
| RNF-06 | Seguridad | La comunicación entre el cliente y el servidor debe ser por HTTPS (TLS 1.2 o superior). |
| RNF-07 | Seguridad | El sistema debe cumplir con la Ley N° 29733 de Protección de Datos Personales del Perú. |
| RNF-08 | Seguridad | El sistema debe cumplir con la Ley N° 30024 sobre Historias Clínicas Electrónicas. |
| RNF-09 | Seguridad | El sistema debe cerrar la sesión automáticamente después de 15 minutos de inactividad. |
| RNF-10 | Usabilidad | Las interfaces deben ser intuitivas, en español, y manejables por personas sin formación técnica. |
| RNF-11 | Usabilidad | El sistema debe ser responsive: funcionar correctamente en computadoras, tablets y celulares. |
| RNF-12 | Compatibilidad | El sistema debe funcionar en los navegadores modernos: Chrome, Firefox, Edge y Safari (últimas dos versiones). |
| RNF-13 | Mantenibilidad | El código debe seguir buenas prácticas (arquitectura en capas, comentarios, nombres descriptivos) para facilitar el mantenimiento. |
| RNF-14 | Mantenibilidad | El sistema debe registrar logs de errores y eventos importantes para facilitar el diagnóstico de problemas. |
| RNF-15 | Escalabilidad | La arquitectura debe permitir escalar horizontalmente si crece el número de usuarios. |
| RNF-16 | Confiabilidad | El sistema debe hacer respaldos automáticos diarios de la base de datos. |
| RNF-17 | Confiabilidad | Ante una caída del servidor, los datos no pueden perderse; debe existir un mecanismo de recuperación. |
| RNF-18 | Tecnológicos | Backend en Java (Spring Boot o Servlets), frontend con HTML5 + CSS3 + JavaScript, BD en Oracle Database 21c. |
| RNF-19 | Tecnológicos | El servidor de aplicaciones debe ser Apache Tomcat 10 o superior. |
| RNF-20 | Legal | Las recetas digitales deben cumplir con la normativa DIGEMID (nombre genérico, dosis, frecuencia, duración). |

**2.c. Historias de usuario**

Para complementar los requerimientos, escribimos historias de usuario que ayudan a entender cada funcionalidad desde la perspectiva del actor que la va a usar. Cada historia sigue el formato clásico: «Como \[rol\], quiero \[acción\] para \[beneficio\]», y le agregamos sus criterios de aceptación, que son las condiciones que se tienen que cumplir para considerar la historia como completada.

| **HU-01 Agendar cita en línea** |     |
| --- |     | --- |
| **Como** | paciente |
| **Quiero** | poder agendar mi cita médica desde la página web a cualquier hora del día |
| **Para** | no tener que ir al centro o llamar por teléfono. |

| **Criterios de aceptación** |
| --- |
| **1\.** Puedo entrar al portal y ver las especialidades disponibles. |
| **2\.** Al elegir una especialidad, veo la lista de médicos con sus horarios libres. |
| **3\.** Puedo seleccionar fecha y hora, y confirmar la cita en menos de 3 clics. |
| **4\.** Recibo un correo y un SMS con la confirmación y un código QR. |

| **HU-02 Recordatorio automático de cita** |     |
| --- |     | --- |
| **Como** | paciente |
| **Quiero** | recibir un recordatorio automático antes de mi cita |
| **Para** | no olvidarme y poder organizar mi día. |

| **Criterios de aceptación** |
| --- |
| **1\.** Recibo un mensaje 24 horas antes de la cita por correo y SMS. |
| **2\.** El recordatorio incluye el nombre del médico, fecha, hora y consultorio. |
| **3\.** Puedo cancelar o reprogramar desde el mismo recordatorio. |

| **HU-03 Cancelar o reprogramar cita** |     |
| --- |     | --- |
| **Como** | paciente |
| **Quiero** | poder cancelar o reprogramar mi cita desde el portal |
| **Para** | no quedar mal si me sale algo y no puedo asistir. |

| **Criterios de aceptación** |
| --- |
| **1\.** Veo el listado de mis citas próximas en mi perfil. |
| **2\.** Puedo cancelar o reprogramar hasta 24 horas antes sin penalidad. |
| **3\.** Si cancelo, el cupo queda libre para otros pacientes. |
| **4\.** Recibo confirmación de la cancelación o el cambio. |

| **HU-04 Check-in rápido en admisión** |     |
| --- |     | --- |
| **Como** | paciente |
| **Quiero** | hacer mi check-in escaneando un QR o mi DNI en un kiosco |
| **Para** | no tener que hacer cola en recepción. |

| **Criterios de aceptación** |
| --- |
| **1\.** Acerco el QR o el DNI al lector y el sistema me identifica. |
| **2\.** El sistema me asigna un número de turno y me indica el consultorio. |
| **3\.** Si no tengo cita, el sistema me deriva a recepción. |

| **HU-05 Ver agenda diaria del médico** |     |
| --- |     | --- |
| **Como** | médico |
| **Quiero** | ver mi agenda del día con los pacientes que voy a atender |
| **Para** | preparar las atenciones y revisar la HC con anticipación. |

| **Criterios de aceptación** |
| --- |
| **1\.** Al iniciar sesión, veo de inmediato la lista de pacientes del día. |
| **2\.** Cada cita muestra el nombre del paciente, la hora y el motivo. |
| **3\.** Puedo hacer clic en un paciente para ver su HC antes de atenderlo. |

| **HU-06 Registrar atención y emitir receta** |     |
| --- |     | --- |
| **Como** | médico |
| **Quiero** | registrar la atención en la HC electrónica y emitir la receta digital |
| **Para** | que todo quede documentado y el paciente no salga del consultorio con papeles sueltos. |

| **Criterios de aceptación** |
| --- |
| **1\.** Puedo registrar el motivo, anamnesis, examen físico, diagnóstico (CIE-10) y tratamiento. |
| **2\.** Puedo emitir una receta con uno o más medicamentos, dosis y frecuencia. |
| **3\.** La atención queda firmada con mi usuario y la fecha-hora. |
| **4\.** El paciente puede ver y descargar su receta desde su portal. |

| **HU-07 Dashboard gerencial** |     |
| --- |     | --- |
| **Como** | gerente |
| **Quiero** | ver un dashboard con los indicadores clave del centro |
| **Para** | tomar decisiones basadas en datos reales y no esperar a fin de mes. |

| **Criterios de aceptación** |
| --- |
| **1\.** Veo los indicadores de citas atendidas, no-show, ingresos y productividad. |
| **2\.** Los datos se actualizan en tiempo real (o casi). |
| **3\.** Puedo filtrar por especialidad, médico, fecha o canal de reserva. |
| **4\.** Puedo exportar los reportes a PDF o Excel. |

| **HU-08 Gestionar usuarios y permisos** |     |
| --- |     | --- |
| **Como** | administrador |
| **Quiero** | gestionar los usuarios y sus permisos |
| **Para** | asegurarme de que cada quien tenga acceso solo a lo que necesita. |

| **Criterios de aceptación** |
| --- |
| **1\.** Puedo crear, editar y desactivar usuarios. |
| **2\.** Puedo asignar uno o varios roles a cada usuario. |
| **3\.** Las contraseñas se guardan encriptadas. |
| **4\.** Hay un log que registra los cambios hechos por el administrador. |

En total identificamos varias historias adicionales (registrar paciente, gestionar horarios médicos, ver historial de pagos, etc.), que se irán afinando durante las iteraciones de desarrollo. Las historias acá listadas son las más representativas y las que cubren los flujos principales del sistema.

**2.d. Diagramas del dominio**

Para representar las entidades del sistema y cómo se relacionan entre sí, armamos dos diagramas complementarios: un diagrama de clases UML, que muestra la estructura orientada a objetos del sistema (clases, atributos, métodos y relaciones), y un diagrama entidad-relación, que es la base para el diseño de la base de datos en Oracle.

**Diagrama de Clases UML**

El diagrama de clases muestra las clases principales del sistema, sus atributos, métodos y las relaciones entre ellas. Las cuatro clases que heredan de la clase abstracta Usuario (Paciente, Médico, Recepcionista y Administrador) comparten los atributos comunes de autenticación pero cada una tiene sus propias responsabilidades:

_Figura 3.7. Diagrama de Clases UML del sistema._

**Algunas relaciones importantes a destacar:**

• Un Paciente tiene 0 o muchas Citas (relación de asociación 1 a N).

• Un Médico atiende 0 o muchas Citas y pertenece a una Especialidad.

• Una Cita genera una Atención (relación 1 a 1, opcional si la cita no se concreta).

• Una HistoriaClinica está compuesta por muchas Atenciones (composición: si se elimina la HC, las atenciones también).

• Una Atención puede emitir varias Recetas y solicitar exámenes auxiliares.

• Cada Cita tiene asociado un Pago (puede ser nulo si la cita aún no se ha pagado).

**Diagrama Entidad-Relación**

El siguiente diagrama entidad-relación es la representación lógica de la base de datos que vamos a implementar en Oracle. Cada entidad se convertirá en una tabla, los atributos en columnas, y las relaciones en llaves foráneas con sus respectivas restricciones de integridad referencial:

_Figura 3.8. Diagrama Entidad-Relación del sistema._

**Notación utilizada (pata de gallo o crow's foot):**

• Las llaves primarias están marcadas con PK y las foráneas con FK.

• UQ indica restricciones de unicidad (no se permiten valores duplicados).

• Una línea con una marca «1» y otra con «pata de gallo» significa relación uno a muchos.

• Dos marcas «1» en ambos extremos significan relación uno a uno.

El modelo final implementado en Oracle Database 21c XE consta de 16 tablas, organizadas en cinco grupos funcionales:

a) Seguridad y acceso: USUARIO, ROL y la tabla intermedia USUARIO_ROL. Esta última implementa una relación de muchos a muchos entre usuarios y roles, lo que permite asignar uno o varios roles a un mismo usuario (por ejemplo, un médico que también actúa como administrador). USUARIO almacena las credenciales y el control de bloqueo (intentos_fallidos, bloqueado, fecha_ult_login), mientras que ROL define los perfiles del sistema (paciente, recepcionista, médico, administrador, gerente).

b) Personas y catálogos: PACIENTE, MEDICO, ESPECIALIDAD y HORARIO_MEDICO. PACIENTE se vincula a USUARIO de forma opcional (0..1) porque no todo paciente registrado en admisión necesita credenciales del portal web. MEDICO, en cambio, requiere obligatoriamente un USUARIO (1:1) para poder iniciar sesión y atender. Cada MEDICO pertenece a una ESPECIALIDAD (1:N) y tiene uno o varios bloques en HORARIO_MEDICO (1:N), que define la agenda semanal por día de la semana (1-7) y rango horario.

c) Operación clínica: HISTORIA_CLINICA, CITA y ATENCION. Cada PACIENTE tiene exactamente una HISTORIA_CLINICA (1:1) que se crea automáticamente al primer registro. CITA agrupa las llaves foráneas a paciente, médico y especialidad, junto con datos operativos (fecha_cita, hora_inicio, hora_fin, estado, canal_reserva, codigo_qr, numero_turno, fecha_checkin) y respeta una restricción de unicidad por médico-fecha-hora_inicio para impedir solapamientos. ATENCION es la consulta efectivamente realizada: tiene relación 1:1 con CITA (UQ sobre id_cita), referencia a la HISTORIA_CLINICA y al MEDICO, y registra anamnesis, examen físico y tratamiento como CLOB, además de signos vitales (presion_arterial, frecuencia_cardiaca, temperatura, peso_kg, talla_cm, saturacion_o2). El campo firmado_por (FK a USUARIO) y fecha_firma documentan la firma electrónica del responsable.

d) Prescripción, exámenes y catálogo CIE-10: RECETA, DETALLE_RECETA, ORDEN_EXAMEN y DIAGNOSTICO_CIE10. Cada ATENCION puede generar 0 o más RECETAS (1:N), y cada RECETA contiene 1 o más medicamentos en DETALLE_RECETA (1:N) con los campos exigidos por la normativa DIGEMID: nombre_generico, nombre_comercial, presentacion, dosis, frecuencia, duracion_dias, via_administracion e indicaciones. Las órdenes de laboratorio o imágenes se modelan en ORDEN_EXAMEN (1:N respecto de ATENCION). DIAGNOSTICO_CIE10 es un catálogo maestro con los códigos de la Clasificación Internacional de Enfermedades, referenciado desde ATENCION (FK diagnostico_cie10) para garantizar el cumplimiento de la regla RN-14.

e) Cobranza y trazabilidad: PAGO y AUDITORIA_LOG. PAGO se asocia a CITA (1:N) y soporta varios métodos (EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA) con sus estados (PENDIENTE, PAGADO, ANULADO, REEMBOLSADO) y datos del comprobante electrónico. AUDITORIA_LOG es la bitácora del sistema: registra acciones (LOGIN, INSERT, UPDATE, DELETE), entidad afectada, detalle, IP de origen y usuario, dando soporte al requerimiento RF-28 y al cumplimiento de la Ley N° 29733 de Protección de Datos Personales.

Adicionalmente, el esquema implementa secuencias (SEQ_\*) y triggers BEFORE INSERT (TRG_\*\_BI) para autogenerar las llaves primarias numéricas, así como restricciones CHECK para validar dominios (sexo M/F, DNI de 8 dígitos numéricos, estado de cita en el conjunto definido, métodos de pago permitidos, etc.) y restricciones UNIQUE para campos de negocio críticos (DNI, CMP, nro_historia, nro_receta, username y email). Las eliminaciones en cascada (ON DELETE CASCADE) se aplican únicamente donde la dependencia es de composición fuerte (HORARIO_MEDICO → MEDICO, HISTORIA_CLINICA → PACIENTE, DETALLE_RECETA → RECETA, ORDEN_EXAMEN → ATENCION, RECETA → ATENCION, USUARIO_ROL → USUARIO).

**2.e. Matriz de trazabilidad de requisitos**

La matriz de trazabilidad nos sirve para asegurarnos de que cada requerimiento que definimos viene de algún lado (un objetivo del proyecto, una regla de negocio, un cuello de botella detectado, una historia de usuario) y que se va a implementar en algún módulo concreto del sistema. Esto nos ayuda a no olvidar ningún requisito y también a justificar cada uno de los que están listados.

_Tabla 3.13. Matriz de trazabilidad de requerimientos funcionales._

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Req.** | **Origen / justificación** | **Historia de usuario** | **Caso de uso / módulo** | **Verificación** |
| RF-01 | RNF-05, OE-todos | Todas las HU | Autenticarse | Pruebas de login |
| RF-02 | RNF-07, RN-16 | Todas las HU | Autenticarse | Pruebas por rol |
| RF-03 | Usabilidad | —   | Autenticarse | Pruebas de recuperación |
| RF-04 | OE-1, RN-01 | —   | Registrar paciente | Pruebas de registro |
| RF-05 | RD-01, RN-02 | —   | Registrar paciente | Pruebas de unicidad DNI |
| RF-06 | Usabilidad | —   | Consultar historial | Pruebas de edición |
| RF-07 | CB-03, PM-02 | HU-01 | Agendar cita médica | Pruebas de agenda |
| RF-08 | OE-2, CB-05, PM-01 | HU-01 | Agendar cita médica | Pruebas de reserva online |
| RF-09 | CB-04, PM-04 | HU-01 | Agendar cita médica | Verificación de email/SMS |
| RF-10 | PM-04 | HU-02 | Agendar cita médica | Job de recordatorios |
| RF-11 | RN-08 | HU-03 | Cancelar/reprogramar cita | Pruebas de cancelación |
| RF-12 | RN-11 | —   | Agendar cita médica | Job de no-show |
| RF-13 | CB-02, PM-05 | HU-04 | Realizar check-in | Pruebas de QR/DNI |
| RF-14 | PM-05 | HU-04 | Realizar check-in | Pruebas de turnos |
| RF-15 | CB-07, PM-07 | —   | Procesar cobro | Pruebas de pago |
| RF-16 | Legal (SUNAT) | —   | Procesar cobro | Validación comprobante |
| RF-17 | OE-3, CB-02, PM-03 | HU-05 | Atender consulta | Pruebas de acceso a HC |
| RF-18 | OE-3, RN-13, RN-14 | HU-06 | Registrar diagnóstico | Pruebas de registro HC |
| RF-19 | RN-15, RNF-20 | HU-06 | Emitir receta médica | Pruebas DIGEMID |
| RF-20 | OE-3 | —   | Solicitar exámenes | Pruebas de órdenes |
| RF-21 | OE-4 | HU-08 | Gestionar usuarios | Pruebas de CRUD médicos |
| RF-22 | OE-4 | HU-05 | Ver agenda diaria | Pruebas de agenda médico |
| RF-23 | OE-4, RN-07 | —   | Configurar horarios | Pruebas de horarios |
| RF-24 | OE-4 | —   | Gestionar especialidades | Pruebas de catálogo |
| RF-25 | OE-5, CB-06, PM-06 | HU-07 | Ver dashboard KPI | Verificación de KPIs |
| RF-26 | OE-5 | HU-07 | Generar reportes gerenciales | Verificación de reportes |
| RF-27 | Usabilidad | HU-07 | Generar reportes gerenciales | Pruebas de exportación |
| RF-28 | RNF-07, PM-08 | HU-08 | Auditar sistema | Pruebas de logs |
| RF-29 | Usabilidad | —   | Consultar historial | Pruebas de visualización |
| RF-30 | OE-3, Usabilidad | —   | Ver resultados | Pruebas de descarga |

Como se puede ver en la matriz, todos los requerimientos funcionales tienen al menos una fuente identificable: ya sea un objetivo específico del proyecto (OE-1 a OE-5 del capítulo 1), una regla de negocio (RN-01 a RN-18), un cuello de botella detectado (CB-01 a CB-07), un punto de mejora (PM-01 a PM-08), o una historia de usuario (HU-01 a HU-08). De la misma forma, cada requerimiento está vinculado a un caso de uso concreto (de los identificados en el diagrama de la Figura 3.4) y tiene definida la forma en que se va a verificar su cumplimiento.

Esta trazabilidad nos será de mucha utilidad en las siguientes fases del proyecto, porque nos permite confirmar al final del desarrollo que cada problema detectado en el análisis tiene una solución implementada, y también justificar ante la empresa cliente por qué cada funcionalidad está presente en el sistema.

**CAPÍTULO 4:**

**1\. Diseño de Arquitectura del Sistema**

**1.1 Descripción General**

El sistema Trinidad Citas Médicas sigue el patrón arquitectónico de capas (Layered Architecture) sobre el framework Spring Boot 3.2 con Java 17. Esta arquitectura separa claramente las responsabilidades del sistema en cuatro capas principales, favoreciendo la mantenibilidad, testeabilidad y escalabilidad del software.

**1.2 Capas de la Arquitectura**

|     |     |     |
| --- | --- | --- |
| **Capa** | **Componente** | **Responsabilidad** |
| Presentación | Controller (Web + API REST) | Recibe peticiones HTTP, retorna vistas HTML o JSON |
| Lógica de Negocio | Service | Aplica reglas de negocio, orquesta operaciones |
| Acceso a Datos | Repository (JPA) | Consultas a la base de datos mediante Hibernate |
| Dominio | Model / Entity | Representa las tablas de la base de datos |
| Transporte | DTO | Objetos de transferencia entre capas |
| Seguridad | Spring Security + JWT | Autenticación, autorización, filtros de acceso |

**1.3 Flujo General de una Petición**

1.  El usuario interactúa con la interfaz web (Thymeleaf) o una aplicación externa (API REST).
2.  El Controller recibe la petición HTTP y valida los datos de entrada.
3.  El Controller delega la lógica al Service correspondiente.
4.  El Service aplica las reglas de negocio y llama al Repository.
5.  El Repository ejecuta las consultas JPA contra la base de datos H2 (dev) u Oracle XE (producción).
6.  El resultado sube por las capas hasta retornar al usuario como HTML o JSON.

**4.1.4 Tecnologías Utilizadas**

|     |     |     |
| --- | --- | --- |
| **Tecnología** | **Versión** | **Rol en el Sistema** |
| Java | 17 LTS | Lenguaje de programación principal |
| Spring Boot | 3.2.5 | Framework base, autoconfiguración, servidor embebido |
| Spring Security | 6.x | Autenticación, autorización, filtros JWT |
| Hibernate / JPA | 6.4.4 | ORM, mapeo objeto-relacional |
| Thymeleaf | 3.x | Motor de plantillas para vistas HTML |
| H2 Database | 2.x | Base de datos en memoria para desarrollo |
| Oracle XE | 21c | Base de datos para producción |
| Maven | 3.9 | Gestión de dependencias y construcción |
| Lombok | 1.18.x | Reducción de código boilerplate |
| JWT (JJWT) | 0.11.x | Tokens de autenticación para la API REST |

**4.1.5 Patrones de Diseño Aplicados**

- **MVC (Model-View-Controller):** separación entre datos, lógica y presentación.
- **Repository Pattern:** abstracción del acceso a datos mediante interfaces JPA.
- **DTO Pattern:** objetos de transferencia para evitar exponer entidades directamente.
- **Builder Pattern:** construcción de entidades complejas usando Lombok @Builder.
- **Filter Pattern:** filtro JWT que intercepta peticiones antes del procesamiento.
- **Singleton:** beans de Spring administrados por el contenedor IoC.

**4.2 Diseño Detallado de Componentes**

**4.2.1 Módulos del Sistema**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Módulo** | **Controller Web** | **Controller API** | **Service** | **Repository** |
| Autenticación | HomeController | AuthRestController | AuthService | UsuarioRepository |
| Usuarios/Roles | UsuarioWebController | UsuarioRestController | UsuarioService | UsuarioRepository |
| Pacientes | PacienteWebController | PacienteRestController | PacienteService | PacienteRepository |
| Médicos | MedicoWebController | MedicoRestController | MedicoService | MedicoRepository |
| Especialidades | EspecialidadWebController | EspecialidadRestController | EspecialidadService | EspecialidadRepository |
| Horarios | HorarioWebController | HorarioMedicoRestController | HorarioService | HorarioMedicoRepository |
| Citas | CitaWebController | CitaRestController | CitaService | CitaRepository |
| Atenciones | AtencionWebController | AtencionRestController | AtencionService | AtencionRepository |
| Recetas | RecetaWebController | RecetaRestController | RecetaService | RecetaRepository |
| Historia Clínica | HistoriaClinicaWebController | HistoriaClinicaRestController | HistoriaClinicaService | HistoriaClinicaRepository |
| Pagos | PagoWebController | PagoRestController | PagoService | PagoRepository |
| Reportes | ReportesWebController | ReporteRestController | ReporteService | —   |
| Auditoría | AuditoriaWebController | AuditoriaLogRestController | AuditoriaService | AuditoriaLogRepository |

**4.2.2 Descripción de Clases Principales**

**Clase: Cita**

Entidad central del sistema. Representa la reserva de una consulta médica. Contiene la relación entre un Paciente, un Médico y una Especialidad en una fecha y hora determinada. Incluye un código QR único para el check-in, un número de turno y el seguimiento del estado a lo largo del ciclo de vida de la atención.

|     |     |     |     |
| --- | --- | --- | --- |
| **Atributo** | **Tipo** | **Descripción** | **Restricciones** |
| idCita | Long | Identificador único | PK, autoincremental |
| paciente | Paciente | Paciente de la cita | FK, NOT NULL |
| medico | Medico | Médico asignado | FK, NOT NULL |
| especialidad | Especialidad | Especialidad médica | FK, NOT NULL |
| fechaCita | LocalDate | Fecha de la consulta | NOT NULL, fecha futura |
| horaInicio | String | Hora de inicio (HH:mm) | NOT NULL |
| horaFin | String | Hora de fin calculada | NOT NULL |
| estado | EstadoCita | Estado actual | PROGRAMADA por defecto |
| canalReserva | String | WEB / APP / PRESENCIAL | NOT NULL |
| codigoQr | String | Código QR para check-in | Único |
| numeroTurno | Integer | Número de turno del día | —   |
| motivoConsulta | String | Motivo de la visita | Max 300 chars |

**Estados del Ciclo de Vida de una Cita**

|     |     |     |
| --- | --- | --- |
| **Estado** | **Descripción** | **Transición Siguiente** |
| PROGRAMADA | Cita registrada, pendiente de confirmación | CONFIRMADA / CANCELADA |
| CONFIRMADA | Paciente confirmó asistencia | EN_ATENCION / NO_ASISTIO |
| EN_ATENCION | Paciente en consultorio | ATENDIDA |
| ATENDIDA | Consulta completada exitosamente | Estado final |
| CANCELADA | Cita cancelada por paciente o médico | Estado final |
| NO_ASISTIO | Paciente no se presentó | Estado final |
| REPROGRAMADA | Cita movida a otra fecha/hora | PROGRAMADA (nueva) |

**Clase: Usuario**

Gestiona el acceso al sistema. Implementa seguridad con contraseñas encriptadas en BCrypt, control de intentos fallidos y bloqueo automático de cuenta.

|     |     |     |
| --- | --- | --- |
| **Atributo** | **Tipo** | **Descripción** |
| username | String | Nombre de usuario único (max 50 chars) |
| passwordHash | String | Contraseña encriptada con BCrypt (factor 10) |
| email | String | Correo electrónico único |
| activo | Integer | 1 = activo, 0 = inactivo |
| intentosFallidos | Integer | Contador de intentos de login fallidos |
| bloqueado | Integer | 1 = cuenta bloqueada |
| roles | Set&lt;Rol&gt; | Roles asignados (relación N:N con USUARIO_ROL) |

**2.3 Mapa de Navegación**

|     |     |     |
| --- | --- | --- |
| **Módulo** | **URL** | **Roles con Acceso** |
| Login | /login | Público |
| Dashboard | /dashboard | Todos los roles autenticados |
| Citas | /citas | ADMINISTRADOR, RECEPCIONISTA, MÉDICO |
| Pacientes | /pacientes | ADMINISTRADOR, RECEPCIONISTA, MÉDICO |
| Médicos | /medicos | ADMINISTRADOR, RECEPCIONISTA |
| Especialidades | /especialidades | ADMINISTRADOR |
| Horarios | /horarios | ADMINISTRADOR |
| Atenciones | /atenciones | ADMINISTRADOR, MÉDICO |
| Historia Clínica | /historia-clinica | ADMINISTRADOR, MÉDICO |
| Recetas | /recetas | MÉDICO, ADMINISTRADOR |
| Órdenes de Examen | /ordenes-examen | MÉDICO, ADMINISTRADOR |
| Pagos | /pagos | ADMINISTRADOR, RECEPCIONISTA |
| Reportes | /reportes | ADMINISTRADOR, GERENTE |
| Auditoría | /auditoria | ADMINISTRADOR, GERENTE |
| Usuarios | /usuarios | ADMINISTRADOR |

**3 Diseño de Base de Datos**

**3.1 Modelo Lógico de Datos**

El modelo lógico define las entidades del dominio, sus atributos y las relaciones entre ellas, independientemente del motor de base de datos. El sistema gestiona 16 entidades principales.

**Relaciones principales:**

- **USUARIO — ROL:** relación N:N mediante tabla USUARIO_ROL. Un usuario puede tener múltiples roles.
- **USUARIO — PACIENTE:** relación 1:1 opcional. Un paciente puede tener cuenta de acceso.
- **USUARIO — MEDICO:** relación 1:1 obligatoria. Todo médico tiene cuenta de acceso.
- **MEDICO — ESPECIALIDAD:** relación N:1. Un médico pertenece a una especialidad.
- **MEDICO — HORARIO_MEDICO:** relación 1:N. Un médico tiene múltiples horarios de atención.
- **PACIENTE — HISTORIA_CLINICA:** relación 1:1. Cada paciente tiene exactamente una historia clínica.
- **CITA — PACIENTE, MEDICO, ESPECIALIDAD:** relación N:1 con cada uno.
- **CITA — PAGO:** relación 1:N. Una cita puede tener múltiples registros de pago.
- **CITA — ATENCION:** relación 1:1. Una cita genera a lo sumo una atención médica.
- **ATENCION — RECETA:** relación 1:N. Una atención puede generar múltiples recetas.
- **RECETA — DETALLE_RECETA:** relación 1:N. Una receta contiene múltiples medicamentos.
- **ATENCION — ORDEN_EXAMEN:** relación 1:N. Una atención puede ordenar múltiples exámenes.

**4.3.2 Modelo Físico de Datos**

**Tabla: USUARIO**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Columna** | **Tipo** | **Nulo** | **Restricciones** | **Descripción** |
| ID_USUARIO | BIGINT | NO  | PK, SEQ_USUARIO | Identificador único |
| USERNAME | VARCHAR(50) | NO  | UNIQUE, NOT NULL | Nombre de usuario |
| PASSWORD_HASH | VARCHAR(255) | NO  | NOT NULL | Hash BCrypt |
| EMAIL | VARCHAR(120) | NO  | UNIQUE, NOT NULL | Correo electrónico |
| ACTIVO | INT | NO  | DEFAULT 1, CHECK(0,1) | Estado activo/inactivo |
| INTENTOS_FALLIDOS | INT | NO  | DEFAULT 0 | Intentos fallidos de login |
| BLOQUEADO | INT | NO  | DEFAULT 0, CHECK(0,1) | Cuenta bloqueada |
| FECHA_CREACION | TIMESTAMP | NO  | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| FECHA_ULT_LOGIN | TIMESTAMP | SÍ  | —   | Último acceso |

**Tabla: PACIENTE**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Columna** | **Tipo** | **Nulo** | **Restricciones** | **Descripción** |
| ID_PACIENTE | BIGINT | NO  | PK, SEQ_PACIENTE | Identificador único |
| ID_USUARIO | BIGINT | SÍ  | FK → USUARIO | Cuenta de acceso (opcional) |
| DNI | VARCHAR(8) | NO  | UNIQUE, NOT NULL | Documento de identidad |
| NOMBRES | VARCHAR(80) | NO  | NOT NULL | Nombres del paciente |
| APELLIDO_PATERNO | VARCHAR(50) | NO  | NOT NULL | Apellido paterno |
| APELLIDO_MATERNO | VARCHAR(50) | SÍ  | —   | Apellido materno |
| FECHA_NACIMIENTO | DATE | NO  | NOT NULL | Fecha de nacimiento |
| SEXO | CHAR(1) | NO  | CHECK('M','F') | Sexo biológico |
| TELEFONO | VARCHAR(15) | SÍ  | —   | Teléfono de contacto |
| EMAIL | VARCHAR(120) | SÍ  | —   | Correo electrónico |
| TIPO_SANGRE | VARCHAR(5) | SÍ  | —   | Grupo sanguíneo |
| ALERGIAS | VARCHAR(500) | SÍ  | —   | Alergias conocidas |
| ACTIVO | INT | NO  | DEFAULT 1, CHECK(0,1) | Estado activo/inactivo |
| FECHA_REGISTRO | TIMESTAMP | NO  | DEFAULT CURRENT_TIMESTAMP | Fecha de registro |

**Tabla: MEDICO**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Columna** | **Tipo** | **Nulo** | **Restricciones** | **Descripción** |
| ID_MEDICO | BIGINT | NO  | PK, SEQ_MEDICO | Identificador único |
| ID_USUARIO | BIGINT | NO  | FK → USUARIO, UNIQUE | Cuenta de acceso |
| ID_ESPECIALIDAD | BIGINT | NO  | FK → ESPECIALIDAD | Especialidad médica |
| CMP | VARCHAR(15) | NO  | UNIQUE, NOT NULL | Código del colegio médico |
| NOMBRES | VARCHAR(80) | NO  | NOT NULL | Nombres del médico |
| APELLIDO_PATERNO | VARCHAR(50) | NO  | NOT NULL | Apellido paterno |
| APELLIDO_MATERNO | VARCHAR(50) | SÍ  | —   | Apellido materno |
| DNI | VARCHAR(8) | NO  | UNIQUE, NOT NULL | Documento de identidad |
| TELEFONO | VARCHAR(15) | SÍ  | —   | Teléfono |
| EMAIL | VARCHAR(120) | SÍ  | —   | Correo electrónico |
| CONSULTORIO | VARCHAR(20) | SÍ  | —   | Número/nombre de consultorio |
| ACTIVO | INT | NO  | DEFAULT 1, CHECK(0,1) | Estado activo/inactivo |

**Tabla: CITA**

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Columna** | **Tipo** | **Nulo** | **Restricciones** | **Descripción** |
| ID_CITA | BIGINT | NO  | PK, SEQ_CITA | Identificador único |
| ID_PACIENTE | BIGINT | NO  | FK → PACIENTE | Paciente de la cita |
| ID_MEDICO | BIGINT | NO  | FK → MEDICO | Médico asignado |
| ID_ESPECIALIDAD | BIGINT | NO  | FK → ESPECIALIDAD | Especialidad |
| FECHA_CITA | DATE | NO  | NOT NULL | Fecha de la consulta |
| HORA_INICIO | VARCHAR(5) | NO  | NOT NULL | Hora inicio (HH:mm) |
| HORA_FIN | VARCHAR(5) | NO  | NOT NULL | Hora fin calculada |
| ESTADO | VARCHAR(20) | NO  | DEFAULT 'PROGRAMADA' | Estado del ciclo de vida |
| CANAL_RESERVA | VARCHAR(20) | NO  | DEFAULT 'WEB' | Canal de reserva |
| CODIGO_QR | VARCHAR(100) | SÍ  | UNIQUE | Código QR para check-in |
| NUMERO_TURNO | INT | SÍ  | —   | Turno del día |
| MOTIVO_CONSULTA | VARCHAR(300) | SÍ  | —   | Motivo de la visita |
| FECHA_REGISTRO | TIMESTAMP | NO  | DEFAULT CURRENT_TIMESTAMP | Fecha de registro |
| FECHA_CHECKIN | TIMESTAMP | SÍ  | —   | Momento del check-in |
| —   | —   | —   | UQ(ID_MEDICO, FECHA_CITA, HORA_INICIO) | Sin solapamiento de citas |

**Tabla: ATENCION**

|     |     |     |     |
| --- | --- | --- | --- |
| **Columna** | **Tipo** | **Nulo** | **Descripción** |
| ID_ATENCION | BIGINT | NO  | PK, identificador único |
| ID_CITA | BIGINT | NO  | FK → CITA, UNIQUE (relación 1:1) |
| ID_HISTORIA | BIGINT | NO  | FK → HISTORIA_CLINICA |
| ID_MEDICO | BIGINT | NO  | FK → MEDICO |
| MOTIVO_CONSULTA | VARCHAR(500) | SÍ  | Motivo de la visita |
| ANAMNESIS | CLOB | SÍ  | Historia del problema actual |
| EXAMEN_FISICO | CLOB | SÍ  | Hallazgos del examen clínico |
| DIAGNOSTICO_CIE10 | VARCHAR(10) | SÍ  | Código CIE-10 del diagnóstico |
| DIAGNOSTICO_DESC | VARCHAR(500) | SÍ  | Descripción del diagnóstico |
| TRATAMIENTO | CLOB | SÍ  | Plan de tratamiento |
| PRESION_ARTERIAL | VARCHAR(15) | SÍ  | Presión arterial (ej: 120/80) |
| FRECUENCIA_CARDIACA | INT | SÍ  | Latidos por minuto |
| TEMPERATURA | DECIMAL(4,1) | SÍ  | Temperatura en °C |
| PESO_KG | DECIMAL(5,2) | SÍ  | Peso en kilogramos |
| TALLA_CM | DECIMAL(5,2) | SÍ  | Talla en centímetros |
| SATURACION_O2 | INT | SÍ  | Saturación de oxígeno (%) |
| FIRMADO_POR | BIGINT | SÍ  | FK → USUARIO (médico que firma) |

**3.3 Diccionario de Datos**

|     |     |     |     |
| --- | --- | --- | --- |
| **Tabla** | **Descripción** | **Columnas** | **Relaciones** |
| ROL | Roles del sistema de acceso | 4   | N:N con USUARIO |
| USUARIO | Cuentas de acceso al sistema | 9   | N:N ROL, 1:1 PACIENTE/MEDICO |
| USUARIO_ROL | Tabla intermedia usuario-rol | 2   | FK a USUARIO y ROL |
| ESPECIALIDAD | Especialidades médicas | 6   | 1:N MEDICO, N:1 CITA |
| PACIENTE | Datos personales y médicos del paciente | 14  | FK USUARIO, 1:1 HISTORIA, 1:N CITA |
| MEDICO | Datos del profesional médico | 12  | FK USUARIO/ESPECIALIDAD, 1:N HORARIO/CITA |
| HORARIO_MEDICO | Horarios de atención del médico | 6   | FK MEDICO |
| HISTORIA_CLINICA | Expediente clínico del paciente | 6   | 1:1 PACIENTE, 1:N ATENCION |
| DIAGNOSTICO_CIE10 | Catálogo de diagnósticos internacionales | 4   | 1:N ATENCION |
| CITA | Reserva de consulta médica | 15  | FK PACIENTE/MEDICO/ESPECIALIDAD, 1:1 ATENCION, 1:N PAGO |
| PAGO | Registro de pagos por cita | 9   | FK CITA |
| ATENCION | Registro de la consulta médica | 17  | FK CITA/HISTORIA/MEDICO, 1:N RECETA/ORDEN |
| RECETA | Receta médica emitida | 5   | FK ATENCION, 1:N DETALLE_RECETA |
| DETALLE_RECETA | Medicamentos de la receta | 9   | FK RECETA |
| ORDEN_EXAMEN | Orden de exámenes diagnósticos | 8   | FK ATENCION |
| AUDITORIA_LOG | Registro de acciones del sistema | 9   | FK USUARIO |

**4 Diseño de la Interfaz de Usuario (UI/UX)**

**4.1 Principios de Diseño**

El sistema aplica los principios de diseño centrado en el usuario, priorizando la eficiencia operativa para el personal médico y administrativo.

|     |     |
| --- | --- |
| **Principio** | **Aplicación en el Sistema** |
| Consistencia | Misma barra de navegación, colores y tipografía en todos los módulos |
| Feedback inmediato | Mensajes de éxito/error tras cada operación (alertas Bootstrap) |
| Prevención de errores | Validaciones en frontend y backend antes de guardar datos |
| Eficiencia | Listados con búsqueda, filtros y paginación en todas las tablas |
| Accesibilidad | Soporte para teclado, etiquetas semánticas HTML5, contraste adecuado |
| Jerarquía visual | Encabezados claros, tablas organizadas, botones de acción destacados |

**4.2 Estructura General de las Vistas**

Todas las páginas del sistema comparten una estructura común basada en plantillas Thymeleaf con Bootstrap 5:

- **Barra de navegación superior:** logo, menú de módulos, usuario activo y botón de logout.
- **Área de contenido principal:** título del módulo, filtros de búsqueda, tabla de datos o formulario.
- **Zona de acciones:** botones de crear, editar, eliminar y exportar según el rol del usuario.
- **Zona de alertas:** mensajes de confirmación o error posicionados en la parte superior del contenido.

**4.3 Descripción de Pantallas Principales**

**Pantalla: Login — URL: /login**

|     |     |     |
| --- | --- | --- |
| **Elemento** | **Tipo** | **Descripción** |
| Campo Usuario | Input text | Ingreso del nombre de usuario registrado |
| Campo Contraseña | Input password | Contraseña del usuario |
| Botón Ingresar | Button submit | Valida credenciales y redirige al dashboard |
| Mensaje de error | Alert danger | Se muestra si credenciales incorrectas o cuenta bloqueada |

**Pantalla: Dashboard — URL: /dashboard**

|     |     |     |
| --- | --- | --- |
| **Elemento** | **Tipo** | **Descripción** |
| Tarjeta Citas Hoy | KPI Card | Total de citas programadas para la fecha actual |
| Tarjeta Pacientes | KPI Card | Total de pacientes registrados en el sistema |
| Tarjeta Médicos | KPI Card | Total de médicos activos en el sistema |
| Tarjeta Atenciones | KPI Card | Atenciones completadas en el mes actual |
| Lista Citas del Día | Tabla | Próximas citas con estado, paciente y médico |

**Pantalla: Gestión de Citas — URL: /citas**

|     |     |     |
| --- | --- | --- |
| **Elemento** | **Tipo** | **Descripción** |
| Filtro por Fecha | Input date | Filtra citas por fecha seleccionada |
| Filtro por Estado | Select | Filtra por PROGRAMADA, CONFIRMADA, ATENDIDA, etc. |
| Filtro por Médico | Select | Filtra citas por médico específico |
| Tabla de Citas | Tabla HTML | Lista con columnas: Turno, Paciente, Médico, Hora, Estado, Acciones |
| Botón Nueva Cita | Button primary | Abre formulario de agendamiento |
| Botón Cancelar | Button danger | Cancela la cita con confirmación |
| Botón Ver Detalle | Button info | Muestra el detalle completo de la cita |

**Pantalla: Registro de Atención Médica — URL: /atenciones**

|     |     |     |
| --- | --- | --- |
| **Elemento** | **Tipo** | **Descripción** |
| Sección Signos Vitales | Inputs numéricos | Presión, temperatura, peso, talla, FC, saturación O2 |
| Campo Anamnesis | Textarea | Historia del problema actual |
| Campo Examen Físico | Textarea | Hallazgos del examen clínico |
| Buscador CIE-10 | Autocomplete | Búsqueda de diagnóstico por código o descripción |
| Campo Tratamiento | Textarea | Plan de tratamiento indicado |
| Botón Agregar Receta | Button | Agrega medicamentos a la receta |
| Botón Guardar Atención | Button primary | Registra la atención y actualiza estado de cita |

**Pantalla: Pacientes — URL: /pacientes**

.

**Pantalla: Médicos — URL: /medicos**

**Pantalla: Historia Clínica — URL: /historia-clinica**

**Pantalla: Reportes — URL: /reportes (solo ADMINISTRADOR)**

**5 Especificación de Interfaces y APIs**

**5.1 Descripción General de la API**

El sistema expone una API REST bajo el prefijo /api/v1/ que permite la integración con aplicaciones externas. La autenticación se realiza mediante tokens JWT obtenidos en /api/auth/login.

|     |     |
| --- | --- |
| **Parámetro** | **Valor** |
| URL Base | http://localhost:8081/trinidad |
| Versión de la API | v1  |
| Autenticación | JWT Bearer Token (header: Authorization: Bearer &lt;token&gt;) |
| Formato de datos | JSON (Content-Type: application/json) |
| Expiración del token | 3600000 ms (1 hora) |
| Refresh token | 86400000 ms (24 horas) |

**5.2 Endpoint de Autenticación**

**POST /api/auth/login** Descripción: Autentica al usuario y retorna un token JWT. Acceso: Público (no requiere token).

Request Body:

|     |     |     |     |
| --- | --- | --- | --- |
| **Campo** | **Tipo** | **Requerido** | **Descripción** |
| username | String | Sí  | Nombre de usuario registrado |
| password | String | Sí  | Contraseña en texto plano |

Response Body (HTTP 200):

|     |     |     |
| --- | --- | --- |
| **Campo** | **Tipo** | **Descripción** |
| token | String | JWT de acceso con expiración de 1 hora |
| refreshToken | String | Token para renovar acceso sin re-login |
| username | String | Nombre de usuario autenticado |
| roles | List&lt;String&gt; | Lista de roles del usuario |

Códigos de respuesta:

- **200 OK:** autenticación exitosa, token generado.
- **401 Unauthorized:** credenciales incorrectas o cuenta bloqueada.
- **400 Bad Request:** campos faltantes o formato inválido.

**4.5.3 Endpoints del Módulo Citas**

|     |     |     |     |
| --- | --- | --- | --- |
| **Método** | **Endpoint** | **Descripción** | **Parámetros** |
| GET | /api/v1/citas | Lista todas las citas | ?fecha=YYYY-MM-DD (opcional) |
| GET | /api/v1/citas/{id} | Obtiene una cita por ID | id: Long (path) |
| GET | /api/v1/citas/paciente/{idPaciente} | Lista citas de un paciente | idPaciente: Long (path) |
| POST | /api/v1/citas | Agenda una nueva cita | Body: CitaDTO (JSON) |
| PUT | /api/v1/citas/{id}/estado | Cambia el estado de una cita | id: Long, estado: EstadoCita |
| POST | /api/v1/citas/{id}/cancelar | Cancela una cita | id: Long (path) |

**Contrato: POST /api/v1/citas (Agendar Cita)**

Request Body — CitaDTO:

|     |     |     |     |     |
| --- | --- | --- | --- | --- |
| **Campo** | **Tipo** | **Requerido** | **Validación** | **Descripción** |
| idPaciente | Long | Sí  | Debe existir | ID del paciente |
| idMedico | Long | Sí  | Debe existir | ID del médico |
| idEspecialidad | Long | Sí  | Debe existir | ID de la especialidad |
| fechaCita | String | Sí  | YYYY-MM-DD, fecha futura | Fecha de la consulta |
| horaInicio | String | Sí  | HH:mm, dentro del horario del médico | Hora de inicio |
| motivoConsulta | String | No  | Max 300 caracteres | Motivo de la visita |
| canalReserva | String | No  | WEB / APP / PRESENCIAL | Canal de reserva |

**Lógica de negocio ejecutada por el Service al agendar:**

1.  Verifica que el médico esté activo y atienda en la especialidad indicada.
2.  Valida que la fecha y hora estén dentro del horario del médico (HORARIO_MEDICO).
3.  Verifica que no exista otra cita para el mismo médico en el mismo slot (UQ_CITA_SLOT).
4.  Calcula la hora de fin sumando DURACION_MINUTOS de la especialidad a la hora de inicio.
5.  Asigna el número de turno correlativo del día para ese médico.
6.  Genera el código QR único para el check-in.
7.  Persiste la cita con estado PROGRAMADA.

**5.4 Endpoints del Módulo Pacientes**

|     |     |     |
| --- | --- | --- |
| **Método** | **Endpoint** | **Descripción** |
| GET | /api/v1/pacientes | Lista todos los pacientes activos |
| GET | /api/v1/pacientes/{id} | Obtiene un paciente por ID |
| GET | /api/v1/pacientes/dni/{dni} | Busca paciente por DNI |
| POST | /api/v1/pacientes | Registra un nuevo paciente |
| PUT | /api/v1/pacientes/{id} | Actualiza datos de un paciente |
| DELETE | /api/v1/pacientes/{id} | Desactiva un paciente (baja lógica) |

**5.5 Endpoints del Módulo Médicos**

|     |     |     |
| --- | --- | --- |
| **Método** | **Endpoint** | **Descripción** |
| GET | /api/v1/medicos | Lista todos los médicos activos |
| GET | /api/v1/medicos/{id} | Obtiene un médico por ID |
| GET | /api/v1/medicos/especialidad/{idEspecialidad} | Lista médicos de una especialidad |
| GET | /api/v1/medicos/disponibilidad/{idMedico} | Consulta disponibilidad de un médico |
| POST | /api/v1/medicos | Registra un nuevo médico |
| PUT | /api/v1/medicos/{id} | Actualiza datos de un médico |

**5.6 Endpoints del Módulo Atenciones**

|     |     |     |
| --- | --- | --- |
| **Método** | **Endpoint** | **Descripción** |
| GET | /api/v1/atenciones/{id} | Obtiene una atención por ID |
| GET | /api/v1/atenciones/cita/{idCita} | Obtiene la atención asociada a una cita |
| GET | /api/v1/atenciones/paciente/{idPaciente} | Lista atenciones de un paciente |
| POST | /api/v1/atenciones | Registra una nueva atención médica |
| PUT | /api/v1/atenciones/{id} | Actualiza una atención existente |

**5.7 Endpoints del Módulo Dashboard**

|     |     |     |
| --- | --- | --- |
| **Método** | **Endpoint** | **Descripción** |
| GET | /api/v1/dashboard/resumen | KPIs generales: pacientes, médicos, citas del día |
| GET | /api/v1/dashboard/estadisticas | Estadísticas de citas por período |
| GET | /api/v1/dashboard/indicadores | Indicadores de rendimiento del sistema |
| GET | /api/v1/dashboard/kpis | Métricas clave para el panel gerencial |

**5.8 Códigos de Respuesta HTTP**

|     |     |     |
| --- | --- | --- |
| **Código** | **Estado** | **Uso en el Sistema** |
| 200 | OK  | Operación exitosa (GET, PUT) |
| 201 | Created | Recurso creado exitosamente (POST) |
| 400 | Bad Request | Datos inválidos, validación fallida |
| 401 | Unauthorized | Token JWT ausente, expirado o inválido |
| 403 | Forbidden | Token válido pero sin permisos para el recurso |
| 404 | Not Found | El recurso solicitado no existe |
| 409 | Conflict | Conflicto de datos (slot ocupado, DNI duplicado) |
| 500 | Internal Server Error | Error inesperado del servidor |

**5.9 Seguridad de la API**

La API implementa un esquema de seguridad en capas. El filtro JwtAuthFilter intercepta cada petición a /api/v1/, extrae el token del header Authorization, lo valida con la clave secreta configurada y establece el contexto de seguridad de Spring.

|     |     |     |
| --- | --- | --- |
| **Capa de Seguridad** | **Mecanismo** | **Descripción** |
| Autenticación Web | Spring Security + Sesión HTTP | Login por formulario, sesión JSESSIONID |
| Autenticación API | JWT Bearer Token | Token firmado con clave secreta, expira en 1 hora |
| Encriptación | BCrypt (factor 10) | Contraseñas nunca almacenadas en texto plano |
| Autorización | @EnableMethodSecurity | Control de acceso a nivel de método por rol |
| CSRF | Deshabilitado para /api/\*\* | API REST no usa sesiones, no requiere CSRF |
| Auditoría | AuditoriaLog automático | Registro de todas las operaciones CRUD con usuario e IP |

**8\. IMPLEMENTACIÓN**

**a.SEMANA 1: Front-end Fundamentos**

**Syllabus: HTML, CSS, JavaScript - Conceptos y Frameworks de Front-end**

**TEMAS:**

**a. Estructura HTML5**

**b. CSS base con variables**

**c. JavaScript vanilla**

**ARCHIVOS DEL PROYECTO:**

**\- src/main/resources/templates/index.html**

**\- static/css/main.css**

**\- static/js/main.js**

**CODIGO REAL DEL PROYECTO:**

**(!DOCTYPE html)**

**(html lang="es")**

**(head)**

**(title)Trinidad Citas Medicas(/title)**

**(link rel="stylesheet" href="/css/main.css")**

**(/head)**

**(body)**

**(nav class="navbar")**

**(a href="/" class="navbar-brand")Trinity Citas(/a)**

**(/nav)**

**(main)**

**(h1)Sistema de Citas Medicas(/h1)**

**(/main)**

**(/body)**

**(/html)**

**SEMANA 2: Bootstrap Fundamentos**

**Syllabus: Introduccion a Bootstrap - CDN, Grid, Navbar, Tablas**

**TEMAS:**

**a. Integrar Bootstrap 5 via WebJars**

**b. Implementar grid system**

**c. Crear navbar responsive**

**ARCHIVOS DEL PROYECTO:**

**\- pom.xml**

**\- templates/fragments/header.html**

**CODIGO REAL DEL PROYECTO:**

**(dependency)**

**(groupId)org.webjars(/groupId)**

**(artifactId)bootstrap(/artifactId)**

**(version)5.3.0(/version)**

**(/dependency)**

**(nav class="navbar navbar-expand-lg navbar-dark bg-primary")**

**(div class="container-fluid")**

**(a class="navbar-brand" href="/")Trinity Citas(/a)**

**(button class="navbar-toggler" type="button" data-bs-toggle="collapse")**

**(span class="navbar-toggler-icon")(/span)**

**(/button)**

**(/div)**

**(/nav)**

**SEMANA 3: Bootstrap Componentes Avanzados**

**Syllabus: Bootstrap Aplicacion - Modales, Formularios, Estilos**

**TEMAS:**

**a. Formularios con validacion Bootstrap**

**b. Modales para confirmaciones**

**c. Cards visuales**

**ARCHIVOS DEL PROYECTO:**

**\- templates/citas/agendar.html**

**CODIGO REAL DEL PROYECTO:**

**(form th:action="@{/citas}" method="post" th:object="${cita}")**

**(div class="mb-3")**

**(label for="fecha" class="form-label")Fecha:(/label)**

**(input type="date" th:field="\*{fecha}" class="form-control" required)**

**(/div)**

**(button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmar")**

**Agendar**

**(/button)**

**(/form)**

**(div class="modal fade" id="confirmar")**

**(div class="modal-dialog")**

**(div class="modal-content")**

**(div class="modal-header")**

**(h5)Confirmar Cita(/h5)**

**(/div)**

**(div class="modal-footer")**

**(button class="btn btn-secondary" data-bs-dismiss="modal")Cancelar(/button)**

**(button class="btn btn-primary")Confirmar(/button)**

**(/div)**

**(/div)**

**(/div)**

**(/div)**

**SEMANA 4: APF1 - Frontend**

**Syllabus: Integracion de Temas Front-end**

**TEMAS:**

**a. Paginas responsivas 100%**

**b. Formularios funcionales**

**c. Interfaz profesional**

**ARCHIVOS DEL PROYECTO:**

**\- static/css/responsive.css**

**CODIGO REAL DEL PROYECTO:**

**@media (max-width: 768px) {**

**.container { width: 90%; margin: 0 auto; }**

**.navbar { flex-direction: column; }**

**.col-md-6 { width: 100%; }**

**}**

**(div class="container mt-4")**

**(div class="row")**

**(div class="col-md-6 col-lg-4")**

**(div class="card")**

**(h5 class="card-title")Especialidad(/h5)**

**(/div)**

**(/div)**

**(/div)**

**(/div)**

**SEMANA 5: Spring Boot Fundamentos**

**Syllabus: Spring Boot - Definicion, Ventajas, Configuracion**

**TEMAS:**

**a. Crear proyecto Spring Boot 3.2.5**

**b. Configurar application.properties**

**c. Estructura MVC**

**ARCHIVOS DEL PROYECTO:**

**\- TrinidadCitasApplication.java**

**\- application-oracle.properties**

**CODIGO REAL DEL PROYECTO:**

**@SpringBootApplication**

**public class TrinidadCitasApplication {**

**public static void main(String\[\] args) {**

**SpringApplication.run(TrinidadCitasApplication.class, args);**

**}**

**}**

**spring.application.name=trinidad-citas-medicas**

**server.port=8081**

**spring.thymeleaf.prefix=classpath:/templates/**

**spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE**

**spring.jpa.hibernate.ddl-auto=validate**

**SEMANA 6: Spring Web (Controllers y Rutas)**

**Syllabus: Spring Web - Definiciones, Usos, Aplicaciones**

**TEMAS:**

**a. Crear @Controller para paginas**

**b. @GetMapping y @PostMapping**

**c. Pasar datos con Model**

**ARCHIVOS DEL PROYECTO:**

**\- controller/api/CitaRestController.java**

**CODIGO REAL DEL PROYECTO:**

**@RestController**

**@RequestMapping("/api/v1/citas")**

**public class CitaRestController {**

**private final CitaService citaService;**

**@GetMapping**

**public List(CitaDTO) listar() {**

**return citaService.listarTodas();**

**}**

**@PostMapping**

**public ResponseEntity(CitaDTO) agendar(@Valid @RequestBody CitaDTO dto) {**

**return ResponseEntity.ok(citaService.agendar(dto));**

**}**

**}**

**SEMANA 7: Thymeleaf (Motor de Plantillas)**

**Syllabus: Thymeleaf - Definiciones, Usos, Ventajas**

**TEMAS:**

**a. Templates Thymeleaf dinamicos**

**b. Expresiones y variables**

**c. Fragmentos reutilizables**

**ARCHIVOS DEL PROYECTO:**

**\- templates/citas/agendar.html**

**CODIGO REAL DEL PROYECTO:**

**(html xmlns:th="http://www.thymeleaf.org")**

**(head th:replace="~{fragments/header :: head}")(/head)**

**(body)**

**(nav th:replace="~{fragments/header :: navbar}")(/nav)**

**(form th:action="@{/citas}" method="post" th:object="${cita}")**

**(table class="table")**

**(tr th:each="medico : ${medicos}")**

**(td th:text="${medico.nombre}")(/td)**

**(/tr)**

**(/table)**

**(/form)**

**(/body)**

**(/html)**

**SEMANA 8: APF2 - Spring Web + Thymeleaf**

**Syllabus: Integracion Spring Web + Thymeleaf**

**TEMAS:**

**a. 5+ Controllers**

**b. Vistas con Thymeleaf dinamicas**

**c. Fragmentos compartidos**

**ARCHIVOS DEL PROYECTO:**

**\- controller/api/**

**\- templates/**

**CODIGO REAL DEL PROYECTO:**

**@RestController @RequestMapping("/api/v1/medicos")**

**public class MedicoRestController {**

**@GetMapping**

**public List(MedicoDTO) listar() {**

**return medicoService.listarTodos();**

**}**

**}**

**(div th:replace="~{fragments/header}")(/div)**

**(main th:replace="~{fragments/content}")(/main)**

**(div th:replace="~{fragments/footer}")(/div)**

**SEMANA 9: ORM y Spring Data**

**Syllabus: ORM - Hibernate, Spring Data, Configuracion BD**

**TEMAS:**

**a. Configurar datasource**

**b. Entidades JPA**

**c. Configurar Hibernate**

**ARCHIVOS DEL PROYECTO:**

**\- model/Cita.java**

**CODIGO REAL DEL PROYECTO:**

**@Entity**

**@Table(name = "CITAS")**

**@Data**

**public class Cita {**

**@Id**

**@GeneratedValue(strategy = GenerationType.SEQUENCE)**

**private Long id;**

**@ManyToOne**

**@JoinColumn(name = "PACIENTE_ID")**

**private Paciente paciente;**

**@Column(name = "FECHA_CITA")**

**@NotNull**

**private LocalDateTime fechaCita;**

**}**

**SEMANA 10: JPA y CRUD Completo**

**Syllabus: JPA - CRUD Web y REST, Operaciones BD**

**TEMAS:**

**a. Repositories JpaRepository**

**b. Servicios CRUD**

**c. Controllers conectados**

**ARCHIVOS DEL PROYECTO:**

**\- repository/CitaRepository.java**

**\- service/CitaService.java**

**CODIGO REAL DEL PROYECTO:**

**@Repository**

**public interface CitaRepository extends JpaRepository(Cita, Long) {**

**List(Cita) findByPacienteId(Long pacienteId);**

**}**

**@Service**

**public class CitaService {**

**public CitaDTO agendar(CitaDTO dto) {**

**Cita cita = new Cita();**

**return mapToDTO(repository.save(cita));**

**}**

**}**

**SEMANA 11: Spring Validator y Relaciones**

**Syllabus: Spring Validator - Validacion, Relaciones**

**TEMAS:**

**a. Validaciones Bean Validation**

**b. Manejo de errores**

**c. Relaciones OneToMany**

**ARCHIVOS DEL PROYECTO:**

**\- model/Paciente.java**

**CODIGO REAL DEL PROYECTO:**

**@Entity**

**public class Paciente {**

**@NotBlank(message = "Nombre requerido")**

**@Column(nullable = false)**

**private String nombre;**

**@Email(message = "Email invalido")**

**private String email;**

**@OneToMany(mappedBy = "paciente")**

**private List(Cita) citas;**

**}**

**@PostMapping**

**public String guardar(@Valid @ModelAttribute PacienteDTO dto, BindingResult result) {**

**if (result.hasErrors()) return "form";**

**return "redirect:/pacientes";**

**}**

**SEMANA 12: APF3 - BD + CRUD Completo**

**Syllabus: Integracion JPA + Hibernate - CRUD completo**

**TEMAS:**

**a. CRUD para 5+ entidades**

**b. Relaciones funcionando**

**c. Validaciones en endpoints**

**ARCHIVOS DEL PROYECTO:**

**\- src/main/java/com/trinidad/citas/**

**CODIGO REAL DEL PROYECTO:**

**CREATE TABLE PACIENTES (ID NUMBER PRIMARY KEY, NOMBRE VARCHAR2(100));**

**CREATE TABLE CITAS (ID NUMBER PRIMARY KEY, PACIENTE_ID NUMBER REFERENCES PACIENTES(ID));**

**INSERT INTO PACIENTES VALUES (1, 'Juan Perez');**

**COMMIT;**

**@DeleteMapping("/{id}")**

**public ResponseEntity(Void) eliminar(@PathVariable Long id) {**

**citaService.eliminar(id);**

**return ResponseEntity.noContent().build();**

**}**

# 9\. CONCLUSIONES

- Se logró documentar los procesos actuales de Trinidad & Especialidades Médicas S.A.C. mediante la notación BPMN, identificando cuellos de botella y oportunidades de mejora significativas en la gestión de citas, registro de pacientes y manejo de historias clínicas.
- Se diseñó una arquitectura de sistema web multicapa (Frontend HTML/CSS/JS + Backend Java + Oracle DB) que cumple con los requerimientos funcionales y no funcionales identificados durante el análisis.
- El proceso de gestión de citas TO-BE propuesto reduce drásticamente los tiempos de atención: de 8 minutos promedio a menos de 1 minuto para agendar una cita, y de 12 minutos a 5 segundos para localizar una historia clínica.
- La digitalización de los procesos permite obtener reportes e indicadores en tiempo real, mejorando la toma de decisiones gerenciales.
- El sistema propuesto se alinea con la normativa peruana vigente sobre historias clínicas electrónicas (Ley N° 30024) y protección de datos personales (Ley N° 29733).

# 10\. RECOMENDACIONES

- Se recomienda implementar en una fase posterior la integración con sistemas de diagnóstico por imágenes (PACS) para completar el ecosistema digital del centro médico.
- Considerar la implementación de notificaciones por SMS y WhatsApp API para maximizar la efectividad de los recordatorios de citas.
- Capacitar a todo el personal del centro médico en el uso del sistema antes de su puesta en producción.
- Establecer un plan de respaldo y recuperación de datos que cumpla con los estándares de seguridad del sector salud.
- Evaluar la migración futura a una arquitectura de microservicios conforme el sistema escale en funcionalidades y usuarios.
- Integrar el sistema con SIS (Seguro Integral de Salud) y aseguradoras privadas para automatizar la verificación de cobertura.

# 11\. BIBLIOGRAFÍAS

Dumas, M., La Rosa, M., Mendling, J., & Reijers, H. A. (2018). Fundamentals of Business Process Management (2nd ed.). Springer.

Sommerville, I. (2016). Software Engineering (10th ed.). Pearson.

Oracle Corporation. (2024). Oracle Database 21c Express Edition Documentation. https://docs.oracle.com/en/database/oracle/oracle-database/21/

Organización Mundial de la Salud. (2020). Digital Health Platform Handbook: Building a Digital Information Infrastructure. WHO.

Pressman, R. S., & Maxim, B. R. (2020). Software Engineering: A Practitioner’s Approach (9th ed.). McGraw-Hill.

Trinidad & Especialidades Médicas S.A.C. (2026). Sitio web institucional. https://trinidadtarapoto.com/

Congreso de la República del Perú. (2013). Ley N° 30024 – Ley que crea el Registro Nacional de Historias Clínicas Electrónicas.

Congreso de la República del Perú. (2011). Ley N° 29733 – Ley de Protección de Datos Personales.