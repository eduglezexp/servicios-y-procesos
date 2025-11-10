package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class FuerzaThorHulkTest {

    FuerzaThorHulk simulacion;

    @BeforeEach
    void beforeEach() {
        simulacion = new FuerzaThorHulk();
    }

    @Test
    void FuerzaThorHulkTerminaPorTiempoYDeclaraResultado() {
        String output = TestUtils.ejecutarConSalida(() -> {
            simulacion.ejecutarSimulacion();
        });
        assertTrue(output.contains("¡Tiempo!"), 
        "La simulación debe terminar por tiempo.");
        assertTrue( output.contains("gana") || output.contains("Empate"),
        "La simulación debe declarar un ganador o un empate.");
    }
}
