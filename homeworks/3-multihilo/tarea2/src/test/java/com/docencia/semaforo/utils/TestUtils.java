package com.docencia.semaforo.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author eduglezexp
 * @version 1.0.0
 */


/**
 * Clase de utilidades para tests unitarios que capturan la salida de System.out
 */
public class TestUtils {

    private static PrintStream originalOut;
    private static ByteArrayOutputStream outContent;

    /**
     * Redirige la salida estandar a un buffer interno
     */
    public static void capturarSalida() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Metodo que restaura la salida estandar original
     */
    public static void restaurarSalida() {
        System.setOut(originalOut);
    }

    /**
     * Metodo que devuelve la salida capturada como String
     */
    public static String obtenerSalida() {
        return outContent.toString();
    }

    /**
     * Metodo que ejecuta un bloque de codigo capturando la salida de System.out.
     * @param runnable bloque de codigo a ejecutar (ej. la simulacion)
     * @return la salida capturada durante la ejecucion
     */
    public static String ejecutarConSalida(Runnable runnable) {
        capturarSalida();
        try {
            runnable.run();
        } finally {
            restaurarSalida();
        }
        return obtenerSalida();
    }
}
