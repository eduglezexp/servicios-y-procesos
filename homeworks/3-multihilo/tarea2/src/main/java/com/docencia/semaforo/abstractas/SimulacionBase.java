package com.docencia.semaforo.abstractas;

import com.docencia.semaforo.interfaces.SimulacionConcurrente;
import com.docencia.semaforo.utils.ThreadUtils;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public abstract class SimulacionBase implements SimulacionConcurrente {

    /**
     * Metodo que crea los hilos necesarios para la simulacion
     * @return array de hilos
     */
    public abstract Thread[] crearHilos();
    
    /**
     * Metodo que ejecuta la simulacion concurrente
     */
    @Override
    public void ejecutarSimulacion() {
        ThreadUtils.ejecutarHilos(crearHilos());
    }
}
