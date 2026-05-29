# Trinidad Médico - Guía de Estilos de Formularios

## 🎨 Sistema de Diseño de Formularios

Este documento describe la estructura y clases CSS recomendadas para todos los formularios en la aplicación Trinidad Médico.

## 📋 Estructura Básica de un Formulario

```html
<div class="form-container">
    <!-- Encabezado opcional con icono -->
    <div class="form-header">
        <i class="bi bi-person-plus"></i>
        <h3>Título de la Sección</h3>
    </div>

    <form th:action="@{/ruta/accion}" th:object="${objeto}" method="post" novalidate>
        
        <!-- Fila con múltiples campos (responsive) -->
        <div class="form-row">
            <div class="form-group">
                <label for="campo1" class="required">Etiqueta de Campo *</label>
                <input type="text" id="campo1" th:field="*{propiedad}" 
                       class="form-control" placeholder="Placeholder..."
                       th:classappend="${#fields.hasErrors('propiedad')} ? 'is-invalid'">
                <div class="invalid-feedback" th:if="${#fields.hasErrors('propiedad')}" 
                     th:errors="*{propiedad}"></div>
            </div>
        </div>

        <!-- Sección con su propio encabezado -->
        <div class="form-section">
            <div class="form-header">
                <i class="bi bi-icon-name"></i>
                <h3>Nueva Sección</h3>
            </div>
            <!-- Contenido de la sección -->
        </div>

        <!-- Botones de acción al final -->
        <div class="form-actions">
            <button type="submit" class="btn btn-success btn-lg">
                <i class="bi bi-check-circle"></i>
                Guardar
            </button>
            <a href="#" class="btn btn-secondary btn-lg">
                <i class="bi bi-x-circle"></i>
                Cancelar
            </a>
        </div>
    </form>
</div>
```

## 🏗️ Componentes y Clases

### 1. Contenedor del Formulario

```html
<div class="form-container">
    <!-- Contenido -->
</div>
```

**Estilos aplicados:**
- Fondo con glassmorphism (blur de fondo)
- Borde cyan semitransparente
- Border-radius: 20px
- Padding: 32px
- Sombra profesional

### 2. Encabezado del Formulario

```html
<div class="form-header">
    <i class="bi bi-icon-name"></i>
    <h3>Título</h3>
</div>
```

**Estilos aplicados:**
- Ícono cyan (#06b6d4)
- Texto blanco
- Borde inferior cyan
- Espaciado visual

### 3. Secciones del Formulario

```html
<div class="form-section">
    <!-- Contenido relacionado -->
</div>
```

**Propósito:** Agrupar campos relacionados lógicamente

### 4. Filas Responsivas

```html
<div class="form-row">
    <div class="form-group">...</div>
    <div class="form-group">...</div>
</div>
```

**Características:**
- Grid responsivo (auto-fit, minmax 300px)
- Espaciado automático entre campos
- Se ajusta automáticamente en móviles

### 5. Grupo de Formulario

```html
<div class="form-group">
    <label for="campo" class="required">Etiqueta *</label>
    <input type="text" id="campo" class="form-control">
</div>
```

**Clases de Etiqueta:**
- `.required` - Añade asterisco rojo (*) automáticamente

### 6. Campos de Entrada - Texto

```html
<!-- Texto simple -->
<input type="text" class="form-control" placeholder="Placeholder...">

<!-- Email -->
<input type="email" class="form-control" placeholder="correo@ejemplo.com">

<!-- Teléfono -->
<input type="tel" class="form-control" placeholder="+51 987 654 321">

<!-- Número -->
<input type="number" class="form-control" placeholder="0">

<!-- Fecha -->
<input type="date" class="form-control">

<!-- Hora -->
<input type="time" class="form-control">

<!-- Búsqueda -->
<input type="search" class="form-control" placeholder="Buscar...">
```

**Estados del Input:**
- Normal: Fondo semitransparente, borde gris claro
- Focus: Borde cyan, sombra azul, fondo más opaco
- Hover: Borde más visible, fondo ligeramente más opaco
- Disabled: Opacidad reducida, cursor no-allowed
- Invalid: Borde rojo, fondo rojo semitransparente

### 7. Área de Texto - Textarea

```html
<div class="form-group">
    <label for="descripcion">Descripción</label>
    <textarea id="descripcion" class="form-control" 
              placeholder="Escribir descripción..." rows="3"></textarea>
    <div class="form-text">Texto de ayuda opcional</div>
</div>
```

**Características:**
- Redimensionable verticalmente
- Altura mínima: 100px
- Sombra de focus extendida

### 8. Select/Combo Box

```html
<select class="form-select">
    <option value="">Seleccionar...</option>
    <option value="valor1">Opción 1</option>
    <option value="valor2">Opción 2</option>
</select>
```

**Características:**
- Flecha dropdown personalizada (cyan)
- Estilos hover y focus
- Opciones con fondo oscuro
- Opción seleccionada con fondo cyan

### 9. Checkboxes y Radio Buttons

```html
<div class="form-check">
    <input type="checkbox" id="check1" class="form-check-input">
    <label for="check1" class="form-check-label">Opción de Checkbox</label>
</div>

<div class="form-check">
    <input type="radio" id="radio1" name="grupo" class="form-check-input">
    <label for="radio1" class="form-check-label">Opción de Radio</label>
</div>
```

**Características:**
- Color accent: Cyan (#06b6d4)
- Tamaño: 20x20px
- Animación smooth al seleccionar

### 10. Mensajes de Validación

```html
<!-- Campo con error -->
<input type="text" class="form-control is-invalid">
<div class="invalid-feedback">Mensaje de error</div>

<!-- Campo válido -->
<input type="text" class="form-control is-valid">
<div class="valid-feedback">Validación exitosa</div>

<!-- Texto de ayuda -->
<div class="form-text">Información adicional o instrucciones</div>
```

**Colores:**
- Error: #ef4444 (rojo)
- Éxito: #10b981 (verde)
- Información: rgba(255,255,255,0.55) (gris)

### 11. Botones

```html
<!-- Botón Primario (Guardar/Aceptar) -->
<button class="btn btn-primary">
    <i class="bi bi-check-circle"></i>
    Guardar
</button>

<!-- Botón Secundario (Cancelar) -->
<button class="btn btn-secondary">
    <i class="bi bi-x-circle"></i>
    Cancelar
</button>

<!-- Botón Peligro (Eliminar) -->
<button class="btn btn-danger">
    <i class="bi bi-trash"></i>
    Eliminar
</button>

<!-- Botón Éxito (Completar) -->
<button class="btn btn-success">
    <i class="bi bi-check"></i>
    Completar
</button>

<!-- Variantes de Tamaño -->
<button class="btn btn-primary btn-lg">Botón Grande</button>
<button class="btn btn-primary btn-sm">Botón Pequeño</button>

<!-- Botón a Ancho Completo -->
<button class="btn btn-primary btn-block">Ancho Completo</button>
```

**Características:**
- Gradientes profesionales
- Sombras dinámicas
- Animación hover (translateY)
- Transición smooth

### 12. Grupo de Entrada (Input Group)

```html
<div class="input-group">
    <input type="text" class="form-control" placeholder="Buscar...">
    <button class="btn btn-primary" type="button">Buscar</button>
</div>
```

### 13. Contenedor de Acciones

```html
<div class="form-actions">
    <button type="submit" class="btn btn-success btn-lg">Guardar</button>
    <a href="#" class="btn btn-secondary btn-lg">Cancelar</a>
</div>
```

**Características:**
- Alineación a la derecha
- Separación entre botones
- Responsive (apilados en móvil)

## 🎨 Paleta de Colores

| Elemento | Color | Uso |
|----------|-------|-----|
| Accent Principal | #06b6d4 (Cyan) | Bordes focus, iconos, botones primarios |
| Fondo Oscuro | #0a1628 | Fondo de inputs, selects |
| Texto Principal | #ffffff | Texto en inputs, labels |
| Texto Secundario | rgba(255,255,255,0.55) | Placeholder, help text |
| Error | #ef4444 (Rojo) | Validación negativa |
| Éxito | #10b981 (Verde) | Validación positiva |
| Borde | rgba(255,255,255,0.12) | Bordes de inputs |

## 📱 Responsividad

La grid de formularios es automáticamente responsiva:
- **Desktop:** 1-3 columnas según el contenedor
- **Tablet:** 1-2 columnas
- **Mobile:** 1 columna

Configuración:
```css
grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
```

En móviles, los botones de acción se apilan verticalmente.

## ✨ Efectos y Transiciones

- Duración: 0.3s
- Timing: ease
- Hover effects: Cambio de color, sombra, elevación (+2px)
- Focus effects: Sombra expandida, borde cyan

## 🔧 Personalización Avanzada

### Desabilitar un campo

```html
<input type="text" class="form-control" disabled>
```

### Campo solo lectura

```html
<input type="text" class="form-control" readonly>
```

### Campo requerido

```html
<input type="text" class="form-control" required>
<label class="required">Campo Requerido</label>
```

### Validación en HTML5

```html
<input type="email" class="form-control" required pattern="^[^\s@]+@[^\s@]+\.[^\s@]+$">
```

## 📖 Ejemplos Completos

### Formulario Simple de Inicio de Sesión

```html
<div class="form-container">
    <form method="post">
        <div class="form-group">
            <label for="email" class="required">Correo Electrónico</label>
            <input type="email" id="email" name="email" class="form-control" 
                   placeholder="usuario@ejemplo.com" required>
        </div>

        <div class="form-group">
            <label for="password" class="required">Contraseña</label>
            <input type="password" id="password" name="password" class="form-control" 
                   placeholder="••••••••" required>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-lg btn-block">
                <i class="bi bi-box-arrow-in-right"></i>
                Iniciar Sesión
            </button>
        </div>
    </form>
</div>
```

### Formulario Avanzado de Registro

```html
<div class="form-container">
    <div class="form-header">
        <i class="bi bi-person-plus"></i>
        <h3>Crear Nueva Cuenta</h3>
    </div>

    <form method="post" novalidate>
        <!-- Sección 1 -->
        <div class="form-section">
            <div class="form-header">
                <i class="bi bi-person"></i>
                <h3>Información Personal</h3>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="nombre" class="required">Nombre</label>
                    <input type="text" id="nombre" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="apellido" class="required">Apellido</label>
                    <input type="text" id="apellido" class="form-control" required>
                </div>
            </div>
        </div>

        <!-- Sección 2 -->
        <div class="form-section">
            <div class="form-header">
                <i class="bi bi-lock"></i>
                <h3>Credenciales de Acceso</h3>
            </div>

            <div class="form-group">
                <label for="email" class="required">Correo Electrónico</label>
                <input type="email" id="email" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="password" class="required">Contraseña</label>
                <input type="password" id="password" class="form-control" required>
                <div class="form-text">Mínimo 8 caracteres, incluir mayúscula y número</div>
            </div>
        </div>

        <!-- Botones -->
        <div class="form-actions">
            <button type="submit" class="btn btn-success btn-lg">
                <i class="bi bi-check-circle"></i>
                Registrarse
            </button>
            <a href="#" class="btn btn-secondary btn-lg">Cancelar</a>
        </div>
    </form>
</div>
```

## 📌 Notas Importantes

1. **Siempre** incluir `<label>` para cada input (accesibilidad)
2. **Siempre** usar `for` en labels y `id` en inputs
3. **Usar** `th:classappend` con Thymeleaf para validación
4. **Incluir** ícono + texto en botones de acción
5. **Agrupar** campos relacionados en `form-section`
6. **Responsivo** por defecto - no requiere media queries adicionales

---

**Versión:** 1.0  
**Última actualización:** 2024  
**Desarrollador:** Trinidad Médico - Team
