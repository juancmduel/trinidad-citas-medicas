$BASE_URL = "http://localhost:8081/trinidad"
$TIMEOUT = 5

$MODULOS = @(
    @{ nombre = "Atenciones"; url = "/atenciones" },
    @{ nombre = "Auditoria"; url = "/auditoria" },
    @{ nombre = "Citas"; url = "/citas" },
    @{ nombre = "Diagnosticos"; url = "/diagnosticos" },
    @{ nombre = "Especialidades"; url = "/especialidades" },
    @{ nombre = "Historia Clinica"; url = "/historia-clinica" },
    @{ nombre = "Horarios"; url = "/horarios" },
    @{ nombre = "Medicos"; url = "/medicos" },
    @{ nombre = "Ordenes Examen"; url = "/ordenes-examen" },
    @{ nombre = "Pacientes"; url = "/pacientes" },
    @{ nombre = "Pagos"; url = "/pagos" },
    @{ nombre = "Recetas"; url = "/recetas" },
    @{ nombre = "Roles"; url = "/roles" },
    @{ nombre = "Usuarios"; url = "/usuarios" },
    @{ nombre = "Reportes"; url = "/reportes" }
)

Write-Host "`nVALIDACION DE MODULOS TRINIDAD CITAS MEDICAS`n"

$ok = 0
$error = 0

foreach ($mod in $MODULOS) {
    $url = "$BASE_URL$($mod.url)"
    Write-Host "Probando: $($mod.nombre)" -ForegroundColor Cyan
    
    try {
        $response = Invoke-WebRequest -Uri $url -TimeoutSec $TIMEOUT -SkipHttpErrorCheck -ErrorAction SilentlyContinue
        
        if ($response.StatusCode -eq 200) {
            Write-Host "  OK - Status: 200" -ForegroundColor Green
            $ok++
        } else {
            Write-Host "  ERROR - Status: $($response.StatusCode)" -ForegroundColor Red
            $error++
        }
    } catch {
        Write-Host "  ERROR - No responde" -ForegroundColor Red
        $error++
    }
}

Write-Host "`nRESUMEN:"
Write-Host "  Exitosos: $ok"
Write-Host "  Errores:  $error"
Write-Host "  Total:    $($MODULOS.Count)`n"
