package es.ies.puerto.utils;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class ThreadUtils {

    /**
     * Metodo que inicia y espera la finalizacion de varios hilos
     * @param hilos hilos a ejecutar y esperar
     * @throws InterruptedException
     */
    public static void ejecutarYEsperar(Thread... hilos) {
        for (Thread hilo : hilos) {
            hilo.start();
        }
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
