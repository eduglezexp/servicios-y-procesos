package es.ies.puerto;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.*;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public class BatallaPokemon {

    private final AtomicBoolean juegoTerminado;
    private int hpPikachu;
    private int hpCharmander;
    private String turno;

    private final ReentrantLock m;
    private final Condition turnoCambio;

    /**
     * Constructor por defecto
     */
    public BatallaPokemon() {
        this.juegoTerminado = new AtomicBoolean(false);
        this.hpPikachu = 100;
        this.hpCharmander = 100;
        this.turno = "Pikachu";
        this.m = new ReentrantLock();
        this.turnoCambio = m.newCondition();
    }

    /**
     * Metodo papra atacar a un pokemon
     * @param atacante pokemon que ataca
     * @param hpObjetivo hp del pokemon objetivo
     */
    private void atacar(String atacante, int hpObjetivo) {
        int danio = ThreadLocalRandom.current().nextInt(5, 21);
        hpObjetivo -= danio;
        System.out.println(atacante + " ataca con " + danio + " de daño. HP rival: " + hpObjetivo);
    }

    private void comprobarVictoria(String atacante, int hpObjetivo) {
        if (hpObjetivo <= 0 && !juegoTerminado.get()) {
            juegoTerminado.set(true);
            System.out.println(atacante + " ha ganado la batalla!");
        }
    }

    private Runnable hilo(String atacante) {
        while (!juegoTerminado.get()) {
            try {
                m.lock();
                while (turno != atacante && !juegoTerminado.get()) {
                    turnoCambio.await();
                }
            } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
            } finally {
                m.unlock();
            }
        }
        return null;
    }

    /**
     * Metodo para ejecutar el hilo del pokemon Pikachu
     * @return 
     */
    private Runnable hiloPikachu() {
        return null;
    }

    /**
     * Metodo para ejecutar el hilo del pokemon Charmander
     * @return
     */
    private Runnable hiloCharmander() {
        return null;
    }

    public static void main(String[] args) {
        Thread pikachu = new Thread(hiloPikachu());
        Thread charmander = new Thread(hiloCharmander());
        pikachu.join();
        charmander.join();
    }
}
