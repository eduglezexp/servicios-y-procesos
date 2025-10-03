package org.formacion.procesos;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

/**
 * @author eduglezexp
 * @version 1.0.0
 */

@Component
public class Procesos {

    private final static String PATH_MIS_PROCESOS = "src/main/resources/mis_procesos.txt";

    /**
     * Funcion para guardar los procesos encontrados de java 
     * en un archivo txt (ps aux | grep java > mis_procesos.txt)
     */
    private void guardarProcesosJava() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        ProcessBuilder processBuilder;
        if (isWindows) {
            processBuilder = new ProcessBuilder(
                    "cmd.exe", "/c", "tasklist | findstr java > " + PATH_MIS_PROCESOS);
        } else {
            processBuilder = new ProcessBuilder(
                    "sh", "-c", "ps aux | grep java > " + PATH_MIS_PROCESOS);
        }
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Funcion para mostrar cuantas lineas tiene un archivo
     * (wc -l mis_procesos.txt) y si hay mas de 3 procesos,
     * imprime "¡Cuidado, muchos procesos de Java activos!"
     * @param nombreArchivo es el nombre del archivo del que 
     * queremos contar el numero de lineas
     */
    private void mostrarNumeroLineasArchivo(String nombreArchivo) {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            ProcessBuilder processBuilder;
            if (isWindows) {
                processBuilder = new ProcessBuilder(
                        "cmd.exe", "/c", "find /c \"java\" \"" + nombreArchivo + "\"");
            } else {
                processBuilder = new ProcessBuilder(
                        "sh", "-c", "wc -l \"" + nombreArchivo + "\"");
            }
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null && !line.isBlank()) {
                    int numeroLineas;
                    if (isWindows) {
                        String[] parts = line.trim().split("\\s+|:");
                        numeroLineas = Integer.parseInt(parts[parts.length - 1]);
                    } else {
                        String[] parts = line.trim().split("\\s+");
                        numeroLineas = Integer.parseInt(parts[0]);
                    }
                    System.out.println("Numero de lineas en " + nombreArchivo + ": " + numeroLineas);
                    if (numeroLineas > 3) {
                        System.out.println("¡Cuidado, muchos procesos de Java activos!");
                    }
                }
                reader.lines().forEach(System.out::println);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al mostrar el numero de lineas del archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Funcion que crea un archivo si no existe y llama a 
     * las funciones guardarProcesosJava(); y 
     * mostrarNumeroLineasArchivo(String nombreArchivo)
     */
    public void ejecutar() {
        File archivo = new File(PATH_MIS_PROCESOS);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            guardarProcesosJava();
            mostrarNumeroLineasArchivo(archivo.getPath());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
