package es.ies.puerto;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.*;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

/**
 * Clase que simula una batalla entre dos pokemons: Pikachu y Charmander
 */
public class BatallaPokemon {

    public final AtomicBoolean juegoTerminado;
    public int hpPikachu = 100;
    public int hpCharmander = 100;
    private String turno = "Pikachu";

    private final ReentrantLock m;
    private final Condition turnoCambio;

    private static final String PIKACHU = "Pikachu";
    private static final String CHARMANDER = "Charmander";

    /**
     * Constructor por defecto
     */
    public BatallaPokemon() {
        this.juegoTerminado = new AtomicBoolean(false);
        this.m = new ReentrantLock();
        this.turnoCambio = m.newCondition();
    }

    /**
     * Metodo que simula un ataque de un pokemon a otro
     * @param atacante nombre del pokemon que ataca
     * @param objetivo nombre del pokemon que recibe el ataque
     */
    private void atacar(String atacante, String objetivo) {
        int danio = ThreadLocalRandom.current().nextInt(5, 21);
        if (objetivo.equals(CHARMANDER)) {
            hpCharmander -= danio;
            System.out.println(atacante + " ataca con " + danio + " de daño. HP de Charmander: " + hpCharmander);
        } else {
            hpPikachu -= danio;
            System.out.println(atacante + " ataca con " + danio + " de daño. HP de Pikachu: " + hpPikachu);
        }
    }

    /**
     * Metodo que comprueba si un pokemon ha ganado la batalla
     * @param atacante nombre del pokemon que ataca
     * @param objetivo nombre del pokemon que recibe el ataque
     */
    private void comprobarVictoria(String atacante, String objetivo) {
        if ((objetivo.equals(CHARMANDER) && hpCharmander <= 0) ||
            (objetivo.equals(PIKACHU) && hpPikachu <= 0)) {
            if (juegoTerminado.compareAndSet(false, true)) {
                System.out.println(atacante + " ha ganado la batalla!");
            }
        }
    }

    /**
     * Metodo que representa el hilo de un pokemon atacando a otro
     * @param atacante nombre del pokemon que ataca
     * @param objetivo nombre del pokemon que recibe el ataque
     * @return Runnable que representa el hilo del pokemon atacando a otro
     */
    private Runnable hilo(String atacante, String objetivo) {
        return () -> {
            while (!juegoTerminado.get()) {
                try {
                    m.lock();
                    while (!turno.equals(atacante) && !juegoTerminado.get()) {
                        turnoCambio.await();
                    }
                    if (juegoTerminado.get()) break;
                    atacar(atacante, objetivo);
                    comprobarVictoria(atacante, objetivo);
                    turno = objetivo;
                    turnoCambio.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    m.unlock();
                }
            }
        };
    }

    /**
     * Metodo que representa el hilo de Pikachu atacando a Charmander
     * @return Runnable que representa el hilo de Pikachu atacando a Charmander
     */
    public Runnable hiloPikachu() {
        return hilo(PIKACHU, CHARMANDER);
    }

    /**
     * Metodo que representa el hilo de Charmander atacando a Pikachu
     * @return Runnable que representa el hilo de Charmander atacando a Pikachu
     */
    public Runnable hiloCharmander() {
        return hilo(CHARMANDER, PIKACHU);
    }

    public static void main(String[] args) throws InterruptedException {
        BatallaPokemon batalla = new BatallaPokemon();

        Thread pikachu = new Thread(batalla.hiloPikachu());
        Thread charmander = new Thread(batalla.hiloCharmander());

        pikachu.start();
        charmander.start();

        pikachu.join();
        charmander.join();
    }
}
