package com.docencia.semaforo.ejercicio1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.docencia.semaforo.utils.TestUtils;

public class ColorSemaforoTest {

    @Test
    public void testSecuenciaColores() {
        String salida = TestUtils.ejecutarConSalida(() -> {
            ColorSemaforo.main(null);
        });
        assertTrue(salida.contains("ROJO"), "Debe contener ROJO");
        assertTrue(salida.contains("VERDE"), "Debe contener VERDE");
        assertTrue(salida.contains("AMBAR"), "Debe contener AMBAR");
    }
}
