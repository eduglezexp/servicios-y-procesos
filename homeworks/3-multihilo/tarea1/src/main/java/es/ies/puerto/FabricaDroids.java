package es.ies.puerto;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

 /**
  * Clase que simula una fabrica de droids con ensambladores y activadores concurrentes
  */
public class FabricaDroids {
    BlockingQueue<String> ensamblados;
    int n = 10;
    AtomicInteger activados;

    /**
     * Constructor por defecto
     */
    public FabricaDroids() {
        ensamblados = new ArrayBlockingQueue<>(n);
        activados = new AtomicInteger(0);
    }

    /**
     * Metodo que representa el ensamblador de droids
     * @return Runnable que representa el ensamblador de droids
     */
    public Runnable ensamblador() {
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
    public Runnable activador() {
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

    public static void main(String[] args) throws InterruptedException {
        FabricaDroids fabrica = new FabricaDroids();
        Thread ensambladorThread = new Thread(fabrica.ensamblador());
        Thread activadorThread = new Thread(fabrica.activador());
        ensambladorThread.start();
        activadorThread.start();
        ensambladorThread.join();
        activadorThread.join();
    }
}
