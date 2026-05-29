# TEST INTEGRAL DE MÓDULOS TRINIDAD

Write-Host "═══════════════════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "TEST INTEGRAL: MÓDULOS DEL SISTEMA TRINIDAD - CITAS MÉDICAS" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Definir base URL
$base_url = "http://localhost:8080/trinidad"
$api_base = "http://localhost:8080/trinidad/api"

# Función auxiliar para hacer requests
function Test-Endpoint {
    param([string]$method, [string]$url, [string]$name, [object]$body = $null)
    
    Write-Host "├─ [$method] $name" -ForegroundColor Yellow
    try {
        if ($method -eq "POST" -and $body) {
            $response = Invoke-WebRequest -Uri $url -Method $method -ContentType "application/json" `
                -Body ($body | ConvertTo-Json) -SkipHttpErrorCheck -TimeoutSec 5
        } else {
            $response = Invoke-WebRequest -Uri $url -Method $method -SkipHttpErrorCheck -TimeoutSec 5
        }
        $status = $response.StatusCode
        if ($status -eq 200 -or $status -eq 201 -or $status -eq 302) {
            Write-Host "   ✓ Status: $status - OK" -ForegroundColor Green
            return $true
        } else {
            Write-Host "   ⚠ Status: $status - WARNING" -ForegroundColor Yellow
            return $false
        }
    } catch {
        Write-Host "   ✗ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# 1. AUTENTICACIÓN
Write-Host "[1] MODULO: AUTENTICACION" -ForegroundColor Cyan
Write-Host "├─ Testing login flow..."
Test-Endpoint "GET" "$base_url/login" "GET /login"
$loginBody = @{ username = "admin"; password = "admin123" }
Test-Endpoint "POST" "$api_base/auth/login" "POST /api/auth/login (API REST)" $loginBody
Write-Host ""

# 2. PÁGINA PRINCIPAL
Write-Host "[2] MODULO: PAGINA PRINCIPAL" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/" "GET / (Home)"
Write-Host ""

# 3. PACIENTES (WEB)
Write-Host "[3] MODULO: GESTION DE PACIENTES (WEB)" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/pacientes" "GET /pacientes (Lista)"
Test-Endpoint "GET" "$base_url/pacientes/nuevo" "GET /pacientes/nuevo (Formulario)"
Write-Host ""

# 4. MÉDICOS (WEB)
Write-Host "[4] MODULO: GESTION DE MEDICOS (WEB)" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/medicos" "GET /medicos (Lista)"
Write-Host ""

# 5. CITAS (WEB)
Write-Host "[5] MODULO: GESTION DE CITAS (WEB)" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/citas" "GET /citas (Lista)"
Test-Endpoint "GET" "$base_url/citas/agendar" "GET /citas/agendar (Formulario)"
Write-Host ""

# 6. DASHBOARD
Write-Host "[6] MODULO: DASHBOARD" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/dashboard" "GET /dashboard"
Write-Host ""

# 7. MÓDULOS ADMINISTRATIVOS
Write-Host "[7] MODULO: ADMINISTRACION" -ForegroundColor Cyan
Test-Endpoint "GET" "$base_url/usuarios" "GET /usuarios (Gestion de usuarios)"
Test-Endpoint "GET" "$base_url/roles" "GET /roles (Gestion de roles)"
Test-Endpoint "GET" "$base_url/auditoria" "GET /auditoria (Logs de auditoria)"
Test-Endpoint "GET" "$base_url/atenciones" "GET /atenciones (Gestion de atenciones)"
Test-Endpoint "GET" "$base_url/recetas" "GET /recetas (Gestion de recetas)"
Test-Endpoint "GET" "$base_url/reportes" "GET /reportes (Reportes)"
Test-Endpoint "GET" "$base_url/configuracion" "GET /configuracion (Configuracion)"
Write-Host ""

# 8. API REST
Write-Host "[8] MODULO: API REST" -ForegroundColor Cyan
Test-Endpoint "GET" "$api_base/v1/citas" "GET /api/v1/citas (Listar citas)"
Test-Endpoint "GET" "$api_base/v1/pacientes" "GET /api/v1/pacientes (Listar pacientes)"
Test-Endpoint "GET" "$api_base/v1/especialidades" "GET /api/v1/especialidades (Listar especialidades)"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "FIN DEL TEST INTEGRAL" -ForegroundColor Green
Write-Host "═══════════════════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
