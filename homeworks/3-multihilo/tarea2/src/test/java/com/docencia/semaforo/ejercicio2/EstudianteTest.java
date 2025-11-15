package com.docencia.semaforo.ejercicio2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.docencia.semaforo.utils.TestUtils;

public class EstudianteTest {
    
    @Test
    public void testSemaforoNoExcede4() {
        String salida = TestUtils.ejecutarConSalida(() -> {
            Estudiante.main(null);
        });
        for (int i = 1; i <= 6; i++) {
            assertTrue(salida.contains("Estudiante " + i + " ha comenzado"), "Salida debe contener inicio de Estudiante " + i);
            assertTrue(salida.contains("Estudiante " + i + " ha finalizado"), "Salida debe contener fin de Estudiante " + i);
        }
    }
}
