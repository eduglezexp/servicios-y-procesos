package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.ies.puerto.utils.TestUtils;

public class BatallaPokemonTest {

    BatallaPokemon batalla;

    @BeforeEach
    void beforeEach() {
        batalla = new BatallaPokemon();
    }

    @Test
    void BatallaPokemonDebeHaberGanador() {
        String output = TestUtils.ejecutarConSalida(() -> {
            batalla.ejecutarSimulacion();
        });
        assertTrue(output.contains("ha ganado la batalla!"), "El output debe contener un ganador de la batalla.");
        assertTrue(batalla.isJuegoTerminado() == true, "El juego debe estar marcado como terminado.");
        assertTrue(batalla.getHpPikachu() <= 0 || batalla.getHpCharmander() <= 0, "Al menos un Pokemon debe tener HP menor o igual a 0.");
    }
}
