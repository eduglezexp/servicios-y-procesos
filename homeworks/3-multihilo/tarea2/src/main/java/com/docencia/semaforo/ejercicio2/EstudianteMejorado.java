package com.docencia.semaforo.ejercicio2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class EstudianteMejorado implements Runnable {

    private final String nombre;
    private static final Semaphore semaforo = new Semaphore(4);
    
    /**
     * Constructor por defecto
     */
    public EstudianteMejorado() {
        nombre = "bot";
    }

    /**
     * Constructor con la propiedad nombre
     * @param nombre del estudiante
     */
    public EstudianteMejorado(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void run() {
        try {
            semaforo.acquire();
            int equipo = semaforo.availablePermits() + 1;
            System.out.println(nombre + " ha comenzado a utilizar el equipo " + equipo);
            int tiempo = ThreadLocalRandom.current().nextInt(3, 6) * 1000;
            Thread.sleep(tiempo);
            System.out.println(nombre + " ha finalizado con el equipo " + equipo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            Thread estudiante = new Thread(new EstudianteMejorado("Estudiante " + i));
            estudiante.start();
            try {
                estudiante.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
