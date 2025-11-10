package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class FabricaDroidsTest {

    FabricaDroids fabrica;

    @BeforeEach
    void beforeEach() {
        fabrica = new FabricaDroids();
    }

    @Test
    void FabricaDroidsNoSeActivaAntesDeEnsamblaryCuentaCorrecta() {
        String output = TestUtils.ejecutarConSalida(() -> {
            fabrica.ejecutarSimulacion();
        });
        for (int k = 1; k <= fabrica.getN(); k++) {
            int idxE = output.indexOf("Ensamblado: Droid-" + k);
            int idxA = output.indexOf("Activado: Droid-" + k);
            assertTrue(idxE != -1 && idxA != -1 && idxE < idxA,
            "Droid-" + k + " debe ensamblarse antes de activarse");
        }
        assertEquals(fabrica.getN(), fabrica.getActivados(),
        "Todos los droids deben haber sido activados");
    }
}
