package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula la caza de horrocruxes por varios buscadores concurrentes 
 */
public class CazaHorrocruxes {
    public final AtomicBoolean encontrado;
    public final AtomicReference<String> ganador;

    /**
     * Constructor por defecto
     */
    public CazaHorrocruxes() {
        encontrado = new AtomicBoolean(false);
        ganador = new AtomicReference<>(null);
    }

    /**
     * Metodo que representa un buscador de horrocruxes
     * @param nombre nombre del buscador
     * @param ubicacion ubicacion donde busca el horrocrux
     * @return Runnable que representa el buscador
     */
    public Runnable buscador(String nombre, String ubicacion) {
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

    public static void main(String[] args) throws InterruptedException {
        CazaHorrocruxes caza = new CazaHorrocruxes();
        Thread harry = new Thread(caza.buscador("Harry", "Bosque Prohibido"));
        Thread hermione = new Thread(caza.buscador("Hermione", "Biblioteca Antigua"));
        Thread ron = new Thread(caza.buscador("Ron", "Mazmorras del Castillo"));
        harry.start();
        hermione.start();
        ron.start();
        harry.join();
        hermione.join();
        ron.join();
    }
}
