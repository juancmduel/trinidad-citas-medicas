#!/usr/bin/env python3
"""
Script de simulación para Trinidad Citas Médicas
Genera datos de prueba en todos los módulos mediante API REST
"""

import requests
import json
import time
import sys
from datetime import datetime, timedelta

BASE_URL = "http://localhost:8080/trinidad/api/v1"
HEADERS = {"Content-Type": "application/json"}
TOKEN = None

def log(msg, level="INFO"):
    """Registrar mensaje con timestamp"""
    print(f"[{level}] {msg}")

def authenticate():
    """Obtener token JWT"""
    global TOKEN
    try:
        response = requests.post(
            f"{BASE_URL}/auth/login",
            json={"username": "admin", "password": "admin123"},
            headers=HEADERS,
            timeout=5
        )
        if response.status_code == 200:
            TOKEN = response.json().get("token")
            log(f"✓ Autenticación exitosa")
            return True
        else:
            log(f"✗ Error de autenticación: {response.status_code}", "ERROR")
            return False
    except Exception as e:
        log(f"✗ Error conectando: {e}", "ERROR")
        return False

def get_auth_headers():
    """Obtener headers con token"""
    return {**HEADERS, "Authorization": f"Bearer {TOKEN}"}

def post_request(endpoint, data):
    """Hacer POST request"""
    try:
        response = requests.post(
            f"{BASE_URL}{endpoint}",
            json=data,
            headers=get_auth_headers(),
            timeout=5
        )
        if response.status_code in [200, 201]:
            return response.json() if response.text else data
        else:
            log(f"✗ {endpoint}: {response.status_code}", "WARN")
            return None
    except Exception as e:
        log(f"✗ Error en {endpoint}: {e}", "ERROR")
        return None

def create_especialidades():
    """Crear especialidades"""
    log("\n[1] CREANDO ESPECIALIDADES...")
    data = [
        {
            "nombre": "Cardiología",
            "precioConsulta": 250,
            "duracionMinutos": 30,
            "descripcion": "Especialidad del corazón y sistema cardiovascular",
            "activo": 1
        },
        {
            "nombre": "Dermatología",
            "precioConsulta": 150,
            "duracionMinutos": 25,
            "descripcion": "Especialidad de la piel y sus enfermedades",
            "activo": 1
        },
        {
            "nombre": "Pediatría",
            "precioConsulta": 120,
            "duracionMinutos": 20,
            "descripcion": "Especialidad en medicina infantil",
            "activo": 1
        }
    ]
    
    for esp in data:
        result = post_request("/especialidades", esp)
        if result:
            log(f"  ✓ {esp['nombre']} - S/. {esp['precioConsulta']}")

def create_pacientes():
    """Crear pacientes"""
    log("\n[2] CREANDO PACIENTES...")
    data = [
        {
            "dni": "10234567",
            "nombres": "Carlos",
            "apellidoPaterno": "García",
            "apellidoMaterno": "López",
            "fechaNacimiento": "1985-03-15",
            "sexo": "M",
            "telefono": "987654321",
            "email": "carlos@email.com",
            "tipoSangre": "O+",
            "alergias": "Penicilina",
            "activo": 1
        },
        {
            "dni": "20345678",
            "nombres": "María",
            "apellidoPaterno": "Rodríguez",
            "apellidoMaterno": "Martínez",
            "fechaNacimiento": "1990-07-22",
            "sexo": "F",
            "telefono": "965432187",
            "email": "maria@email.com",
            "tipoSangre": "A+",
            "alergias": "Ninguna",
            "activo": 1
        },
        {
            "dni": "30456789",
            "nombres": "Juan",
            "apellidoPaterno": "Pérez",
            "apellidoMaterno": "González",
            "fechaNacimiento": "1975-11-08",
            "sexo": "M",
            "telefono": "943210765",
            "email": "juan@email.com",
            "tipoSangre": "B+",
            "alergias": "Aspirina",
            "activo": 1
        }
    ]
    
    for pac in data:
        result = post_request("/pacientes", pac)
        if result:
            log(f"  ✓ {pac['nombres']} {pac['apellidoPaterno']} - DNI: {pac['dni']}")

def create_medicos():
    """Crear médicos"""
    log("\n[3] CREANDO MÉDICOS...")
    data = [
        {
            "cmp": "CMP-001",
            "dni": "60789012",
            "nombres": "Roberto",
            "apellidoPaterno": "Carrasco",
            "apellidoMaterno": "Soto",
            "especialidadId": 1,
            "usuarioId": 25,
            "telefono": "971234567",
            "email": "roberto@hospital.com",
            "consultorio": "201",
            "activo": 1
        },
        {
            "cmp": "CMP-002",
            "dni": "70890123",
            "nombres": "Sandra",
            "apellidoPaterno": "Vega",
            "apellidoMaterno": "Cruz",
            "especialidadId": 2,
            "usuarioId": 26,
            "telefono": "961234567",
            "email": "sandra@hospital.com",
            "consultorio": "202",
            "activo": 1
        }
    ]
    
    for med in data:
        result = post_request("/medicos", med)
        if result:
            log(f"  ✓ {med['nombres']} {med['apellidoPaterno']} - CMP: {med['cmp']}")

def create_citas():
    """Crear citas"""
    log("\n[4] CREANDO CITAS...")
    today = datetime.now().date()
    data = [
        {
            "pacienteId": 1,
            "medicoId": 1,
            "especialidadId": 1,
            "fechaCita": str(today + timedelta(days=1)),
            "horaInicio": "08:30",
            "horaFin": "09:00",
            "motivoConsulta": "Evaluación cardíaca",
            "estado": "PROGRAMADA",
            "canalReserva": "WEB"
        },
        {
            "pacienteId": 2,
            "medicoId": 2,
            "especialidadId": 2,
            "fechaCita": str(today + timedelta(days=2)),
            "horaInicio": "09:00",
            "horaFin": "09:30",
            "motivoConsulta": "Consulta dermatológica",
            "estado": "PROGRAMADA",
            "canalReserva": "WEB"
        },
        {
            "pacienteId": 3,
            "medicoId": 1,
            "especialidadId": 1,
            "fechaCita": str(today + timedelta(days=3)),
            "horaInicio": "10:00",
            "horaFin": "10:30",
            "motivoConsulta": "Seguimiento cardíaco",
            "estado": "CONFIRMADA",
            "canalReserva": "WEB"
        }
    ]
    
    for cita in data:
        result = post_request("/citas", cita)
        if result:
            log(f"  ✓ Cita {cita['fechaCita']} - {cita['horaInicio']}")

def create_ordenes_examen():
    """Crear órdenes de examen"""
    log("\n[5] CREANDO ÓRDENES DE EXAMEN...")
    data = [
        {
            "pacienteId": 1,
            "medicoId": 1,
            "tipoExamen": "Electrocardiograma",
            "razonMedica": "Evaluación cardíaca",
            "fechaSolicitud": datetime.now().strftime("%Y-%m-%d"),
            "urgencia": "NORMAL"
        },
        {
            "pacienteId": 2,
            "medicoId": 2,
            "tipoExamen": "Hemograma",
            "razonMedica": "Chequeo general",
            "fechaSolicitud": datetime.now().strftime("%Y-%m-%d"),
            "urgencia": "NORMAL"
        }
    ]
    
    for orden in data:
        result = post_request("/ordenes-examen", orden)
        if result:
            log(f"  ✓ {orden['tipoExamen']}")

def create_pagos():
    """Crear pagos"""
    log("\n[6] CREANDO PAGOS...")
    data = [
        {
            "citaId": 1,
            "monto": 250,
            "metodoPago": "EFECTIVO",
            "estado": "PAGADO",
            "referencia": "REF-001",
            "fecha": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        },
        {
            "citaId": 2,
            "monto": 150,
            "metodoPago": "TARJETA",
            "estado": "PAGADO",
            "referencia": "REF-002",
            "fecha": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
    ]
    
    for pago in data:
        result = post_request("/pagos", pago)
        if result:
            log(f"  ✓ Pago S/. {pago['monto']}")

def create_historia_clinica():
    """Crear historia clínica"""
    log("\n[7] CREANDO HISTORIA CLÍNICA...")
    data = [
        {
            "pacienteId": 1,
            "observaciones": "HTA, Sedentarismo. En tratamiento con Enalapril."
        },
        {
            "pacienteId": 2,
            "observaciones": "Dermatitis atópica. Presenta acné moderado."
        },
        {
            "pacienteId": 3,
            "observaciones": "Paciente en buen estado de salud. Sin enfermedades crónicas."
        }
    ]
    
    for hist in data:
        result = post_request("/historia-clinica", hist)
        if result:
            log(f"  ✓ Paciente {hist['pacienteId']} registrado")

def main():
    """Ejecutar simulación completa"""
    print("\n" + "="*50)
    print("  SIMULACIÓN - TRINIDAD CITAS MÉDICAS")
    print("="*50)
    
    if not authenticate():
        log("No se pudo autenticar. Abortando.", "CRITICAL")
        sys.exit(1)
    
    create_especialidades()
    time.sleep(1)
    create_pacientes()
    time.sleep(1)
    create_medicos()
    time.sleep(1)
    create_citas()
    time.sleep(1)
    create_ordenes_examen()
    time.sleep(1)
    create_pagos()
    time.sleep(1)
    create_historia_clinica()
    
    print("\n" + "="*50)
    print("  ✓ SIMULACIÓN COMPLETADA")
    print("  URL: http://localhost:8080/trinidad")
    print("="*50 + "\n")

if __name__ == "__main__":
    main()
