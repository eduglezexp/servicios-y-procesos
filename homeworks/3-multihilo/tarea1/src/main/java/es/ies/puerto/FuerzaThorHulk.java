package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula una competencia de fuerza entre Thor y Hulk concurrentemente
 */
public class FuerzaThorHulk extends SimulacionBase {

    private static final String HULK = "Hulk";
    private static final String THOR = "Thor";


    private final int durationMS = 5000;
    private final AtomicBoolean tiempoTerminado;
    private final AtomicInteger totalThor;
    private final AtomicInteger totalHulk;

    /**
     * Constructor por defecto
     */
    public FuerzaThorHulk() {
        tiempoTerminado = new AtomicBoolean(false);
        totalThor = new AtomicInteger(0);
        totalHulk = new AtomicInteger(0);
    }

    /**
     * Metodo que representa el temporizador de la simulacion
     * @return Runnable que representa el temporizador
     */
    private Runnable temporizador() {
        return () -> {
            try {
                Thread.sleep(durationMS);
                tiempoTerminado.set(true);
                System.out.println("¡Tiempo!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Metodo que representa a un competidor (Thor o Hulk) que acumula fuerza
     * @param nombre nombre del competidor
     * @param total acumulador de fuerza del competidor
     * @return Runnable que representa al competidor
     */
    private Runnable competidor(String nombre, AtomicInteger total) {
        return () -> {
            while (!tiempoTerminado.get()) {
                int peso = ThreadLocalRandom.current().nextInt(5, 21);
                total.addAndGet(peso);
                try {
                    int tiempo = ThreadLocalRandom.current().nextInt(50, 121);
                    Thread.sleep(tiempo);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(nombre + " terminó con fuerza total = " + total.get());
        };
    }

    /**
     * Metodo que representa el hilo de Thor
     * @return Runnable que representa a Thor
     */
    private Runnable thor() {
        return competidor(THOR, totalThor);
    }

    /**
     * Metodo que representa el hilo de Hulk
     * @return Runnable que representa a Hulk
     */
    private Runnable hulk() {
        return competidor(HULK, totalHulk);
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        Thread temporizador = new Thread(temporizador());
        Thread thor = new Thread(thor());
        Thread hulk = new Thread(hulk());
        return new Thread[]{ temporizador, thor, hulk};
    }

    /**
     * Metodo que ejecuta la simulacion y muestra el ganador
     */
    @Override
    public void ejecutarSimulacion() {
        super.ejecutarSimulacion();
        int fuerzaThor = totalThor.get();
        int fuerzaHulk = totalHulk.get();
        if (fuerzaThor > fuerzaHulk) {
            System.out.println("Thor gana con " + fuerzaThor + " vs " + fuerzaHulk);
        } else if (fuerzaHulk > fuerzaThor) {
            System.out.println("Hulk gana con " + fuerzaHulk + " vs " + fuerzaThor);
        } else {
            System.out.println("Empate: " + fuerzaThor);
        }
    }

    public static void main(String[] args) {
        FuerzaThorHulk simulacion = new FuerzaThorHulk();
        simulacion.ejecutarSimulacion();
    }
}
