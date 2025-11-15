package com.docencia.semaforo.ejercicio1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class ColorSemaforo implements Runnable {

    private Colores color = Colores.ROJO;
    private final AtomicBoolean stopSemaforo;
    private final Semaphore semaphore;

    /**
     * Constructor por defecto
     */
    public ColorSemaforo() {
        stopSemaforo = new AtomicBoolean(false);
        semaphore = new Semaphore(1, true);
    }
    
    /**
     * Metodo para detener el semaforo
     */
    public void detener() {
        stopSemaforo.set(true);
    }

    /**
     * Metodo para obtener el tiempo del color
     * @return el tiempo del color
     */
    private int tiempoDelColor() {
        return color.getTiempo();
    }

    /**
     * Metodo que actualiza la propiedad color 
     * con el siguiente color
     */
    private void siguienteColor() {
        color = color.siguiente();
    }

    @Override
    public void run() {
        while (!stopSemaforo.get()) {
            try {
                semaphore.acquire();
                System.out.println(color);
                Thread.sleep(tiempoDelColor());
                siguienteColor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        ColorSemaforo semaforo = new ColorSemaforo();
        Thread hilo = new Thread(semaforo);
        hilo.start();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        semaforo.detener();
    }
}
