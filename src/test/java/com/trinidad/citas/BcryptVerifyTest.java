package com.trinidad.citas;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Test rápido para verificar/generar hash BCrypt.
 * Ejecutar con: mvn test -Dtest=BcryptVerifyTest -DskipTests=false
 */
class BcryptVerifyTest {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Test
    void verificarHashExistente() {
        String password = "admin123";
        String hashEnSeed = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

        boolean coincide = encoder.matches(password, hashEnSeed);
        System.out.println("===========================================");
        System.out.println("PASSWORD: " + password);
        System.out.println("HASH EN SEED: " + hashEnSeed);
        System.out.println("¿COINCIDE?: " + coincide);
        System.out.println("===========================================");
    }

    @Test
    void generarNuevoHash() {
        String password = "admin123";
        String nuevoHash = encoder.encode(password);

        System.out.println("===========================================");
        System.out.println("NUEVO HASH GENERADO para '" + password + "':");
        System.out.println(nuevoHash);
        System.out.println("¿VERIFICA OK?: " + encoder.matches(password, nuevoHash));
        System.out.println("-------------------------------------------");
        System.out.println("SQL para actualizar Oracle:");
        System.out.println("UPDATE USUARIO SET PASSWORD_HASH = '" + nuevoHash + "' WHERE USERNAME IN ('admin','gerente','recepcion','dr.garcia','dra.lopez','paciente1');");
        System.out.println("COMMIT;");
        System.out.println("===========================================");
    }
}
