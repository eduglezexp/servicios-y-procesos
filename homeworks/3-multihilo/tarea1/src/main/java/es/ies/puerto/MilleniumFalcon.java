package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import es.ies.puerto.abstractas.SimulacionBase;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula la nave Millenium Falcon con Han Solo y Chewbacca concurrentemente
 */
public class MilleniumFalcon extends SimulacionBase{
    private final AtomicBoolean fin;
    private final AtomicBoolean destruida;
    private final AtomicInteger velocidad;
    private final AtomicInteger escudos;

    private final int tiempoMisionMS = 4000;
    private long inicio;

    /**
     * Constructor por defecto
     */
    public MilleniumFalcon() {
        this.fin = new AtomicBoolean(false);
        this.destruida = new AtomicBoolean(false);
        this.velocidad = new AtomicInteger(0);
        this.escudos = new AtomicInteger(100);
    }

    /**
     * Metodo que crea un tripulante con una accion y una condicion de destruccion
     * @param accion accion que realiza el tripulante
     * @param condicionDestruccion condicion que provoca la destruccion de la nave
     * @param mensajeDestruccion mensaje que se muestra cuando la nave se destruye
     * @return Runnable que representa al tripulante
     */
    private Runnable crearTripulante(Runnable accion, Supplier<Boolean> condicionDestruccion, String mensajeDestruccion) {
        return () -> {
            while (!fin.get()) {
                accion.run();
                if (condicionDestruccion.get()) {
                    destruida.set(true);
                    fin.set(true);
                    System.out.println(mensajeDestruccion);
                }
                dormir(150);
                if (System.currentTimeMillis() - inicio >= tiempoMisionMS) {
                    fin.set(true);
                }
            }
        };
    }

    /**
     * Metodo que representa el hilo de Han Solo
     * @return Runnable que representa a Han Solo
     */
    private Runnable hanSolo() {
        int velocidadRandom = ThreadLocalRandom.current().nextInt(5, 16);
        return crearTripulante(
            () -> velocidad.addAndGet(velocidadRandom),
            () -> ThreadLocalRandom.current().nextInt(1, 101) <= 5,
            "Fallo de hiperimpulsor. ¡La nave se destruye!"
        );
    }

    /**
     * Metodo que representa el hilo de Chewbacca
     * @return Runnable que representa a Chewbacca
     */
    private Runnable chewbacca() {
        int escudosRandom = ThreadLocalRandom.current().nextInt(-10, 6);
        return crearTripulante(
            () -> escudos.addAndGet(escudosRandom),
            () -> escudos.get() <= 0,
            "¡Escudos agotados! ¡La nave se destruye!"
        );
    }

    /**
     * Metodo que duerme el hilo actual un numero de milisegundos dado
     * @param milisegundos milisegundos a dormir
     */
    private void dormir(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    @Override
    public Thread[] crearHilos() {
        inicio = System.currentTimeMillis();
        Thread hanSoloThread = new Thread(hanSolo());
        Thread chewbaccaThread = new Thread(chewbacca());
        return new Thread[] { hanSoloThread, chewbaccaThread };
    }

    /**
     * Metodo que ejecuta la simulacion y muestra el resultado final
     */
    @Override
    public void ejecutarSimulacion() {
        super.ejecutarSimulacion();
        if (!destruida.get()) {
            System.out.println("¡Han y Chewie escapan! Vel=" + velocidad + ", Escudos=" + escudos);
        }
    }

    public static void main(String[] args) {
        MilleniumFalcon falcon = new MilleniumFalcon();
        falcon.ejecutarSimulacion();
    }
}
