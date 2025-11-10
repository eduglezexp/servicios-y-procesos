package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class ExploradoresJedi extends SimulacionBase {

    private final AtomicBoolean pistaEncontrada;
    private final AtomicReference<String> ganador;

    /**
     * Constructor por defecto
     */
    public ExploradoresJedi() {
        this.pistaEncontrada = new AtomicBoolean(false);
        this.ganador = new AtomicReference<>(null);
    }

    /**
     * Getters
     */
    public boolean isPistaEncontrada() {
        return pistaEncontrada.get();
    }

    /**
     * Hilo que representa a un Jedi explorador que busca una pista en un planeta
     * @param nombre  nombre del Jedi
     * @param planeta planeta donde busca la pista
     * @return Runnable que ejecuta la busqueda de la pista
     */
    private Runnable jedi(String nombre, String planeta) {
        return () -> {
            try {
                int tiempo = ThreadLocalRandom.current().nextInt(400, 1501);
                Thread.sleep(tiempo);
                if (!pistaEncontrada.get()) {
                    pistaEncontrada.set(true);
                    ganador.set(nombre);
                    System.out.println(nombre + " halló una pista en " + planeta + ". Fin de búsqueda.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() { 
        Thread kenobi = new Thread(jedi("Kenobi", "Tatooine"));
        Thread skywalker = new Thread(jedi("Skywalker", "Dagobah"));
        return new Thread[] { kenobi, skywalker };
    }

    public static void main(String[] args) {
        ExploradoresJedi exploradores = new ExploradoresJedi();
        exploradores.ejecutarSimulacion();
    }
}
