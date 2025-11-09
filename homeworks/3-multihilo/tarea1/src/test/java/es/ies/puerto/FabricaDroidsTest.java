package es.ies.puerto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class FabricaDroidsTest {

    @Test
    void FabricaDroidsNoSeActivaAntesDeEnsamblaryCuentaCorrecta() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        FabricaDroids fabrica = new FabricaDroids();
        Thread ensambladorThread = new Thread(fabrica.ensamblador());
        Thread activadorThread = new Thread(fabrica.activador());
        ensambladorThread.start();
        activadorThread.start();
        ensambladorThread.join();
        activadorThread.join();
        String output = outContent.toString();
        for (int k = 1; k <= fabrica.n; k++) {
            int idxE = output.indexOf("Ensamblado: Droid-" + k);
            int idxA = output.indexOf("Activado: Droid-" + k);
            assertTrue(idxE != -1 && idxA != -1 && idxE < idxA,
                    "Droid-" + k + " debe ensamblarse antes de activarse");
        }
        assertEquals(fabrica.n, fabrica.activados.get(),
                "Todos los droids deben haber sido activados");
    }
}
