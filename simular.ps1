$baseUrl = "http://localhost:8080/trinidad/api/v1"
$headers = @{ "Content-Type" = "application/json" }

Write-Host "`n╔════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  SIMULACIÓN - TRINIDAD CITAS MÉDICAS           ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════╝" -ForegroundColor Cyan

# 1. ESPECIALIDADES
Write-Host "`n[1] ESPECIALIDADES..." -ForegroundColor Green
@(
    @{ nombre = "Cardiología"; precioConsulta = 250; duracionMinutos = 30; descripcion = "Corazón y sistema cardiovascular" },
    @{ nombre = "Dermatología"; precioConsulta = 150; duracionMinutos = 25; descripcion = "Piel y enfermedades dermatolóxicas" },
    @{ nombre = "Pediatría"; precioConsulta = 120; duracionMinutos = 20; descripcion = "Medicina infantil" }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/especialidades" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ $($_.nombre)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 2. PACIENTES
Write-Host "`n[2] PACIENTES..." -ForegroundColor Green
@(
    @{ dni = "10234567"; nombres = "Carlos"; apellidoPaterno = "García"; apellidoMaterno = "López"; fechaNacimiento = "1985-03-15"; sexo = "M"; telefono = "987654321"; email = "carlos@email.com"; tipoSangre = "O+"; alergias = "Penicilina" },
    @{ dni = "20345678"; nombres = "María"; apellidoPaterno = "Rodríguez"; apellidoMaterno = "Martínez"; fechaNacimiento = "1990-07-22"; sexo = "F"; telefono = "965432187"; email = "maria@email.com"; tipoSangre = "A+"; alergias = "Ninguna" },
    @{ dni = "30456789"; nombres = "Juan"; apellidoPaterno = "Pérez"; apellidoMaterno = "González"; fechaNacimiento = "1975-11-08"; sexo = "M"; telefono = "943210765"; email = "juan@email.com"; tipoSangre = "B+"; alergias = "Aspirina" },
    @{ dni = "40567890"; nombres = "Ana"; apellidoPaterno = "Ramírez"; apellidoMaterno = "Silva"; fechaNacimiento = "1988-05-19"; sexo = "F"; telefono = "932187654"; email = "ana@email.com"; tipoSangre = "AB+"; alergias = "Ninguna" }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/pacientes" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ $($_.nombres) $($_.apellidoPaterno)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 3. MÉDICOS
Write-Host "`n[3] MÉDICOS..." -ForegroundColor Green
@(
    @{ cmp = "CMP-001"; dni = "60789012"; nombres = "Roberto"; apellidoPaterno = "Carrasco"; apellidoMaterno = "Soto"; especialidadId = 1; usuarioId = 25; telefono = "971234567"; email = "robert@hospital.com"; consultorio = "201"; estado = $true },
    @{ cmp = "CMP-002"; dni = "70890123"; nombres = "Sandra"; apellidoPaterno = "Vega"; apellidoMaterno = "Cruz"; especialidadId = 2; usuarioId = 26; telefono = "961234567"; email = "sandra@hospital.com"; consultorio = "202"; estado = $true },
    @{ cmp = "CMP-003"; dni = "80901234"; nombres = "Pedro"; apellidoPaterno = "Flores"; apellidoMaterno = "Ruiz"; especialidadId = 3; usuarioId = 27; telefono = "951234567"; email = "pedro@hospital.com"; consultorio = "203"; estado = $true }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/medicos" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ $($_.nombres) $($_.apellidoPaterno) - CMP: $($_.cmp)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 4. HORARIOS
Write-Host "`n[4] HORARIOS..." -ForegroundColor Green
@(
    @{ medicoId = 1; diaSemana = "LUNES"; horaInicio = "08:00"; horaFin = "12:00"; numeroConsultorio = 201; estado = $true },
    @{ medicoId = 1; diaSemana = "MIERCOLES"; horaInicio = "14:00"; horaFin = "18:00"; numeroConsultorio = 201; estado = $true },
    @{ medicoId = 2; diaSemana = "MARTES"; horaInicio = "08:00"; horaFin = "12:00"; numeroConsultorio = 202; estado = $true },
    @{ medicoId = 3; diaSemana = "VIERNES"; horaInicio = "15:00"; horaFin = "19:00"; numeroConsultorio = 203; estado = $true }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/horarios" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ Médico $($_.medicoId) - $($_.diaSemana)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 5. CITAS
Write-Host "`n[5] CITAS..." -ForegroundColor Green
@(
    @{ pacienteId = 1; medicoId = 1; especialidadId = 1; fechaCita = "2024-12-20"; horaCita = "08:30"; motivo = "Evaluación cardíaca"; estado = "PROGRAMADA"; numeroSala = 201 },
    @{ pacienteId = 2; medicoId = 2; especialidadId = 2; fechaCita = "2024-12-21"; horaCita = "09:00"; motivo = "Consulta dermatológica"; estado = "PROGRAMADA"; numeroSala = 202 },
    @{ pacienteId = 3; medicoId = 3; especialidadId = 3; fechaCita = "2024-12-22"; horaCita = "10:00"; motivo = "Control pediátrico"; estado = "CONFIRMADA"; numeroSala = 203 },
    @{ pacienteId = 4; medicoId = 1; especialidadId = 1; fechaCita = "2024-12-23"; horaCita = "11:00"; motivo = "Seguimiento cardíaco"; estado = "ATENDIDA"; numeroSala = 201 }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/citas" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ Cita Paciente $($_.pacienteId) - $($_.fechaCita)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 6. ÓRDENES EXAMEN
Write-Host "`n[6] ÓRDENES DE EXAMEN..." -ForegroundColor Green
@(
    @{ pacienteId = 1; medicoId = 1; tipoExamen = "Electrocardiograma"; razonMedica = "Evaluación cardíaca"; fechaOrden = "2024-12-20"; urgencia = "NORMAL"; costo = 200 },
    @{ pacienteId = 2; medicoId = 2; tipoExamen = "Hemograma"; razonMedica = "Chequeo general"; fechaOrden = "2024-12-21"; urgencia = "NORMAL"; costo = 150 },
    @{ pacienteId = 3; medicoId = 3; tipoExamen = "Radiografía"; razonMedica = "Tamizaje"; fechaOrden = "2024-12-22"; urgencia = "NORMAL"; costo = 180 }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/ordenes-examen" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ Orden: $($_.tipoExamen) - S/. $($_.costo)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 7. PAGOS
Write-Host "`n[7] PAGOS..." -ForegroundColor Green
@(
    @{ citaId = 1; monto = 250; metodoPago = "EFECTIVO"; estado = "PAGADO"; comprobante = "REC-001"; fechaPago = "2024-12-20"; descripcion = "Consulta Cardiología" },
    @{ citaId = 2; monto = 150; metodoPago = "TARJETA"; estado = "PAGADO"; comprobante = "REC-002"; fechaPago = "2024-12-21"; descripcion = "Consulta Dermatología" },
    @{ citaId = 3; monto = 120; metodoPago = "TRANSFERENCIA"; estado = "PENDIENTE"; comprobante = "REC-003"; fechaPago = "2024-12-22"; descripcion = "Consulta Pediatría" }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/pagos" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ Pago S/. $($_.monto) - $($_.estado)" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

# 8. HISTORIA CLÍNICA
Write-Host "`n[8] HISTORIA CLÍNICA..." -ForegroundColor Green
@(
    @{ pacienteId = 1; antecedentes = "HTA"; enfermedadesActuales = "Hipertensión"; medicamentosActuales = "Enalapril"; vacunas = "COVID completo"; proximoControl = "2025-01-20" },
    @{ pacienteId = 2; antecedentes = "Dermatitis"; enfermedadesActuales = "Acné"; medicamentosActuales = "Ninguno"; vacunas = "COVID completo"; proximoControl = "2025-01-15" },
    @{ pacienteId = 3; antecedentes = "Ninguno"; enfermedadesActuales = "Sano"; medicamentosActuales = "Ninguno"; vacunas = "Completo"; proximoControl = "2025-06-22" },
    @{ pacienteId = 4; antecedentes = "Alergia penicilina"; enfermedadesActuales = "Ninguno"; medicamentosActuales = "Ninguno"; vacunas = "COVID completo"; proximoControl = "2025-01-25" }
) | ForEach-Object {
    try {
        $json = $_ | ConvertTo-Json
        $null = Invoke-WebRequest -Uri "$baseUrl/historia-clinica" -Method POST -Headers $headers -Body $json -UseBasicParsing
        Write-Host "  ✓ Paciente $($_.pacienteId) registrado" -ForegroundColor Yellow
    } catch {
        Write-Host "  ✗ Error" -ForegroundColor Red
    }
}

Write-Host "`n╔════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║  ✓ SIMULACIÓN COMPLETADA                       ║" -ForegroundColor Green
Write-Host "║  URL: http://localhost:8080/trinidad           ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
