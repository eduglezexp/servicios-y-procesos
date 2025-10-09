package org.formacion.procesos.component;

import org.formacion.procesos.component.interfaces.IFicheroComponent;
import org.formacion.procesos.repository.interfaces.IAlmacenamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("component1")
public class FicheroComponent implements IFicheroComponent{

    @Autowired
    @Qualifier("baseDatosRepository")
    IAlmacenamientoRepository baseDatosRepository;

    @Autowired
    @Qualifier("ficheroRepository")
    IAlmacenamientoRepository ficheroRepository;

    @Override
    public String mensaje() {
        return baseDatosRepository.saludar() 
        + "\n" + ficheroRepository.saludar();
    }
}
