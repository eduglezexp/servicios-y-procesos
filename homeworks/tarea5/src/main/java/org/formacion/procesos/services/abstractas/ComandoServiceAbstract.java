package org.formacion.procesos.services.abstractas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
            Process proceso = new ProcessBuilder("sh", "-c", linea).start();
            Path outputFile = fileRepository.getPath();
            Path errorFile = Paths.get(outputFile.toString().replace(".txt", "_err.txt"));
            ejecutarProceso(proceso, outputFile, errorFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean ejecutarProceso(Process proceso, Path outputPath, Path errorPath) {
        try (OutputStream outStream = Files.newOutputStream(outputPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            OutputStream errStream = Files.newOutputStream(errorPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            BufferedReader outReader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(proceso.getErrorStream()))) {

            String outLine;
            String errLine = null;

            while ((outLine = outReader.readLine()) != null || (errLine = errReader.readLine()) != null) {
                if (outLine != null) {
                    System.out.println("[OUT] " + outLine);
                    outStream.write((outLine + "\n").getBytes());
                }
                if (errLine != null) {
                    System.err.println("[ERR] " + errLine);
                    errStream.write((errLine + "\n").getBytes());
                }
            }

            int exitCode = proceso.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
