package com.docencia.semaforo.ejercicio1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.docencia.semaforo.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class ColorSemaforoMejorado extends SimulacionBase {

    private Semaphore rojo;
    private Semaphore verde;
    private Semaphore ambar;
    private final AtomicBoolean stop;

    /**
     * Constructor por defecto
     */
    public ColorSemaforoMejorado() {
        this.rojo = new Semaphore(1);
        this.verde = new Semaphore(0);
        this.ambar = new Semaphore(0);
        stop = new AtomicBoolean(false);
    }

    /**
     * Metodo para detener el semaforo
     */
    public void detener() {
        stop.set(true);
    }

    /**
     * Metodo para obtener el tiempo del color
     * @return el tiempo del color
     */
    private int tiempoDelColor(Colores color) {
        return color.getTiempo();
    }

    /**
     * Metodo para ejecutar el color de un semaforo
     * @param nombreColor nombre del color
     * @param actual color actual
     * @param siguiente color siguiente
     * @return Runnable que representa el color de un semaforo
     */
    private Runnable ejecutarColor(Colores nombreColor, Semaphore actual, Semaphore siguiente) {
        return () -> {
            while (!stop.get()) {
                try {
                    actual.acquire();
                    if (stop.get()) break;
                    System.out.println(nombreColor);
                    Thread.sleep(tiempoDelColor(nombreColor));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if (!stop.get()) {
                    siguiente.release();
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
        Thread hiloRojo = new Thread(ejecutarColor(Colores.ROJO, rojo, verde));
        Thread hiloVerde = new Thread(ejecutarColor(Colores.VERDE, verde, ambar));
        Thread hiloAmbar = new Thread(ejecutarColor(Colores.AMBAR, ambar, rojo));
        return new Thread[]{hiloRojo, hiloVerde, hiloAmbar};
    }

    public static void main(String[] args) {
        ColorSemaforoMejorado semaforo = new ColorSemaforoMejorado();
        semaforo.ejecutarSimulacion();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        semaforo.detener();
    }
}
