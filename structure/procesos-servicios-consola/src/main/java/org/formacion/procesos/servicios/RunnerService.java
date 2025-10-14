package org.formacion.procesos.servicios;

import java.util.Scanner;

import org.formacion.procesos.controller.ComandoLsController;
import org.formacion.procesos.controller.ComandoPsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunnerService {

    @Autowired
    ComandoPsController comandoPsController;

    @Autowired
    ComandoLsController comandoLsController;

    public void menuConsola() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Lanzador de Procesos (CLI) Linux/Windows ===\n" +
                "Comandos:\n" +
                "  run PING host=8.8.8.8 count=4 timeoutMs=15000\n" +
                "  run LIST_DIR path=.\n" +
                "  run HASH_SHA256 file=README.md\n" +
                "  help | os | exit\n");
        String linea = scanner.nextLine();
        if (linea.toUpperCase().startsWith("PS")) {
            comandoPsController.procesarLinea(linea);
        }
        comandoLsController.procesarLinea(linea);
    }

    private void helpConsola() {
        System.out.println(
                "Ejemplos\n" +
                        "run PING host=8.8.8.8 count=4\n" +
                        "run LIST_DIR path=.\n" +
                        "run HASH_SHA256 file=README.md timeoutMs=5000\n");
    }
}
