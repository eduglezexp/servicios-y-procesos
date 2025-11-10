package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class BatallaMagosTest {

    BatallaMagos batalla;

    @BeforeEach
    void beforeEach() {
        batalla = new BatallaMagos();
    }

    @Test
    void BatallaMagosDebeHaberGanadorYTerminar() {
        String output = TestUtils.ejecutarConSalida(() -> {
            batalla.ejecutarSimulacion();
        });
        assertTrue(output.contains("gana la batalla mágica."),
        "La simulación debe terminar con un ganador.");
        assertTrue(batalla.isCombateTerminado() == true, 
        "El combate debe estar terminado.");
        assertTrue(batalla.getEnergiaGandalf() <= 0 || batalla.getEnergiaSaruman() <= 0,
        "La energía de un mago debe ser cero o menor.");
    }
}
