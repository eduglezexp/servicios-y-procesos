package es.ies.puerto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula una ciudad en peligro siendo salvada por Superman y Batman concurrentemente
 */
public class CiudadEnPeligro extends SimulacionBase {

    private static final String BATAM = "Batam";
    private static final String SUPERMAN = "Superman";

    private final AtomicBoolean amenazaNeutralizada;
    private List<String> zonasA;
    private List<String> zonasB;
    private final AtomicReference<String> ganador;

    /**
     * Constructor por defecto
     */
    public CiudadEnPeligro() {
        this.amenazaNeutralizada = new AtomicBoolean(false);
        this.zonasA = new ArrayList<>(Arrays.asList("Norte", "Centro", "Este"));
        this.zonasB = new ArrayList<>(Arrays.asList("Oeste", "Sur"));
        this.ganador = new AtomicReference<>(null);
    }

    /**
     * Getters
     */
    public boolean isAmenazaNeutralizada() {
        return amenazaNeutralizada.get();
    }

    public String getGanador() {
        return ganador.get();
    }

    /**
     * Metodo que representa el hilo de un superheroe
     * @param superHerore nombre del superheroe
     * @return Runnable que representa al superheroe
     */
    private Runnable hilo(String superHerore) {
        return () -> {
            try {
                List<String> zonas;
                int tiempo;
                if (superHerore == SUPERMAN) {
                    zonas = zonasA;
                    tiempo = ThreadLocalRandom.current().nextInt(200, 501);
                } else {
                    zonas = zonasB;
                    tiempo = ThreadLocalRandom.current().nextInt(300, 601);
                }
                for (String zona : zonas) {
                    if (amenazaNeutralizada.get()) break;
                    Thread.sleep(tiempo);
                    if (amenazaNeutralizada.get()) break;
                    System.out.println(superHerore + " salvó " + zona);
                }
                if (!amenazaNeutralizada.get()) {
                    amenazaNeutralizada.set(true);
                    ganador.set(superHerore);
                    System.out.println("Amenaza neutralizada por " + superHerore);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Metodo que representa el hilo de Superman
     * @return Runnable que representa a Superman
     */
    private Runnable superman() {
        return hilo(SUPERMAN);
    }

    /**
     * Metodo que representa el hilo de Batman
     * @return Runnable que representa a Batman
     */
    private Runnable batman() {
        return hilo(BATAM);
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        Thread superman = new Thread(superman());
        Thread batman = new Thread(batman());
        return new Thread[] { superman, batman };
    }

    public static void main(String[] args) {
        CiudadEnPeligro ciudad = new CiudadEnPeligro();
        ciudad.ejecutarSimulacion();
    }
}
