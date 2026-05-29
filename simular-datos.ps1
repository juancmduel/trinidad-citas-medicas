$baseUrl = "http://localhost:8080/trinidad/api/v1"
$headers = @{ "Content-Type" = "application/json" }

Write-Host "`n════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  SIMULACIÓN - TRINIDAD CITAS MÉDICAS" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════`n" -ForegroundColor Cyan

# Autenticar
Write-Host "[AUTH] Obteniendo token..." -ForegroundColor Yellow
$loginBody = @{ username = "admin"; password = "admin123" } | ConvertTo-Json
$loginResp = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Headers $headers -Body $loginBody -UseBasicParsing
$token = ($loginResp.Content | ConvertFrom-Json).token
$authHeaders = @{ "Content-Type" = "application/json"; "Authorization" = "Bearer $token" }
Write-Host "✓ Autenticado`n" -ForegroundColor Green

# 1. ESPECIALIDADES
Write-Host "[1] Creando especialidades..." -ForegroundColor Cyan
$esp1 = @{ nombre = "Cardiología"; precioConsulta = 250; duracionMinutos = 30; descripcion = "Corazón y sistema cardiovascular"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/especialidades" -Method POST -Headers $authHeaders -Body $esp1 -UseBasicParsing
Write-Host "  ✓ Cardiología (S/. 250)" -ForegroundColor Green

$esp2 = @{ nombre = "Dermatología"; precioConsulta = 150; duracionMinutos = 25; descripcion = "Piel y enfermedades"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/especialidades" -Method POST -Headers $authHeaders -Body $esp2 -UseBasicParsing
Write-Host "  ✓ Dermatología (S/. 150)" -ForegroundColor Green

$esp3 = @{ nombre = "Pediatría"; precioConsulta = 120; duracionMinutos = 20; descripcion = "Medicina infantil"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/especialidades" -Method POST -Headers $authHeaders -Body $esp3 -UseBasicParsing
Write-Host "  ✓ Pediatría (S/. 120)`n" -ForegroundColor Green

# 2. PACIENTES
Write-Host "[2] Creando pacientes..." -ForegroundColor Cyan
$pac1 = @{ dni = "10234567"; nombres = "Carlos"; apellidoPaterno = "García"; apellidoMaterno = "López"; fechaNacimiento = "1985-03-15"; sexo = "M"; telefono = "987654321"; email = "carlos@email.com"; tipoSangre = "O+"; alergias = "Penicilina"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/pacientes" -Method POST -Headers $authHeaders -Body $pac1 -UseBasicParsing
Write-Host "  ✓ Carlos García López - DNI: 10234567" -ForegroundColor Green

$pac2 = @{ dni = "20345678"; nombres = "María"; apellidoPaterno = "Rodríguez"; apellidoMaterno = "Martínez"; fechaNacimiento = "1990-07-22"; sexo = "F"; telefono = "965432187"; email = "maria@email.com"; tipoSangre = "A+"; alergias = "Ninguna"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/pacientes" -Method POST -Headers $authHeaders -Body $pac2 -UseBasicParsing
Write-Host "  ✓ María Rodríguez Martínez - DNI: 20345678" -ForegroundColor Green

$pac3 = @{ dni = "30456789"; nombres = "Juan"; apellidoPaterno = "Pérez"; apellidoMaterno = "González"; fechaNacimiento = "1975-11-08"; sexo = "M"; telefono = "943210765"; email = "juan@email.com"; tipoSangre = "B+"; alergias = "Aspirina"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/pacientes" -Method POST -Headers $authHeaders -Body $pac3 -UseBasicParsing
Write-Host "  ✓ Juan Pérez González - DNI: 30456789`n" -ForegroundColor Green

# 3. MÉDICOS
Write-Host "[3] Creando médicos..." -ForegroundColor Cyan
$med1 = @{ cmp = "CMP-001"; dni = "60789012"; nombres = "Roberto"; apellidoPaterno = "Carrasco"; apellidoMaterno = "Soto"; especialidadId = 1; usuarioId = 25; telefono = "971234567"; email = "roberto@hospital.com"; consultorio = "201"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/medicos" -Method POST -Headers $authHeaders -Body $med1 -UseBasicParsing
Write-Host "  ✓ Dr. Roberto Carrasco Soto - CMP-001" -ForegroundColor Green

$med2 = @{ cmp = "CMP-002"; dni = "70890123"; nombres = "Sandra"; apellidoPaterno = "Vega"; apellidoMaterno = "Cruz"; especialidadId = 2; usuarioId = 26; telefono = "961234567"; email = "sandra@hospital.com"; consultorio = "202"; activo = 1 } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/medicos" -Method POST -Headers $authHeaders -Body $med2 -UseBasicParsing
Write-Host "  ✓ Dra. Sandra Vega Cruz - CMP-002`n" -ForegroundColor Green

# 4. CITAS
Write-Host "[4] Creando citas..." -ForegroundColor Cyan
$tomorrow = (Get-Date).AddDays(1).ToString("yyyy-MM-dd")
$cita1 = @{ pacienteId = 1; medicoId = 1; especialidadId = 1; fechaCita = $tomorrow; horaInicio = "08:30"; horaFin = "09:00"; motivoConsulta = "Evaluación cardíaca"; estado = "PROGRAMADA"; canalReserva = "WEB" } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/citas" -Method POST -Headers $authHeaders -Body $cita1 -UseBasicParsing
Write-Host "  ✓ Cita Carlos García - Cardiología - $tomorrow" -ForegroundColor Green

$tomorrow2 = (Get-Date).AddDays(2).ToString("yyyy-MM-dd")
$cita2 = @{ pacienteId = 2; medicoId = 2; especialidadId = 2; fechaCita = $tomorrow2; horaInicio = "09:00"; horaFin = "09:30"; motivoConsulta = "Consulta dermatológica"; estado = "PROGRAMADA"; canalReserva = "WEB" } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/citas" -Method POST -Headers $authHeaders -Body $cita2 -UseBasicParsing
Write-Host "  ✓ Cita María Rodríguez - Dermatología - $tomorrow2" -ForegroundColor Green

$tomorrow3 = (Get-Date).AddDays(3).ToString("yyyy-MM-dd")
$cita3 = @{ pacienteId = 3; medicoId = 1; especialidadId = 1; fechaCita = $tomorrow3; horaInicio = "10:00"; horaFin = "10:30"; motivoConsulta = "Seguimiento cardíaco"; estado = "CONFIRMADA"; canalReserva = "WEB" } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/citas" -Method POST -Headers $authHeaders -Body $cita3 -UseBasicParsing
Write-Host "  ✓ Cita Juan Pérez - Cardiología - $tomorrow3`n" -ForegroundColor Green

# 5. ÓRDENES DE EXAMEN
Write-Host "[5] Creando órdenes de examen..." -ForegroundColor Cyan
$today = (Get-Date).ToString("yyyy-MM-dd")
$orden1 = @{ pacienteId = 1; medicoId = 1; tipoExamen = "Electrocardiograma"; razonMedica = "Evaluación cardíaca"; fechaSolicitud = $today; urgencia = "NORMAL" } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/ordenes-examen" -Method POST -Headers $authHeaders -Body $orden1 -UseBasicParsing
Write-Host "  ✓ Electrocardiograma - Paciente 1" -ForegroundColor Green

$orden2 = @{ pacienteId = 2; medicoId = 2; tipoExamen = "Hemograma"; razonMedica = "Chequeo general"; fechaSolicitud = $today; urgencia = "NORMAL" } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/ordenes-examen" -Method POST -Headers $authHeaders -Body $orden2 -UseBasicParsing
Write-Host "  ✓ Hemograma - Paciente 2`n" -ForegroundColor Green

# 6. PAGOS
Write-Host "[6] Creando pagos..." -ForegroundColor Cyan
$now = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss")
$pago1 = @{ citaId = 1; monto = 250; metodoPago = "EFECTIVO"; estado = "PAGADO"; referencia = "REF-001"; fecha = $now } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/pagos" -Method POST -Headers $authHeaders -Body $pago1 -UseBasicParsing
Write-Host "  ✓ Pago S/. 250 - Efectivo - Cita 1" -ForegroundColor Green

$pago2 = @{ citaId = 2; monto = 150; metodoPago = "TARJETA"; estado = "PAGADO"; referencia = "REF-002"; fecha = $now } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/pagos" -Method POST -Headers $authHeaders -Body $pago2 -UseBasicParsing
Write-Host "  ✓ Pago S/. 150 - Tarjeta - Cita 2`n" -ForegroundColor Green

# 7. HISTORIA CLÍNICA
Write-Host "[7] Creando historia clínica..." -ForegroundColor Cyan
$hist1 = @{ pacienteId = 1; observaciones = "HTA, Sedentarismo. En tratamiento con Enalapril." } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/historia-clinica" -Method POST -Headers $authHeaders -Body $hist1 -UseBasicParsing
Write-Host "  ✓ Paciente 1 registrado" -ForegroundColor Green

$hist2 = @{ pacienteId = 2; observaciones = "Dermatitis atópica. Presenta acné moderado." } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/historia-clinica" -Method POST -Headers $authHeaders -Body $hist2 -UseBasicParsing
Write-Host "  ✓ Paciente 2 registrado" -ForegroundColor Green

$hist3 = @{ pacienteId = 3; observaciones = "Paciente en buen estado de salud. Sin enfermedades crónicas." } | ConvertTo-Json
$r = Invoke-WebRequest -Uri "$baseUrl/historia-clinica" -Method POST -Headers $authHeaders -Body $hist3 -UseBasicParsing
Write-Host "  ✓ Paciente 3 registrado`n" -ForegroundColor Green

Write-Host "════════════════════════════════════════" -ForegroundColor Green
Write-Host "  ✓ SIMULACIÓN COMPLETADA" -ForegroundColor Green
Write-Host "════════════════════════════════════════" -ForegroundColor Green
Write-Host "`n📍 Acceda a: http://localhost:8080/trinidad" -ForegroundColor Cyan
Write-Host "`n"
