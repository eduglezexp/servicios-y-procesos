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
  * Clase que simula una carrera de viajeros en el tiempo con la TARDIS concurrentemente
  */
public class Tardis extends SimulacionBase {

    private final AtomicBoolean destinoAlcanzado;
    private final AtomicReference<String> eraGanadora;

    /**
     * Constructor por defecto
     */
    public Tardis() {
        this.destinoAlcanzado = new AtomicBoolean(false);
        this.eraGanadora = new AtomicReference<>(null);
    }

    /**
     * Getters
     */
    public boolean isDestinoAlcanzado() {
        return destinoAlcanzado.get();
    }

    public String getEraGanadora() {
        return eraGanadora.get();
    }

    /**
     * Metodo que representa un viajero en el tiempo
     * @param era nombre de la era a la que viaja
     * @return Runnable que representa el viajero
     */
    private Runnable viajero(String era) {
        return () -> {
            try {
                int tiempo = ThreadLocalRandom.current().nextInt(500, 2001);
                Thread.sleep(tiempo);
                if (!destinoAlcanzado.get()) {
                    destinoAlcanzado.set(true);
                    eraGanadora.set(era);
                    System.out.println("La TARDIS llegó primero a " + era);
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
        Thread roma = new Thread(viajero("Roma Antigua"));
        Thread futuro = new Thread(viajero("Futuro Lejano"));
        Thread era = new Thread(viajero("Era Victoriana"));
        Thread anio = new Thread(viajero("Año 3000"));
        return new Thread[]{ roma, futuro, era, anio };
    }

    public static void main(String[] args) {
        Tardis tardis = new Tardis();
        tardis.ejecutarSimulacion();
    }
}
