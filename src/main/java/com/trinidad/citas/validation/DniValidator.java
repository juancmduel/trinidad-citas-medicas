package com.trinidad.citas.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementación del validador de DNI peruano.
 * Acepta exactamente 8 dígitos numéricos (0-9).
 */
@SuppressWarnings("unused")
public class DniValidator implements ConstraintValidator<ValidDni, String> {

    private static final String DNI_PATTERN = "^\\d{8}$";

    @Override
    public void initialize(ValidDni constraintAnnotation) {
        // No se requiere configuracion adicional del validador
    }

    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) {
        if (dni == null) return false;
        return dni.matches(DNI_PATTERN);
    }
}
