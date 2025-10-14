package org.formacion.procesos.controller;

import org.formacion.procesos.controller.abstractas.ComandoControllerAbstract;
import org.formacion.procesos.domain.ProcessType;
import org.springframework.stereotype.Component;

@Component
public class ComandoLsController extends ComandoControllerAbstract{

    public ComandoLsController() {
        this.setTipo(ProcessType.LS);
    }
}
