package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class BatallaPokemonTest {

    @Test
    void BatallaPokemonDebeHaberGanador() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BatallaPokemon batalla = new BatallaPokemon();
        Thread pikachuThread = new Thread(batalla.hiloPikachu());
        Thread charmanderThread = new Thread(batalla.hiloCharmander());
        pikachuThread.start();
        charmanderThread.start();
        pikachuThread.join();
        charmanderThread.join();
        String output = outContent.toString();
        assertTrue(output.contains("ha ganado la batalla!"), "El output debe contener un ganador de la batalla.");
        assertTrue(batalla.juegoTerminado.get() == true, "El juego debe estar marcado como terminado.");
        assertTrue(batalla.hpPikachu <= 0 || batalla.hpCharmander <= 0, "Al menos un Pokemon debe tener HP menor o igual a 0.");
    }
}
