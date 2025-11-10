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

    public void averiguarSuperHeroe() {
        
    }

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

    private Runnable superman() {
        return hilo(SUPERMAN);
    }

    private Runnable batman() {
        return hilo(BATAM);
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearHilos'");
    }

    public static void main(String[] args) {
        
    }
}
