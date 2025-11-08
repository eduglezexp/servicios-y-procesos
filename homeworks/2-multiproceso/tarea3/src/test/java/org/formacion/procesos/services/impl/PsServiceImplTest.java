package org.formacion.procesos.services.impl;

import org.formacion.procesos.repositories.file.FileJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PsServiceImplTest {
    PsServiceImpl psServiceImpl;

    @BeforeEach
    void beforeEach(){
        psServiceImpl = new PsServiceImpl();
        psServiceImpl.setComando("ps");
        psServiceImpl.setFileRepository(new FileJobRepository());

    }

    @Test
    void validarTest(){
        String[] arrayComando = {"ps","aux | head"};
        boolean validar = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(validar, "error de validacion");
    }

    @Test
    void validarconMenosTest(){
        String[] arrayCommand = {"ps","-aux | head"};
        boolean validar = psServiceImpl.validar(arrayCommand);
        Assertions.assertTrue(validar, "error de validacion");
    }

    @Test
    void validarVacioTest(){
        String[] arrayComando = {"ps"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida,"se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest(){
        String[] arrayComando = {"ps","au |"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void validarFalse2Test(){
        String[] arrayComando = {"ps","-"};
        boolean valida =psServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void procesarLineaTest(){
        String linea = "ps aux | head";
        boolean procesado = psServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }

    @Test
    void procesarLineaMalTest(){
        String linea = "pas au / head";
        boolean procesado = psServiceImpl.procesarLinea(linea);
        Assertions.assertFalse(procesado, "error al procesar linea");
    }
    
    @Test
    void procesarLineaSoloTest(){
        String linea = "ps ";
        boolean procesado = psServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }
}
