package org.formacion.procesos.services.impl;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class TopServiceImpl extends ComandoServiceAbstract {

    public TopServiceImpl() {
        this.setTipo(Job.TOP);
        this.setValidacion("^(\\s*|(-?()))$");
    }
}
