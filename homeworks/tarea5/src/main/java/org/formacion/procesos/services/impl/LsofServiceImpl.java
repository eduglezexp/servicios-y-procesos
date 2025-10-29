package org.formacion.procesos.services.impl;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class LsofServiceImpl extends ComandoServiceAbstract{

    public LsofServiceImpl() {
        this.setTipo(Job.LSOF);
        this.setValidacion("^(\\s*|(-(i)))$");
    }
}
