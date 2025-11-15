package com.docencia.semaforo.ejercicio1;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public enum Colores {
    ROJO(3000),
    VERDE(3000),
    AMBAR(1000);

    private final int tiempo;
    
    /**
     * Construtor con la propiedad tiempo
     * @param tiempo del color
     */
    Colores(int tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Getters
     */
    public int getTiempo() {
        return tiempo;
    }

    /**
     * Metodo que devuelve el siguiente color
     * @return el siguiente color
     */
    public Colores siguiente() {
        return switch (this) {
            case ROJO -> VERDE;
            case VERDE -> AMBAR;
            case AMBAR -> ROJO;
        };
    }
}
