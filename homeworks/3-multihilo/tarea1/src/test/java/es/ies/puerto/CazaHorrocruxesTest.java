package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class CazaHorrocruxesTest {

    private static final String RON = "Ron";
    private static final String HERMIONE = "Hermione";
    private static final String HARRY = "Harry";
    private static final String BOSQUE_PROHIBIDO = "Bosque Prohibido";
    private static final String BIBLIOTECA_ANTIGUA = "Biblioteca Antigua";
    private static final String MAZMORRAS_DEL_CASTILLO = "Mazmorras del Castillo";

    @Test
    void CazaHorrocruxesUnGanadorYUnSoloHallazgo() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        CazaHorrocruxes caza = new CazaHorrocruxes();
        Thread harry = new Thread(caza.buscador(HARRY, BOSQUE_PROHIBIDO));
        Thread hermione = new Thread(caza.buscador(HERMIONE, BIBLIOTECA_ANTIGUA));
        Thread ron = new Thread(caza.buscador(RON, MAZMORRAS_DEL_CASTILLO));
        harry.start();
        hermione.start();
        ron.start();
        harry.join();
        hermione.join();
        ron.join();
        String output = outContent.toString();
        assertTrue(caza.encontrado.get() == true, "El horrocrux debe haber sido encontrado.");
        assertTrue(
            HARRY.equals(caza.ganador.get()) ||
            HERMIONE.equals(caza.ganador.get()) ||
            RON.equals(caza.ganador.get()),
            "El ganador debe ser Harry, Hermione o Ron"
        );
        assertEquals(1, output.split("encontró un Horrocrux").length - 1, 
        "Solo debe imprimirse un hallazgo");
    }
}
