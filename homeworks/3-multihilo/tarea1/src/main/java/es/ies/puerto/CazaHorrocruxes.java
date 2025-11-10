package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula la caza de horrocruxes por varios buscadores concurrentes 
 */
public class CazaHorrocruxes extends SimulacionBase {
    
    private final AtomicBoolean encontrado;
    private final AtomicReference<String> ganador;

    /**
     * Constructor por defecto
     */
    public CazaHorrocruxes() {
        this.encontrado = new AtomicBoolean(false);
        this.ganador = new AtomicReference<>(null);
    }

    /**
     * Getters
     */
    public boolean isEncontrado() {
        return encontrado.get();
    }

    public String getGanador() {
        return ganador.get();
    }

    /**
     * Metodo que representa un buscador de horrocruxes
     * @param nombre nombre del buscador
     * @param ubicacion ubicacion donde busca el horrocrux
     * @return Runnable que representa el buscador
     */
    private Runnable buscador(String nombre, String ubicacion) {
        return ()-> {
            try {
                int tiempo = ThreadLocalRandom.current().nextInt(500, 2001);
                Thread.sleep(tiempo);
                if (!encontrado.get()) {
                    encontrado.set(true);
                    ganador.set(nombre);
                    System.out.println(nombre + " encontró un Horrocrux en " + ubicacion + ". ¡Búsqueda terminada!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        Thread harry = new Thread(buscador("Harry", "Bosque Prohibido"));
        Thread hermione = new Thread(buscador("Hermione", "Biblioteca Antigua"));
        Thread ron = new Thread(buscador("Ron", "Mazmorras del Castillo"));
        return new Thread[]{ harry, hermione, ron };
    }

    public static void main(String[] args) {
        CazaHorrocruxes caza = new CazaHorrocruxes();
        caza.ejecutarSimulacion();
    }
}
