$baseUrl = "http://localhost:8081/trinidad"
$results = @()

function Test-URL {
    param([string]$nombre, [string]$url)
    try {
        $r = Invoke-WebRequest -Uri $url -Method GET -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
        Write-Host "[PASS] $nombre - Status: $($r.StatusCode)" -ForegroundColor Green
        return $true
    }
    catch {
        $code = $_.Exception.Response.StatusCode
        Write-Host "[FAIL] $nombre - Status: $code" -ForegroundColor Red
        return $false
    }
}

Write-Host "`n===== PRUEBAS FUNCIONALES =====`n"

$pass = 0
$fail = 0

# PACIENTES
if (Test-URL "Pacientes - Lista" "$baseUrl/pacientes") { $pass++ } else { $fail++ }
if (Test-URL "Pacientes - Nuevo" "$baseUrl/pacientes/nuevo") { $pass++ } else { $fail++ }

# MEDICOS
if (Test-URL "Medicos - Lista" "$baseUrl/medicos") { $pass++ } else { $fail++ }
if (Test-URL "Medicos - Nuevo" "$baseUrl/medicos/nuevo") { $pass++ } else { $fail++ }

# CITAS
if (Test-URL "Citas - Lista" "$baseUrl/citas") { $pass++ } else { $fail++ }
if (Test-URL "Citas - Agendar" "$baseUrl/citas/agendar") { $pass++ } else { $fail++ }

# ESPECIALIDADES
if (Test-URL "Especialidades - Lista" "$baseUrl/especialidades") { $pass++ } else { $fail++ }
if (Test-URL "Especialidades - Nuevo" "$baseUrl/especialidades/nuevo") { $pass++ } else { $fail++ }

# ATENCIONES
if (Test-URL "Atenciones - Lista" "$baseUrl/atenciones") { $pass++ } else { $fail++ }
if (Test-URL "Atenciones - Nuevo" "$baseUrl/atenciones/nuevo") { $pass++ } else { $fail++ }

# RECETAS
if (Test-URL "Recetas - Lista" "$baseUrl/recetas") { $pass++ } else { $fail++ }

# DIAGNOSTICOS
if (Test-URL "Diagnosticos" "$baseUrl/diagnosticos") { $pass++ } else { $fail++ }

# HORARIOS
if (Test-URL "Horarios" "$baseUrl/horarios") { $pass++ } else { $fail++ }

# HISTORIA CLINICA
if (Test-URL "Historia Clinica" "$baseUrl/historia-clinica") { $pass++ } else { $fail++ }

# DASHBOARD
if (Test-URL "Dashboard" "$baseUrl/dashboard") { $pass++ } else { $fail++ }

# REPORTES
if (Test-URL "Reportes" "$baseUrl/reportes") { $pass++ } else { $fail++ }

# USUARIOS
if (Test-URL "Usuarios - Lista" "$baseUrl/usuarios") { $pass++ } else { $fail++ }
if (Test-URL "Usuarios - Nuevo" "$baseUrl/usuarios/nuevo") { $pass++ } else { $fail++ }

# ROLES
if (Test-URL "Roles - Lista" "$baseUrl/roles") { $pass++ } else { $fail++ }

Write-Host "`n===== RESUMEN =====" -ForegroundColor Cyan
Write-Host "PASARON: $pass" -ForegroundColor Green
Write-Host "FALLARON: $fail" -ForegroundColor Red
Write-Host "TOTAL: $($pass + $fail)"
