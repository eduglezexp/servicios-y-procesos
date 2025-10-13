package org.formacion.procesos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProcesosServiciosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcesosServiciosApplication.class, args);
    }

    @Bean
    CommandLineRunner demo() {
        return args -> {
            System.out.println("Iniciando proceso al arrancar la aplicación...");

            System.out.println("Proceso finalizado.");
        };
    }
}