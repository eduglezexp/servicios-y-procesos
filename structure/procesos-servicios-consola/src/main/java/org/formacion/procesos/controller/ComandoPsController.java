package org.formacion.procesos.controller;

import org.formacion.procesos.controller.abstractas.ComandoControllerAbstract;
import org.formacion.procesos.domain.ProcessType;
import org.springframework.stereotype.Component;

@Component
public class ComandoPsController extends ComandoControllerAbstract {

    public ComandoPsController() {
        this.setTipo(ProcessType.PS);
    }
}
