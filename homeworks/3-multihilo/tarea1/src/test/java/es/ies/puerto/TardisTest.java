package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class TardisTest {

    Tardis tardis;

    @BeforeEach
    void beforeEach() {
        tardis = new Tardis();
    }

    @Test
    void TardisExisteUnaEraGanadora() {
        String output = TestUtils.ejecutarConSalida(() -> {
            tardis.ejecutarSimulacion();
        });
        assertTrue(tardis.isDestinoAlcanzado() == true, 
        "El destino deberia haber sido alcanzado");
        assertTrue(tardis.getEraGanadora() != null, 
        "La era ganadora no deberia ser nula");
        assertEquals(1, output.split("llegó primero").length - 1,
        "solo debe imprimirse un lugar");
    }
}
