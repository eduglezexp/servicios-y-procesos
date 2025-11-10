package es.ies.puerto.abstractas;

import es.ies.puerto.interfaces.SimulacionConcurrente;
import es.ies.puerto.utils.ThreadUtils;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

public abstract class SimulacionBase implements SimulacionConcurrente{

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
        ThreadUtils.ejecutarYEsperar(crearHilos());
    }
}
