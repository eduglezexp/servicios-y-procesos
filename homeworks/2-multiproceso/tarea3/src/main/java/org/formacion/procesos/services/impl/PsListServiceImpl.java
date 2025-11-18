package org.formacion.procesos.services.impl;

import java.util.List;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstractList;

public class PsListServiceImpl extends ComandoServiceAbstractList{
    private static final List<String> EXPRESION_REGULAR = List.of("aux", "|","head");

    public PsListServiceImpl() {
        super(Job.PS, EXPRESION_REGULAR);
    }
}
