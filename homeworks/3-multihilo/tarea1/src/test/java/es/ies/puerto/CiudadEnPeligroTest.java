package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class CiudadEnPeligroTest {

    CiudadEnPeligro ciudad;

    @BeforeEach
    void beforeEach() {
        ciudad = new CiudadEnPeligro();
    }

    @Test
    void CiudadEnPeligroSoloNeutralizaElOtroSeDetiene() {
        String output = TestUtils.ejecutarConSalida(() -> {
            ciudad.ejecutarSimulacion();
        });
        assertTrue(ciudad.isAmenazaNeutralizada() == true,
        "La ciudad debe haber sido salvada.");
        assertTrue(
            "Superman".equals(ciudad.getGanador()) ||
            "Batam".equals(ciudad.getGanador()),
            "El ganador debe ser Superman o Batam"
        );
        assertEquals(1, output.split("Amenaza neutralizada").length -1,
        "Solo debe imprimirse una neutralización de amenaza");
    }
}
