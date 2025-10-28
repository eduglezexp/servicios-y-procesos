package org.formacion.procesos.services.abstractas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.formacion.procesos.domain.Job;
import org.formacion.procesos.repositories.interfaces.IJobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ComandoServiceAbstract {
    private String comando;
    private Job tipo;
    private String validacion;

    IJobRepository fileRepository;

    public IJobRepository getFileRepository() {
        return fileRepository;
    }

    @Autowired
    public void setFileRepository(IJobRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Job getTipo() {
        return tipo;
    }

    public String getTipoToString() {
        if (tipo == null) {
            return null;
        }
        return tipo.toString();
    }

    public void setTipo(Job tipo) {
        this.tipo = tipo;
    }

    public String getValidacion() {
        return validacion;
    }

    public void setValidacion(String validacion) {
        this.validacion = validacion;
    }

    public void procesarLinea(String linea) {
        String[] arrayComando = linea.split("\s+");
        this.setComando(arrayComando[0]);
        if (!validar(arrayComando)) {
            return;
        }
        try {
            Process proceso = new ProcessBuilder("sh", "-c", linea + " > " +fileRepository.getPath())
            .start();
            ejecutarProceso(proceso);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean ejecutarProceso(Process proceso) {
        try {
            proceso.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean validar(String[] arrayComando) {
        if (!validarComando()) {
            return false;
        }
        if (arrayComando.length -1 == 0) {
            return true;
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

    public boolean validarComando() {
        if (!this.getComando().toUpperCase().equals(getTipoToString())) {
            System.out.println("El comando es invalido");
            return false;
        }
        return true;
    }
}
