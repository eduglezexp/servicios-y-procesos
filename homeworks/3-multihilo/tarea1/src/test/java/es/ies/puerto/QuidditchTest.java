package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class QuidditchTest {

    Quidditch partido;

    @BeforeEach
    void beforeEach() {
        partido = new Quidditch();
    }

    @Test
    void QuidditchTerminaCuandoSnitchAtrapada() {
        String output = TestUtils.ejecutarConSalida(() -> {
            partido.ejecutarSimulacion();
        });
        assertTrue(output.contains("¡Snitch dorada atrapada!"),
        "El partido debe terminar cuando la snitch dorada es atrapada");
        assertTrue(output.contains("Marcador final:"),
        "Debe imprimirse el marcador final al terminar el juego");
    }
}
