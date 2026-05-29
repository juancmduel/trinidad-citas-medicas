# ============================================================================
# PRUEBAS COMPLETAS DE TODOS LOS MODULOS - TRINIDAD CITAS MEDICAS
# ============================================================================

$baseUrl = "http://localhost:8081/trinidad"
$results = @()

function Test-Endpoint {
    param(
        [string]$nombre,
        [string]$url,
        [string]$metodo = "GET"
    )
    
    try {
        $response = Invoke-WebRequest -Uri $url -Method $metodo -ErrorAction Stop -UseBasicParsing -TimeoutSec 5
        $status = "[OK]"
        $code = $response.StatusCode
    }
    catch {
        $status = "[ERROR]"
        $code = $_.Exception.Response.StatusCode
        if ($null -eq $code) { $code = "No conecta" }
    }
    
    $result = [PSCustomObject]@{
        Modulo = $nombre
        URL = $url
        Metodo = $metodo
        Status = $status
        Code = $code
    }
    
    $results += $result
    $color = if ($status -eq "[OK]") { "Green" } else { "Red" }
    Write-Host "$status - $nombre ($code)" -ForegroundColor $color
    
    return $code -eq 200 -or $code -eq 302 -or $code -eq 301
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  PRUEBAS FUNCIONALES TRINIDAD" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# ========== PACIENTES ==========
Write-Host "`n[PACIENTES]" -ForegroundColor Yellow
Test-Endpoint "GET /pacientes (lista)" "$baseUrl/pacientes"
Test-Endpoint "GET /pacientes/nuevo (form crear)" "$baseUrl/pacientes/nuevo"

# ========== MEDICOS ==========
Write-Host "`n[MEDICOS]" -ForegroundColor Yellow
Test-Endpoint "GET /medicos (lista)" "$baseUrl/medicos"
Test-Endpoint "GET /medicos/nuevo (form crear)" "$baseUrl/medicos/nuevo"

# ========== CITAS ==========
Write-Host "`n[CITAS]" -ForegroundColor Yellow
Test-Endpoint "GET /citas (lista)" "$baseUrl/citas"
Test-Endpoint "GET /citas/agendar" "$baseUrl/citas/agendar"

# ========== ESPECIALIDADES ==========
Write-Host "`n[ESPECIALIDADES]" -ForegroundColor Yellow
Test-Endpoint "GET /especialidades (lista)" "$baseUrl/especialidades"
Test-Endpoint "GET /especialidades/nuevo" "$baseUrl/especialidades/nuevo"

# ========== ATENCIONES ==========
Write-Host "`n[ATENCIONES]" -ForegroundColor Yellow
Test-Endpoint "GET /atenciones (lista)" "$baseUrl/atenciones"
Test-Endpoint "GET /atenciones/nuevo" "$baseUrl/atenciones/nuevo"

# ========== RECETAS ==========
Write-Host "`n[RECETAS]" -ForegroundColor Yellow
Test-Endpoint "GET /recetas (lista)" "$baseUrl/recetas"

# ========== DIAGNOSTICOS ==========
Write-Host "`n[DIAGNOSTICOS]" -ForegroundColor Yellow
Test-Endpoint "GET /diagnosticos" "$baseUrl/diagnosticos"

# ========== HORARIOS ==========
Write-Host "`n[HORARIOS]" -ForegroundColor Yellow
Test-Endpoint "GET /horarios" "$baseUrl/horarios"

# ========== HISTORIA CLINICA ==========
Write-Host "`n[HISTORIA CLINICA]" -ForegroundColor Yellow
Test-Endpoint "GET /historias" "$baseUrl/historias"

# ========== DASHBOARD ==========
Write-Host "`n[DASHBOARD]" -ForegroundColor Yellow
Test-Endpoint "GET /dashboard" "$baseUrl/dashboard"

# ========== REPORTE ==========
Write-Host "`n[REPORTES]" -ForegroundColor Yellow
Test-Endpoint "GET /reportes" "$baseUrl/reportes"

# ========== USUARIOS ==========
Write-Host "`n[USUARIOS]" -ForegroundColor Yellow
Test-Endpoint "GET /usuarios" "$baseUrl/usuarios"

# ========== ROLES ==========
Write-Host "`n[ROLES]" -ForegroundColor Yellow
Test-Endpoint "GET /roles" "$baseUrl/roles"

# ========== RESUMEN ==========
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE RESULTADOS" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$exitosos = ($results | Where-Object { $_.Status -eq "[OK]" }).Count
$errores = ($results | Where-Object { $_.Status -eq "[ERROR]" }).Count
$total = $results.Count

Write-Host "Exitosos: $exitosos" -ForegroundColor Green
Write-Host "Errores:  $errores" -ForegroundColor Red
Write-Host "Total:    $total"

if ($errores -gt 0) {
    Write-Host "`n[ENDPOINTS CON ERROR]" -ForegroundColor Red
    $results | Where-Object { $_.Status -eq "[ERROR]" } | ForEach-Object {
        Write-Host "  ERROR - $($_.Modulo) - $($_.URL) (Code: $($_.Code))"
    }
}
