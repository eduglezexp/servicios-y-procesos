package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula un partido de Quidditch entre dos equipos con cazadores y un
 * buscador concurrentes
 */
public class Quidditch extends SimulacionBase {

    private final AtomicBoolean snitchAtrapada;
    private int puntosEquipoA = 0;
    private int puntosEquipoB = 0;
    private final ReentrantLock m;

    private static final String EQUIPO_A = "A";
    private static final String EQUIPO_B = "B";

    /**
     * Constructor por defecto
     */
    public Quidditch() {
        snitchAtrapada = new AtomicBoolean(false);
        this.m = new ReentrantLock();
    }

    /**
     * Metodo que representa un cazador de un equipo
     * 
     * @param nombreEquipo nombre del equipo
     * @return Runnable que representa el cazador
     */
    private Runnable cazador(String nombreEquipo) {
        return () -> {
            while (!snitchAtrapada.get()) {
                try {
                    int tiempo = ThreadLocalRandom.current().nextInt(200, 501);
                    Thread.sleep(tiempo);

                    int g = ThreadLocalRandom.current().nextInt(0, 2) * 10;
                    if (g > 0) {
                        m.lock();
                        try {
                            if (!snitchAtrapada.get()) {
                                if (nombreEquipo.equals(EQUIPO_A)) {
                                    puntosEquipoA += g;
                                    System.out.println("Equipo A anota 10. Total A=" + puntosEquipoA);
                                } else if (nombreEquipo.equals(EQUIPO_B)) {
                                    puntosEquipoB += g;
                                    System.out.println("Equipo B anota 10. Total B=" + puntosEquipoB);
                                }
                            }
                        } finally {
                            m.unlock();
                        }
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Metodo que representa un buscador intentando atrapar la snitch dorada
     * 
     * @return Runnable que representa el buscador
     */
    private Runnable buscador() {
        return () -> {
            while (!snitchAtrapada.get()) {
                try {
                    int tiempo = ThreadLocalRandom.current().nextInt(300, 701);
                    Thread.sleep(tiempo);
                    if (ThreadLocalRandom.current().nextInt(1, 101) <= 15) {
                        snitchAtrapada.set(true);
                        System.out.println("¡Snitch dorada atrapada!");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        Thread cazadorA = new Thread(cazador(EQUIPO_A));
        Thread cazadorB = new Thread(cazador(EQUIPO_B));
        Thread buscador = new Thread(buscador());
        return new Thread[]{cazadorA, cazadorB, buscador};
    }

    /**
     * Metodo que ejecuta la simulacion y muestra el marcador final
     */
    @Override
    public void ejecutarSimulacion() {
        super.ejecutarSimulacion();
        System.out.println("Marcador final: A=" + puntosEquipoA + " B=" + puntosEquipoB);
    }

    public static void main(String[] args) {
        Quidditch partido = new Quidditch();
        partido.ejecutarSimulacion();
    }
}
