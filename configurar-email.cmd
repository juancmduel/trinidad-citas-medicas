@echo off
chcp 65001 >nul
echo ============================================================
echo   CONFIGURACION DE CORREO - Trinidad Citas Medicas
echo ============================================================
echo.
echo Este script reemplaza las credenciales de Gmail en
echo application.properties para que el sistema pueda
echo enviar correos de confirmacion de citas.
echo.
echo REQUISITOS:
echo   1. Tener una cuenta Gmail
echo   2. Activar verificacion en 2 pasos en tu cuenta Google
echo   3. Generar una App Password en:
echo      https://myaccount.google.com/apppasswords
echo      (Selecciona: App = Correo, Dispositivo = Windows)
echo.
echo ============================================================

set /p GMAIL_USER="Ingresa tu correo Gmail (ej: tucorreo@gmail.com): "
set /p GMAIL_PASS="Ingresa tu App Password de Google (16 caracteres sin espacios): "

set PROPS_FILE=src\main\resources\application.properties

if not exist "%PROPS_FILE%" (
    echo.
    echo ERROR: No se encontro el archivo %PROPS_FILE%
    echo Asegurate de ejecutar este script desde la raiz del proyecto.
    pause
    exit /b 1
)

powershell -Command "(Get-Content '%PROPS_FILE%') -replace 'spring\.mail\.username=.*', 'spring.mail.username=%GMAIL_USER%' | Set-Content '%PROPS_FILE%'"
powershell -Command "(Get-Content '%PROPS_FILE%') -replace 'spring\.mail\.password=.*', 'spring.mail.password=%GMAIL_PASS%' | Set-Content '%PROPS_FILE%'"

echo.
echo ============================================================
echo   Listo! Credenciales actualizadas correctamente.
echo   Archivo: %PROPS_FILE%
echo ============================================================
echo.
pause
