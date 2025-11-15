package com.docencia.semaforo.ejercicio2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class Estudiante implements Runnable {

    private final String nombre;
    private static final Semaphore semaforo = new Semaphore(4);
    
    /**
     * Constructor por defecto
     */
    public Estudiante() {
        nombre = "bot";
    }

    /**
     * Constructor con la propiedad nombre
     * @param nombre del estudiante
     */
    public Estudiante(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void run() {
        try {
            semaforo.acquire();
            System.out.println(nombre + " ha comenzado a utilizar el equipo.");
            int tiempo = ThreadLocalRandom.current().nextInt(3, 6) * 1000;
            Thread.sleep(tiempo);
            System.out.println(nombre + " ha finalizado con el equipo.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            Thread estudiante = new Thread(new Estudiante("Estudiante " + i));
            estudiante.start();
            try {
                estudiante.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
