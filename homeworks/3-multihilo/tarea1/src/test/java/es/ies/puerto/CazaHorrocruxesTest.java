package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class CazaHorrocruxesTest {

    static final String RON = "Ron";
    static final String HERMIONE = "Hermione";
    static final String HARRY = "Harry";

    CazaHorrocruxes caza;

    @BeforeEach
    void beforeEach() {
        caza = new CazaHorrocruxes();
    }

    @Test
    void CazaHorrocruxesUnGanadorYUnSoloHallazgo() {
        String output = TestUtils.ejecutarConSalida(() -> {
            caza.ejecutarSimulacion();
        });
        assertTrue(caza.isEncontrado() == true, 
        "El horrocrux debe haber sido encontrado.");
        assertTrue(
            HARRY.equals(caza.getGanador()) ||
            HERMIONE.equals(caza.getGanador()) ||
            RON.equals(caza.getGanador()),
            "El ganador debe ser Harry, Hermione o Ron"
        );
        assertEquals(1, output.split("encontró un Horrocrux").length - 1, 
        "Solo debe imprimirse un hallazgo");
    }
}
