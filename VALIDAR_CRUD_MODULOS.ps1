# Test automatizado de CRUD en todos los modulos Trinidad

param([switch]$Quick = $false)

$base = "http://localhost:8081/trinidad"
$api = "$base/api/v1"
$results = @()

function Get-JWTToken {
    try {
        $resp = Invoke-RestMethod -Uri "$base/api/auth/login" -Method Post `
            -ContentType 'application/json' `
            -Body '{"username":"admin","password":"admin123"}' `
            -TimeoutSec 5
        return $resp.token
    } catch {
        Write-Host "[ERROR] No se pudo obtener token JWT" -ForegroundColor Red
        exit 1
    }
}

function Test-CRUD {
    param(
        [string]$modulo,
        [string]$endpoint,
        [hashtable]$createData,
        [hashtable]$updateData,
        [string]$token
    )
    
    $headers = @{ Authorization = "Bearer $token" }
    $status = "PASS"
    $details = @()
    
    try {
        # CREATE
        $createJson = $createData | ConvertTo-Json -Depth 5
        $created = Invoke-RestMethod -Uri "$api$endpoint" -Method Post `
            -Headers $headers -ContentType 'application/json' `
            -Body $createJson -TimeoutSec 10
        
        $id = $created.id -or $created.idPaciente -or $created.idMedico -or $created.idCita -or $created.idEspecialidad
        if (-not $id) {
            $status = "FAIL"
            $details += "CREATE: No se retorno ID"
        } else {
            $details += "CREATE(id=$id)"
        }
        
        # READ
        $read = Invoke-RestMethod -Uri "$api$endpoint/$id" -Method Get `
            -Headers $headers -TimeoutSec 10
        $details += "READ(OK)"
        
        # UPDATE
        if ($updateData) {
            $updateJson = $updateData | ConvertTo-Json -Depth 5
            $updated = Invoke-RestMethod -Uri "$api$endpoint/$id" -Method Put `
                -Headers $headers -ContentType 'application/json' `
                -Body $updateJson -TimeoutSec 10
            $details += "UPDATE(OK)"
        }
        
        $details += "DELETE(SKIP)"
        
    } catch {
        $status = "ERROR"
        $msg = $_.Exception.Message
        if ($msg.Length -gt 40) { $msg = $msg.Substring(0,40) }
        $details += $msg
    }
    
    $result = [PSCustomObject]@{
        Modulo = $modulo
        Status = $status
        Details = ($details -join " | ")
    }
    
    $color = if ($status -eq "PASS") { "Green" } else { "Red" }
    Write-Host "[RESULT] $status - $modulo : $($details -join ' | ')" -ForegroundColor $color
    return $result
}

Write-Host "`n========== VALIDACION AUTOMATIZADA DE CRUD ==========" -ForegroundColor Cyan
Write-Host "Fecha: $(Get-Date -Format 'dd/MM/yyyy HH:mm:ss')" -ForegroundColor Cyan

Write-Host "`nObteniendo token JWT..." -ForegroundColor Yellow
$token = Get-JWTToken
Write-Host "Token obtenido OK" -ForegroundColor Green

Write-Host "`n====================================================`n" -ForegroundColor Cyan

# [1] PACIENTES
Write-Host "[1/8] MODULO PACIENTES" -ForegroundColor Yellow
$pacienteCreate = @{
    dni = "99999999"
    nombres = "TEST CRUD"
    apellidoPaterno = "VALIDACION"
    apellidoMaterno = "SISTEMA"
    fechaNacimiento = "1990-05-24"
    sexo = "M"
    telefono = "987654321"
    email = "test.crud@mail.com"
    direccion = "Av Test 123"
    distrito = "Tarapoto"
    tipoSangre = "O+"
    alergias = "Ninguna"
}
$pacienteUpdate = @{
    dni = "99999999"
    nombres = "TEST CRUD ACTUALIZADO"
    apellidoPaterno = "VALIDACION"
    apellidoMaterno = "MODIFICADO"
    fechaNacimiento = "1990-05-24"
    sexo = "M"
    telefono = "987654322"
    email = "test.crud@mail.com"
    direccion = "Av Test 124"
    distrito = "Tarapoto"
    tipoSangre = "A+"
    alergias = "Actualizado"
}
$results += Test-CRUD -modulo "Pacientes" -endpoint "/pacientes" `
    -createData $pacienteCreate -updateData $pacienteUpdate -token $token

# [2] ESPECIALIDADES
Write-Host "`n[2/8] MODULO ESPECIALIDADES" -ForegroundColor Yellow
$especCreate = @{
    nombre = "TEST ESPECIALIDAD"
    descripcion = "Especialidad de prueba CRUD"
    costoConsulta = 150.00
    duracionMinutos = 25
}
$especUpdate = @{
    nombre = "TEST ESPECIALIDAD ACTUALIZADA"
    descripcion = "Descripcion modificada"
    costoConsulta = 160.00
    duracionMinutos = 30
}
$results += Test-CRUD -modulo "Especialidades" -endpoint "/especialidades" `
    -createData $especCreate -updateData $especUpdate -token $token

# [3] MEDICOS
Write-Host "`n[3/8] MODULO MEDICOS" -ForegroundColor Yellow
$medicoCreate = @{
    nombres = "DR. TEST"
    apellidoPaterno = "VALIDACION"
    apellidoMaterno = "CRUD"
    cmp = "12345678"
    idEspecialidad = 1
    telefono = "999888777"
    email = "dr.test@hospital.com"
    consultorio = "C-999"
}
$medicoUpdate = @{
    nombres = "DR. TEST ACTUALIZADO"
    apellidoPaterno = "VALIDACION"
    apellidoMaterno = "MODIFICADO"
    cmp = "12345678"
    idEspecialidad = 1
    telefono = "999888778"
    email = "dr.test@hospital.com"
    consultorio = "C-998"
}
$results += Test-CRUD -modulo "Medicos" -endpoint "/medicos" `
    -createData $medicoCreate -updateData $medicoUpdate -token $token

# [4] CITAS
Write-Host "`n[4/8] MODULO CITAS" -ForegroundColor Yellow
$citaCreate = @{
    idPaciente = 1
    idMedico = 1
    idEspecialidad = 1
    fechaCita = (Get-Date).AddDays(5).ToString("yyyy-MM-dd")
    horaInicio = "10:00"
    motivo = "Consulta de prueba CRUD"
    estado = "PROGRAMADA"
}
$citaUpdate = @{
    idPaciente = 1
    idMedico = 1
    idEspecialidad = 1
    fechaCita = (Get-Date).AddDays(6).ToString("yyyy-MM-dd")
    horaInicio = "11:00"
    motivo = "Consulta de prueba CRUD - ACTUALIZADA"
    estado = "CONFIRMADA"
}
$results += Test-CRUD -modulo "Citas" -endpoint "/citas" `
    -createData $citaCreate -updateData $citaUpdate -token $token

# [5] ROLES
Write-Host "`n[5/8] MODULO ROLES" -ForegroundColor Yellow
$roleCreate = @{
    nombre = "TEST_ROLE_CRUD"
    descripcion = "Rol de prueba para validacion CRUD"
}
$roleUpdate = @{
    nombre = "TEST_ROLE_CRUD"
    descripcion = "Rol de prueba - ACTUALIZADO"
}
$results += Test-CRUD -modulo "Roles" -endpoint "/roles" `
    -createData $roleCreate -updateData $roleUpdate -token $token

# [6] USUARIOS
Write-Host "`n[6/8] MODULO USUARIOS" -ForegroundColor Yellow
$rnd = [Math]::Floor([Math]::Random() * 9999)
$usuarioCreate = @{
    username = "testcrud$rnd"
    email = "testcrud$rnd@test.com"
    password = "TestCRUD2026"
    nombres = "Usuario Test"
    apellidoPaterno = "CRUD"
    apellidoMaterno = "Validacion"
    idRol = 5
}
$usuarioUpdate = @{
    username = "testcrud$rnd"
    email = "testcrud$rnd@test.com"
    password = "TestCRUD2026"
    nombres = "Usuario Test Actualizado"
    apellidoPaterno = "CRUD"
    apellidoMaterno = "Modificado"
    idRol = 5
}
$results += Test-CRUD -modulo "Usuarios" -endpoint "/usuarios" `
    -createData $usuarioCreate -updateData $usuarioUpdate -token $token

# [7] RECETAS
Write-Host "`n[7/8] MODULO RECETAS" -ForegroundColor Yellow
$recetaCreate = @{
    idCita = 1
    observaciones = "Receta de prueba CRUD"
    fechaEmision = (Get-Date).ToString("yyyy-MM-dd")
}
$recetaUpdate = @{
    idCita = 1
    observaciones = "Receta de prueba CRUD - ACTUALIZADA"
    fechaEmision = (Get-Date).ToString("yyyy-MM-dd")
}
$results += Test-CRUD -modulo "Recetas" -endpoint "/recetas" `
    -createData $recetaCreate -updateData $recetaUpdate -token $token

# [8] ATENCIONES
Write-Host "`n[8/8] MODULO ATENCIONES" -ForegroundColor Yellow
$atencionCreate = @{
    idCita = 1
    presion = "120/80"
    temperatura = "37.0"
    peso = "75.5"
    diagnostico = "Diagnostico de prueba CRUD"
    observaciones = "Observaciones de prueba"
}
$atencionUpdate = @{
    idCita = 1
    presion = "118/78"
    temperatura = "36.8"
    peso = "75.3"
    diagnostico = "Diagnostico de prueba CRUD - ACTUALIZADO"
    observaciones = "Observaciones actualizadas"
}
$results += Test-CRUD -modulo "Atenciones" -endpoint "/atenciones" `
    -createData $atencionCreate -updateData $atencionUpdate -token $token

# RESUMEN
Write-Host "`n====================================================`n" -ForegroundColor Cyan

$passed = ($results | Where-Object { $_.Status -eq "PASS" }).Count
$failed = ($results | Where-Object { $_.Status -ne "PASS" }).Count
$total = $results.Count

Write-Host "RESULTADO FINAL" -ForegroundColor Cyan
Write-Host "EXITOSOS: $passed / $total" -ForegroundColor Green
if ($failed -gt 0) {
    Write-Host "ERRORES:  $failed / $total" -ForegroundColor Red
}

Write-Host "`nDETALLES:`n"
foreach ($r in $results) {
    $color = if ($r.Status -eq "PASS") { "Green" } else { "Red" }
    Write-Host "  $($r.Status.PadRight(6)) | $($r.Modulo.PadRight(18)) | $($r.Details)" -ForegroundColor $color
}

Write-Host "`n====================================================`n"
