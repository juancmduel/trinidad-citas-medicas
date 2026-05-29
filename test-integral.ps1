$BASE = "http://localhost:8080/trinidad"

# 1. AUTH
$r = Invoke-WebRequest -Uri "$BASE/api/auth/login" -Method Post -Body '{"username":"admin","password":"admin123"}' -ContentType "application/json" -UseBasicParsing 2>$null
$TOKEN = ($r.Content | ConvertFrom-Json).token
$H = @{ Authorization = "Bearer $TOKEN" }
Write-Host "[AUTH] Login: $($r.StatusCode)" -ForegroundColor $(if($r.StatusCode -lt 300){"Green"}else{"Red"})

function T($l, $u) {
    $res = Invoke-WebRequest -Uri "$BASE$u" -Headers $H -UseBasicParsing 2>$null
    $b = $res.Content | ConvertFrom-Json -ErrorAction SilentlyContinue
    $c = if ($b -is [array]) { "$($b.Count) items" } elseif ($b) { "1 reg" } else { "-" }
    $color = if ($res.StatusCode -lt 300) { "Green" } else { "Red" }
    Write-Host "[$($res.StatusCode)] $l - $c" -ForegroundColor $color
}

function POST($l, $u, $body) {
    $res = Invoke-WebRequest -Uri "$BASE$u" -Method Post -Headers $H -Body ($body | ConvertTo-Json) -ContentType "application/json" -UseBasicParsing 2>$null
    $color = if ($res.StatusCode -lt 300) { "Green" } else { "Red" }
    Write-Host "[$($res.StatusCode)] POST $l" -ForegroundColor $color
    return if ($res.StatusCode -lt 300) { $res.Content | ConvertFrom-Json } else { $null }
}

function DEL($l, $u) {
    $res = Invoke-WebRequest -Uri "$BASE$u" -Method Delete -Headers $H -UseBasicParsing 2>$null
    $color = if ($res.StatusCode -lt 300) { "Green" } else { "Red" }
    Write-Host "[$($res.StatusCode)] DELETE $l" -ForegroundColor $color
}

Write-Host "`n=== ESPECIALIDADES ===" -ForegroundColor Cyan
T "GET /especialidades"   "/api/v1/especialidades"
T "GET /especialidades/1" "/api/v1/especialidades/1"
$esp = POST "Especialidades" "/api/v1/especialidades" @{nombre="TEST-ESP";descripcion="prueba";precioConsulta=50;duracionMinutos=30;activo=1}
if ($esp) { DEL "Especialidades/$($esp.idEspecialidad)" "/api/v1/especialidades/$($esp.idEspecialidad)" }

Write-Host "`n=== MÉDICOS ===" -ForegroundColor Cyan
T "GET /medicos"              "/api/v1/medicos"
T "GET /medicos/1"            "/api/v1/medicos/1"
T "GET /medicos/especialidad/1" "/api/v1/medicos/especialidad/1"

Write-Host "`n=== PACIENTES ===" -ForegroundColor Cyan
T "GET /pacientes"   "/api/v1/pacientes"
T "GET /pacientes/1" "/api/v1/pacientes/1"

Write-Host "`n=== CITAS ===" -ForegroundColor Cyan
T "GET /citas"                      "/api/v1/citas"
T "GET /citas/hoy"                  "/api/v1/citas/hoy"
T "GET /citas/estado/PROGRAMADA"    "/api/v1/citas/estado/PROGRAMADA"
T "GET /citas/medico/1"             "/api/v1/citas/medico/1"
T "GET /citas/paciente/1"           "/api/v1/citas/paciente/1"

Write-Host "`n=== HORARIOS MÉDICOS ===" -ForegroundColor Cyan
T "GET /horarios"           "/api/v1/horarios"
T "GET /horarios/medico/1"  "/api/v1/horarios/medico/1"
$hor = POST "Horarios" "/api/v1/horarios" @{idMedico=1;diaSemana=2;horaInicio="08:00";horaFin="16:00";activo=1}
if ($hor) { DEL "Horarios/$($hor.idHorario)" "/api/v1/horarios/$($hor.idHorario)" }

Write-Host "`n=== HISTORIA CLÍNICA ===" -ForegroundColor Cyan
T "GET /historias"            "/api/v1/historias"
T "GET /historias/paciente/1" "/api/v1/historias/paciente/1"

Write-Host "`n=== ATENCIONES ===" -ForegroundColor Cyan
T "GET /atenciones" "/api/v1/atenciones"

Write-Host "`n=== ÓRDENES EXAMEN ===" -ForegroundColor Cyan
T "GET /ordenes" "/api/v1/ordenes"

Write-Host "`n=== RECETAS ===" -ForegroundColor Cyan
T "GET /recetas" "/api/v1/recetas"

Write-Host "`n=== DETALLE RECETAS ===" -ForegroundColor Cyan
T "GET /detalles-receta" "/api/v1/detalles-receta"

Write-Host "`n=== PAGOS ===" -ForegroundColor Cyan
T "GET /pagos"                      "/api/v1/pagos"
T "GET /pagos/estado/PENDIENTE"     "/api/v1/pagos/estado/PENDIENTE"

Write-Host "`n=== DIAGNÓSTICOS CIE10 ===" -ForegroundColor Cyan
T "GET /diagnosticos"      "/api/v1/diagnosticos"
T "GET /diagnosticos/A00"  "/api/v1/diagnosticos/A00"

Write-Host "`n=== ROLES ===" -ForegroundColor Cyan
T "GET /roles"   "/api/v1/roles"
T "GET /roles/1" "/api/v1/roles/1"
$rol = POST "Roles" "/api/v1/roles" @{nombre="TEST-ROL-INTEGRAL";descripcion="prueba";activo=1}
if ($rol) { DEL "Roles/$($rol.idRol)" "/api/v1/roles/$($rol.idRol)" }

Write-Host "`n=== USUARIOS ===" -ForegroundColor Cyan
T "GET /usuarios"               "/api/v1/usuarios"
T "GET /usuarios/activos"       "/api/v1/usuarios/activos"
T "GET /usuarios/username/admin" "/api/v1/usuarios/username/admin"

Write-Host "`n=== AUDITORÍA ===" -ForegroundColor Cyan
T "GET /auditoria" "/api/v1/auditoria"

Write-Host "`n=== REPORTES/KPI ===" -ForegroundColor Cyan
T "GET /reportes/kpi" "/api/v1/reportes/kpi"

Write-Host "`n=== FIN DE PRUEBAS INTEGRALES ===" -ForegroundColor Yellow
