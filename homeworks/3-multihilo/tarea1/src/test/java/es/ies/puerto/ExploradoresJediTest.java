package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class ExploradoresJediTest {

    ExploradoresJedi exploradores;

    @BeforeEach
    void beforeEach() {
        exploradores = new ExploradoresJedi();
    }

    @Test
    void ExploradoresJediUnSoloGanador() {
        String output = TestUtils.ejecutarConSalida(() -> {
            exploradores.ejecutarSimulacion();
        });
        assertTrue(exploradores.isPistaEncontrada(),
        "El Holocrón debe haber sido encontrado.");
        assertEquals(1, output.split("halló una pista").length - 1,
        "Solo debe imprimirse un hallazgo");
    }
}
