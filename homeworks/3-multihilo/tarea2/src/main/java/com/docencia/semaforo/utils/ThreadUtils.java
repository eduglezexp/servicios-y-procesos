package com.docencia.semaforo.utils;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class ThreadUtils {

    /**
     * Funcion que inicia varios hilos
     * @param hilos hilos a ejecutar
     * @throws InterruptedException
     */
    public static void ejecutarHilos(Thread... hilos) {
        for (Thread hilo : hilos) {
            hilo.start();
        }
    }
}
