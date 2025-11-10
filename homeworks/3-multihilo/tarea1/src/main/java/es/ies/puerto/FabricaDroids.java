package es.ies.puerto;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

 /**
  * Clase que simula una fabrica de droids con ensambladores y activadores concurrentes
  */
public class FabricaDroids extends SimulacionBase {
    private final BlockingQueue<String> ensamblados;
    private int n = 10;
    private final AtomicInteger activados;

    /**
     * Constructor por defecto
     */
    public FabricaDroids() {
        ensamblados = new ArrayBlockingQueue<>(n);
        activados = new AtomicInteger(0);
    }

    /**
     * Getters
     */
    public int getN() {
        return n;
    }

    public int getActivados() {
        return activados.get();
    }

    /**
     * Metodo que representa el ensamblador de droids
     * @return Runnable que representa el ensamblador de droids
     */
    private Runnable ensamblador() {
        return () -> {
            for (int i = 1; i <= n; i++) {
                try {
                    int tiempo = ThreadLocalRandom.current().nextInt(100, 301);
                    Thread.sleep(tiempo);
                    String droid = "Droid-" + i;
                    System.out.println("Ensamblado: " + droid);
                    ensamblados.put(droid);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Metodo que representa el activador de droids
     * @return Runnable que representa el activador de droids
     */
    private Runnable activador() {
        return () -> {
            int cuenta = 0;
            while (cuenta < n) {
                try {
                    String droid = ensamblados.take();
                    System.out.println("Activado: " + droid);
                    activados.incrementAndGet();
                    cuenta++;
                    int tiempo = ThreadLocalRandom.current().nextInt(50, 151);
                    Thread.sleep(tiempo);
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
        Thread ensambladorThread = new Thread(ensamblador());
        Thread activadorThread = new Thread(activador());
        return new Thread[] { ensambladorThread, activadorThread };
    }

    public static void main(String[] args) {
        FabricaDroids fabrica = new FabricaDroids();
        fabrica.ejecutarSimulacion();
    }
}
