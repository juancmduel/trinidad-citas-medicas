package com.trinidad.citas.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(String entidad, Object id) {
        super(entidad + " no encontrado(a) con id: " + id);
    }
}
