package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class MilleniumFalconTest {

    MilleniumFalcon falcon;

    @BeforeEach
    void beforeEach() {
        falcon = new MilleniumFalcon();
    }

    @Test
    void MilleniumFalconFinalizaConEscapeODestruccion() {
        String output = TestUtils.ejecutarConSalida(() -> {
            falcon.ejecutarSimulacion();
        });
        assertTrue(output.contains("se destruye") || output.contains("escapan"),
        "La simulación debe terminar con la destrucción de la nave o su escape.");
    }
}
