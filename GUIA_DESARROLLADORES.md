# 👨‍💻 GUÍA PARA DESARROLLADORES - Trinidad Citas Médicas

## 🚀 INICIO RÁPIDO

### Prerequisitos
```
✅ Java 17+ (JDK)
✅ Maven 3.8+
✅ IDE: IntelliJ IDEA / VS Code + Extensions
✅ Git
```

### Setup Inicial (5 minutos)

#### 1. Clonar y abrir proyecto
```bash
git clone <repo-url>
cd trinidad-citas-medicas
```

#### 2. Instalar dependencias Maven
```bash
mvn clean install
```

#### 3. Ejecutar con H2 (desarrollo)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

O en IDE:
- Click derecho en `TrinidadCitasApplication.java` → Run As → Spring Boot App
- Configure VM options: `-Dspring.profiles.active=dev`

#### 4. Acceder a la aplicación
```
http://localhost:8081/trinidad
```

#### 5. Credenciales iniciales (seed data)
```
Usuario: admin@trinidad.com
Contraseña: admin123

Usuario: medico@trinidad.com
Contraseña: medico123

Usuario: paciente@trinidad.com
Contraseña: paciente123
```

---

## 🔧 CONFIGURACIÓN DE DESARROLLO

### application-dev.properties (H2 en memoria)
```properties
# H2 Database (in-memory, recreated on restart)
spring.datasource.url=jdbc:h2:mem:trinidaddb;MODE=Oracle;...
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console (web UI)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Acceder en: http://localhost:8081/trinidad/h2-console
```

### Para trabajar con Oracle (producción)
```bash
# 1. Instalar Oracle 21c XE en localhost:1521
# 2. Crear usuario TRINIDAD_DB

sqlplus sys as sysdba
SQL> CREATE USER TRINIDAD_DB IDENTIFIED BY Trinidad2026;
SQL> GRANT CONNECT, RESOURCE, DBA TO TRINIDAD_DB;

# 3. Ejecutar Spring Boot con profile Oracle
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=oracle"

# O en application.properties:
# spring.profiles.active=oracle,api,web
```

---

## 📁 ESTRUCTURA DE CARPETAS PARA DESARROLLO

```
trinidad-citas-medicas/
│
├── .git/                     # Control de versiones
├── .gitignore               # Archivos a ignorar
│
├── pom.xml                  # Dependencias Maven
│
├── src/
│   ├── main/
│   │   ├── java/com/trinidad/citas/
│   │   │   ├── config/             👈 Editar para seguridad
│   │   │   ├── model/              👈 Agregar entidades aquí
│   │   │   ├── repository/         👈 Agregar repos aquí
│   │   │   ├── service/            👈 Lógica de negocio
│   │   │   ├── controller/web/     👈 Web controllers
│   │   │   ├── controller/api/     👈 REST endpoints
│   │   │   ├── dto/                👈 DTOs aquí
│   │   │   ├── exception/          👈 Excepciones custom
│   │   │   ├── util/               👈 Utilidades
│   │   │   ├── validation/         👈 Validadores
│   │   │   └── TrinidadCitasApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Config principal
│   │       ├── application-dev.properties     # Config dev (H2)
│   │       ├── application-oracle.properties  # Config prod (Oracle)
│   │       ├── templates/                     # Vistas Thymeleaf
│   │       ├── static/                        # CSS, JS, img
│   │       ├── db/
│   │       │   ├── oracle/
│   │       │   │   ├── 00_create_user.sql     # Crear usuario Oracle
│   │       │   │   ├── 01_schema.sql          # DDL tablas
│   │       │   │   └── 02_seed.sql            # Datos iniciales
│   │       │   └── h2/
│   │       │       ├── 01_schema_h2.sql       # DDL para H2
│   │       │       └── 02_seed_h2.sql         # Seed H2
│   │       └── META-INF/
│   │
│   └── test/
│       ├── java/com/trinidad/citas/
│       │   └── ... (unit tests)
│       └── resources/
│           └── application-test.properties
│
├── target/                   # Compilados (generado)
├── .idea/                    # IntelliJ config (generado)
│
├── docs/
│   ├── API.md               # Documentación API
│   └── ...
│
├── logs/                     # Archivos de log (generado)
│
├── README.md                # Descripción general
├── ANALISIS_PROYECTO_COMPLETO.md      # 👈 ANÁLISIS NUEVO
├── DIAGRAMA_ARQUITECTURA.md           # 👈 DIAGRAMAS
├── RESUMEN_RAPIDO.md                  # 👈 RESUMEN
│
└── PLAN_SEMANAS.md          # Roadmap
```

---

## 📝 WORKFLOW DE DESARROLLO

### 1️⃣ AGREGAR UNA NUEVA ENTIDAD

**Ejemplo: Agregar `Medicamento` (tabla de medicinas)

#### Paso 1: Crear la Entity
Archivo: `src/main/java/com/trinidad/citas/model/Medicamento.java`
```java
package com.trinidad.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "MEDICAMENTO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medicamento")
    @SequenceGenerator(name = "seq_medicamento", sequenceName = "SEQ_MEDICAMENTO", allocationSize = 1)
    @Column(name = "ID_MEDICAMENTO")
    private Long idMedicamento;
    
    @NotBlank
    @Size(max = 150)
    @Column(name = "NOMBRE_GENERICO", nullable = false, unique = true, length = 150)
    private String nombreGenerico;
    
    @Size(max = 150)
    @Column(name = "NOMBRE_COMERCIAL", length = 150)
    private String nombreComercial;
    
    @Size(max = 50)
    @Column(name = "PRESENTACION", length = 50)
    private String presentacion;
    
    @NotBlank
    @Size(max = 30)
    @Column(name = "UNIDAD_MEDIDA", nullable = false, length = 30)
    private String unidadMedida; // mg, ml, comprimido, etc.
    
    @Column(name = "ACTIVO", nullable = false)
    @Builder.Default
    private Integer activo = 1;
}
```

#### Paso 2: Crear el Repository
Archivo: `src/main/java/com/trinidad/citas/repository/MedicamentoRepository.java`
```java
package com.trinidad.citas.repository;

import com.trinidad.citas.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    
    Optional<Medicamento> findByNombreGenerico(String nombreGenerico);
    
    List<Medicamento> findByActivoOrderByNombreGenerico(Integer activo);
    
    List<Medicamento> findByNombreGenericoContainingIgnoreCaseOrNombreComercialContainingIgnoreCase(
        String generico, String comercial);
}
```

#### Paso 3: Crear el DTO
Archivo: `src/main/java/com/trinidad/citas/dto/MedicamentoDTO.java`
```java
package com.trinidad.citas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MedicamentoDTO {
    
    private Long idMedicamento;
    
    @NotBlank @Size(max = 150)
    private String nombreGenerico;
    
    @Size(max = 150)
    private String nombreComercial;
    
    @Size(max = 50)
    private String presentacion;
    
    @NotBlank @Size(max = 30)
    private String unidadMedida;
    
    private Integer activo;
}
```

#### Paso 4: Crear el Service
Archivo: `src/main/java/com/trinidad/citas/service/MedicamentoService.java`
```java
package com.trinidad.citas.service;

import com.trinidad.citas.dto.MedicamentoDTO;
import com.trinidad.citas.exception.ResourceNotFoundException;
import com.trinidad.citas.model.Medicamento;
import com.trinidad.citas.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentoService {
    
    private final MedicamentoRepository medicamentoRepository;
    private final AuditoriaLogService auditoriaLogService;
    
    @Transactional
    public MedicamentoDTO crear(MedicamentoDTO dto) {
        Medicamento medicamento = Medicamento.builder()
            .nombreGenerico(dto.getNombreGenerico())
            .nombreComercial(dto.getNombreComercial())
            .presentacion(dto.getPresentacion())
            .unidadMedida(dto.getUnidadMedida())
            .activo(1)
            .build();
        
        medicamentoRepository.save(medicamento);
        
        auditoriaLogService.registrar("CREAR_MEDICAMENTO", "MEDICAMENTO", 
            medicamento.getIdMedicamento().toString(), "Nuevo medicamento: " + dto.getNombreGenerico());
        
        return convertirDTO(medicamento);
    }
    
    public List<MedicamentoDTO> listarActivos() {
        return medicamentoRepository.findByActivoOrderByNombreGenerico(1)
            .stream()
            .map(this::convertirDTO)
            .collect(Collectors.toList());
    }
    
    public MedicamentoDTO obtener(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado"));
        return convertirDTO(medicamento);
    }
    
    @Transactional
    public MedicamentoDTO actualizar(Long id, MedicamentoDTO dto) {
        Medicamento medicamento = medicamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado"));
        
        medicamento.setNombreGenerico(dto.getNombreGenerico());
        medicamento.setNombreComercial(dto.getNombreComercial());
        medicamento.setPresentacion(dto.getPresentacion());
        medicamento.setUnidadMedida(dto.getUnidadMedida());
        
        medicamentoRepository.save(medicamento);
        
        auditoriaLogService.registrar("ACTUALIZAR_MEDICAMENTO", "MEDICAMENTO", 
            id.toString(), "Medicamento actualizado");
        
        return convertirDTO(medicamento);
    }
    
    private MedicamentoDTO convertirDTO(Medicamento medicamento) {
        return new MedicamentoDTO(
            medicamento.getIdMedicamento(),
            medicamento.getNombreGenerico(),
            medicamento.getNombreComercial(),
            medicamento.getPresentacion(),
            medicamento.getUnidadMedida(),
            medicamento.getActivo()
        );
    }
}
```

#### Paso 5: Crear el REST Controller
Archivo: `src/main/java/com/trinidad/citas/controller/api/MedicamentoRestController.java`
```java
package com.trinidad.citas.controller.api;

import com.trinidad.citas.dto.MedicamentoDTO;
import com.trinidad.citas.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicamentos")
@RequiredArgsConstructor
public class MedicamentoRestController {
    
    private final MedicamentoService medicamentoService;
    
    @GetMapping
    public ResponseEntity<List<MedicamentoDTO>> listar() {
        return ResponseEntity.ok(medicamentoService.listarActivos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(medicamentoService.obtener(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicamentoDTO> crear(@Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(medicamentoService.crear(dto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicamentoDTO> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody MedicamentoDTO dto) {
        return ResponseEntity.ok(medicamentoService.actualizar(id, dto));
    }
}
```

#### Paso 6: Crear el Web Controller (Thymeleaf)
Archivo: `src/main/java/com/trinidad/citas/controller/web/MedicamentoWebController.java`
```java
package com.trinidad.citas.controller.web;

import com.trinidad.citas.dto.MedicamentoDTO;
import com.trinidad.citas.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicamentos")
@RequiredArgsConstructor
public class MedicamentoWebController {
    
    private final MedicamentoService medicamentoService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarActivos());
        return "medicamentos/lista";
    }
    
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("medicamento", medicamentoService.obtener(id));
        return "medicamentos/detalle";
    }
    
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("medicamento", new MedicamentoDTO());
        return "medicamentos/form";
    }
    
    @PostMapping
    public String guardar(@Valid MedicamentoDTO medicamento) {
        medicamentoService.crear(medicamento);
        return "redirect:/medicamentos";
    }
}
```

#### Paso 7: Crear templates Thymeleaf
Archivo: `src/main/resources/templates/medicamentos/lista.html`
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Medicamentos - Trinidad</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css">
</head>
<body>
    <nav th:replace="fragments/header :: navbar"></nav>
    
    <div class="container mt-5">
        <div class="row mb-3">
            <div class="col">
                <h1>Medicamentos</h1>
            </div>
            <div class="col text-end">
                <a href="/medicamentos/nuevo" class="btn btn-primary">+ Nuevo Medicamento</a>
            </div>
        </div>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Genérico</th>
                    <th>Comercial</th>
                    <th>Presentación</th>
                    <th>Unidad</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="med : ${medicamentos}">
                    <td th:text="${med.nombreGenerico}"></td>
                    <td th:text="${med.nombreComercial}"></td>
                    <td th:text="${med.presentacion}"></td>
                    <td th:text="${med.unidadMedida}"></td>
                    <td>
                        <a th:href="@{/medicamentos/{id}(id=${med.idMedicamento})}" 
                           class="btn btn-sm btn-info">Ver</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    
    <script src="/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

#### Paso 8: Crear migrations SQL (si aplica)
Archivo: `src/main/resources/db/oracle/03_medicamentos.sql`
```sql
CREATE TABLE MEDICAMENTO (
    ID_MEDICAMENTO    NUMBER(10)         NOT NULL,
    NOMBRE_GENERICO   VARCHAR2(150 CHAR) NOT NULL,
    NOMBRE_COMERCIAL  VARCHAR2(150 CHAR),
    PRESENTACION      VARCHAR2(50 CHAR),
    UNIDAD_MEDIDA     VARCHAR2(30 CHAR)  NOT NULL,
    ACTIVO            NUMBER(1)          DEFAULT 1 NOT NULL,
    CONSTRAINT PK_MEDICAMENTO PRIMARY KEY (ID_MEDICAMENTO),
    CONSTRAINT UQ_MED_GENERICO UNIQUE (NOMBRE_GENERICO),
    CONSTRAINT CK_MED_ACTIVO CHECK (ACTIVO IN (0, 1))
);
CREATE SEQUENCE SEQ_MEDICAMENTO START WITH 1 INCREMENT BY 1 NOCACHE;
```

---

### 2️⃣ MODIFICAR UN FLUJO EXISTENTE

**Ejemplo: Cambiar validación de DNI**

#### Encontrar
Archivo: `src/main/java/com/trinidad/citas/validation/DNIValidator.java`

#### Editar
```java
@Component
public class DNIValidator implements ConstraintValidator<ValidDNI, String> {
    
    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) {
        // Actual: solo verifica length == 8 && numeric
        // Nuevo: agregar validación de dígito verificador
        
        if (dni == null) return true;
        if (!dni.matches("\\d{8}")) return false;
        
        // Validación dígito verificador (algoritmo peruano)
        int[] multiplicadores = {3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;
        for (int i = 0; i < 8; i++) {
            suma += (dni.charAt(i) - '0') * multiplicadores[i];
        }
        int digitoVerificador = 11 - (suma % 11);
        if (digitoVerificador == 11) digitoVerificador = 0;
        if (digitoVerificador == 10) return false;
        
        return true;
    }
}
```

#### Test
```bash
mvn test -Dtest=DNIValidatorTest
```

---

### 3️⃣ AGREGAR UN NUEVO ENDPOINT REST

**Ejemplo: Buscar medicamentos por nombre**

#### En MedicamentoRestController
```java
@GetMapping("/buscar")
public ResponseEntity<List<MedicamentoDTO>> buscar(@RequestParam String termino) {
    // Buscar en nombreGenerico o nombreComercial
    return ResponseEntity.ok(medicamentoService.buscar(termino));
}
```

#### En MedicamentoService
```java
public List<MedicamentoDTO> buscar(String termino) {
    return medicamentoRepository
        .findByNombreGenericoContainingIgnoreCaseOrNombreComercialContainingIgnoreCase(termino, termino)
        .stream()
        .map(this::convertirDTO)
        .collect(Collectors.toList());
}
```

#### Test con cURL
```bash
curl -X GET "http://localhost:8081/trinidad/api/v1/medicamentos/buscar?termino=paracetamol" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 🧪 TESTING

### Run Unit Tests
```bash
mvn test
```

### Run con Coverage
```bash
mvn clean test jacoco:report
# Report en: target/site/jacoco/index.html
```

### Debug Mode
En IntelliJ:
1. Right-click en test method
2. Debug '<test-name>'
3. Set breakpoints (F8 para ir línea a línea)

---

## 🐛 DEBUGGING

### Ver SQL generado por Hibernate
```properties
# application-dev.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Logs por componente
```properties
logging.level.com.trinidad.citas=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate=DEBUG
```

### H2 Console (dev)
```
URL: http://localhost:8081/trinidad/h2-console
JDBC URL: jdbc:h2:mem:trinidaddb
User: sa
Password: (dejar en blanco)
```

### Postman Collection
```
Importar: docs/Trinidad-API.postman_collection.json
Environment: docs/Trinidad-ENV.postman_environment.json
Variables: {{base_url}}, {{token}}, etc.
```

---

## 📚 CONVENCIONES DE CÓDIGO

### Naming
```
Classes:           PascalCase (Usuario, PacienteService, CitaRestController)
Methods:           camelCase (crearCita, obtenerDisponibles)
Constants:         UPPER_SNAKE_CASE (MAX_INTENTOS_LOGIN = 5)
Variables:         camelCase (usuarioId, nombrePaciente)
Database:          UPPER_SNAKE_CASE (ID_USUARIO, NOMBRE_PACIENTE)
```

### Annotations Lombok
```java
@Data              // @Getter @Setter @ToString @EqualsAndHashCode
@NoArgsConstructor // Constructor sin args
@AllArgsConstructor // Constructor con todos los args
@Builder           // Builder pattern
@Slf4j             // Log4j logger
```

### Entity Best Practices
```java
@Entity
@Table(name = "XXX")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_xxx")
    @SequenceGenerator(name = "seq_xxx", sequenceName = "SEQ_XXX", allocationSize = 1)
    @Column(name = "ID_XXX")
    private Long id;
    
    @NotBlank // Use NotBlank for strings, NotNull for objects
    @Size(max = 100)
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY) // Lazy by default to avoid N+1
    @JoinColumn(name = "PARENT_ID", nullable = false)
    @ToString.Exclude // Avoid circular refs in toString()
    private Parent parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Child> children = new ArrayList<>();
}
```

### Service Best Practices
```java
@Service
@RequiredArgsConstructor // Constructor injection (final fields)
@Transactional
public class MyService {
    
    private final MyRepository repository;
    private final AuditoriaLogService auditoriaService;
    
    @Transactional // Rollback on exception
    public MyDTO crear(MyDTO dto) {
        // 1. Validar
        if (repository.existsByName(dto.getName())) {
            throw new BusinessException("Ya existe registro con ese nombre");
        }
        
        // 2. Crear entidad
        MyEntity entity = MyEntity.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .build();
        
        // 3. Guardar
        repository.save(entity);
        
        // 4. Registrar en auditoría
        auditoriaService.registrar("CREAR_XXX", "XXX", entity.getId().toString(), 
            "Nuevo registro: " + dto.getName());
        
        // 5. Retornar DTO
        return convertirDTO(entity);
    }
}
```

---

## 📦 RELEASE / DEPLOYMENT

### Build JAR
```bash
mvn clean package -DskipTests
# Output: target/trinidad-citas-medicas-1.0.0.jar
```

### Run JAR
```bash
java -jar target/trinidad-citas-medicas-1.0.0.jar \
  --spring.profiles.active=oracle \
  --spring.datasource.url=jdbc:oracle:thin:@prod-server:1521:XE \
  --spring.datasource.username=TRINIDAD_DB \
  --spring.datasource.password=<password>
```

### Docker (opcional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/trinidad-citas-medicas-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=oracle"]
```

---

## 🔗 RECURSOS ÚTILES

### Documentación
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Thymeleaf: https://www.thymeleaf.org/

### Tools
- Postman: https://www.postman.com/downloads/
- DBeaver: https://dbeaver.io/download/
- SourceTree (Git GUI): https://www.sourcetreeapp.com/

---

## ✅ CHECKLIST ANTES DE COMMIT

- [ ] Código compila sin errores: `mvn clean compile`
- [ ] Tests pasan: `mvn test`
- [ ] Sin warnings de Lombok/Spring
- [ ] Código formateado (Ctrl+Alt+L en IntelliJ)
- [ ] Comentarios en español/inglés consistente
- [ ] DTOs actualizados si cambió entity
- [ ] Auditoría registrada en servicio
- [ ] Endpoint documentado (swagger si aplica)
- [ ] Migration SQL si cambió BD
- [ ] Mensaje de commit descriptivo

---

## 🆘 TROUBLESHOOTING

### Error: "Unable to connect to Oracle"
```
✅ Verifica: hostname, puerto (1521), usuario (TRINIDAD_DB)
✅ Asegúrate que Oracle está corriendo: sqlplus TRINIDAD_DB@XE
✅ Prueba en dev mode con H2: mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Error: "Port 8081 already in use"
```bash
# Encuentra proceso usando puerto 8081
lsof -i :8081  # macOS/Linux
netstat -ano | findstr :8081  # Windows

# Matar proceso
kill -9 <PID>  # macOS/Linux
taskkill /PID <PID> /F  # Windows
```

### Lombok no funciona en IDE
```
IntelliJ:
1. File → Settings → Plugins → Search "Lombok" → Install
2. File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable
3. Restart IDE
```

### JWT Token expirado
```
Tokens expiran cada 1 hora. Para refreshar:
POST /api/v1/auth/refresh
Header: Authorization: Bearer <EXPIRED_TOKEN>
```

