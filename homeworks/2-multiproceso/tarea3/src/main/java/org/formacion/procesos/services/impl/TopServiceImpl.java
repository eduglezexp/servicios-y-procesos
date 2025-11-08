package org.formacion.procesos.services.impl;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

@Component
public class TopServiceImpl extends ComandoServiceAbstract {

    /**
     * Constructor por defecto
     */
    public TopServiceImpl() {
        this.setTipo(Job.TOP);
        this.setValidacion("^((-(bn1))|\\s*)$");
    }
}
