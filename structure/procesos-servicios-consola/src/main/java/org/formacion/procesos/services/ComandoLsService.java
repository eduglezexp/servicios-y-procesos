package org.formacion.procesos.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.formacion.procesos.domain.ProcessType;
import org.formacion.procesos.services.abstractas.ComandoServiceAbstract;
import org.springframework.stereotype.Component;

@Component
public class ComandoLsService extends ComandoServiceAbstract{

    private String validacion = "((-(la|l|a)))?$";

    public ComandoLsService() {
        this.setTipo(ProcessType.LS);
    }

    @Override
    public boolean validar(String[] arrayComando) {
        if (!super.validarComando()) {
            return false;
        }
        String parametro = arrayComando[1];
        Pattern pattern = Pattern.compile(validacion);
        Matcher matcher = pattern.matcher(parametro);
        if (!matcher.find()) {
            System.out.println("No cumple");
            return false;
        }
        return true;
    }
}
