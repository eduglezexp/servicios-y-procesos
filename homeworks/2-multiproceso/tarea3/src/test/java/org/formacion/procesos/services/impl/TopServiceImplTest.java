package org.formacion.procesos.services.impl;

import org.formacion.procesos.repositories.file.FileJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TopServiceImplTest {
    TopServiceImpl topServiceImpl;

    @BeforeEach
    void beforeEach(){
        topServiceImpl = new TopServiceImpl();
        topServiceImpl.setComando("top");
        topServiceImpl.setFileRepository(new FileJobRepository());

    }

    @Test
    void validarTest(){
        String[] arrayComando = {"top","-bn1"};
        boolean validar = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(validar, "error de validacion");
    }

    @Test
    void validarMenosTest(){
        String[] arrayComando = {"top","-"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

        @Test
    void validarVacioTest(){
        String[] arrayComando = {"top"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida,"se ha producido un error en la validacion");
    }
    @Test
    void validarFalseTest(){
        String[] arrayComando = {"top","iiiiii"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void validarFalse2Test(){
        String[] arrayComando = {"top","-iiiiii"};
        boolean valida =topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void procesarLineaTest(){
        String linea = "top -bn1";
        boolean procesado = topServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }

    @Test
    void procesarLineaMalTest(){
        String linea = "tapy -i";
        boolean procesado = topServiceImpl.procesarLinea(linea);
        Assertions.assertFalse(procesado, "error al procesar linea");
    }

    @Test
    void procesarLineaSoloTest(){
        String linea = "top ";
        boolean procesado = topServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }
}
