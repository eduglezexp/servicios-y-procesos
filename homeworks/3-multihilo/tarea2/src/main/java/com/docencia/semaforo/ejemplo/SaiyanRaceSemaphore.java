package com.docencia.semaforo.ejemplo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRaceSemaphore implements Runnable {
    private final String name;
    private int distance = 0;

    private static final int GOAL = 100;

    private static final Semaphore turnGoku = new Semaphore(1);  
    private static final Semaphore turnVegeta = new Semaphore(0); 

    private static final AtomicBoolean winnerDeclared = new AtomicBoolean(false);

    public SaiyanRaceSemaphore(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        final boolean isGoku = "Goku".equals(name);
        final Semaphore myTurn = isGoku ? turnGoku : turnVegeta;
        final Semaphore otherTurn = isGoku ? turnVegeta : turnGoku;

        try {
            while (!winnerDeclared.get() && distance < GOAL) {
                myTurn.acquire();
                if (winnerDeclared.get()) {
                    otherTurn.release(); 
                    break;
                }
                int step = ThreadLocalRandom.current().nextInt(1, 11);
                distance += step;
                System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

                if (distance >= GOAL) {
                    if (winnerDeclared.compareAndSet(false, true)) {
                        System.out.println(name + " ha ganado la carrera!");
                    }
                }
                otherTurn.release();
                
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    otherTurn.release();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            (isGoku ? turnVegeta : turnGoku).release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread goku = new Thread(new SaiyanRaceSemaphore("Goku"));
        Thread vegeta = new Thread(new SaiyanRaceSemaphore("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();
        vegeta.join();
    }
}
