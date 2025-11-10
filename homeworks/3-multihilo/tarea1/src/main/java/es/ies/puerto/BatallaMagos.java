package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula una batalla mágica entre dos magos: Gandalf y Saruman de manera concurrente
 */
public class BatallaMagos extends SimulacionBase {

    private final AtomicInteger energiaGandalf;
    private final AtomicInteger energiaSaruman;
    private final AtomicBoolean combateTerminado;
    private final ReentrantLock m;

    private static final String GANDALF = "Gandalf";
    private static final String SARUMAN = "Saruman";

    /**
     * Constructor por defecto
     */
    public BatallaMagos() {
        this.energiaGandalf = new AtomicInteger(120);
        this.energiaSaruman = new AtomicInteger(120);
        this.combateTerminado = new AtomicBoolean(false);
        this.m = new ReentrantLock();
    }

    /**
     * Getters
     */
    public boolean isCombateTerminado() {
        return combateTerminado.get();
    }

    public int getEnergiaGandalf() {
        return energiaGandalf.get();
    }

    public int getEnergiaSaruman() {
        return energiaSaruman.get();
    }

    /**
     * Metodo que simula el lanzamiento de un hechizo de un mago a otro
     * @param atacante nombre del mago que ataca
     * @param energiaRival energia del mago rival
     */
    private void lanzarHechizo(String atacante, AtomicInteger energiaRival) {
        int danio = ThreadLocalRandom.current().nextInt(8, 26);
        int energiaRestante = energiaRival.addAndGet(-danio);
        System.out.println(atacante + " lanza hechizo por " + danio + ". Energía rival: " + energiaRestante);
        if (energiaRestante <= 0 && !combateTerminado.get()) {
            combateTerminado.set(true);
            System.out.println(atacante + " gana la batalla mágica.");
        }
    }

    /**
     * Metodo que representa a un mago que lanza hechizos
     * @param atacante nombre del mago
     * @param energiaRival energia del mago rival
     * @return Runnable que representa al mago
     */
    private Runnable mago(String atacante, AtomicInteger energiaRival) {
        return () -> {
            while (!combateTerminado.get()) {
                m.lock();
                try {
                    if (!combateTerminado.get()) {
                        lanzarHechizo(atacante, energiaRival);
                    }
                } finally {
                    m.unlock();
                }
                try {
                    int tiempo = ThreadLocalRandom.current().nextInt(200, 601);
                    Thread.sleep(tiempo);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Metodo que representa el hilo de Gandalf
     * @return Runnable que representa a Gandalf
     */
    private Runnable magoGandalf() {
        return mago(GANDALF, energiaSaruman);
    }

    /**
     * Metodo que representa el hilo de Saruman
     * @return Runnable que representa a Saruman
     */
    private Runnable magoSaruman() {
        return mago(SARUMAN, energiaGandalf);
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        Thread gandalf = new Thread(magoGandalf());
        Thread saruman = new Thread(magoSaruman());
        return new Thread[] { gandalf, saruman };
    }

    public static void main(String[] args) {
        BatallaMagos batalla = new BatallaMagos();
        batalla.ejecutarSimulacion();
    }
}
