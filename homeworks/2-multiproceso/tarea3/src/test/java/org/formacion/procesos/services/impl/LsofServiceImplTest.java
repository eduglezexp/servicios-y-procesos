package org.formacion.procesos.services.impl;

import org.formacion.procesos.repositories.file.FileJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LsofServiceImplTest {
    LsofServiceImpl lsofServiceImpl;

    @BeforeEach
    void beforeEach(){
        lsofServiceImpl = new LsofServiceImpl();
        lsofServiceImpl.setComando("lsof");
        lsofServiceImpl.setFileRepository(new FileJobRepository());
    }

    @Test
    void validarTest(){
        String[] arrayCommand = {"lsof","-i"};
        boolean validar = lsofServiceImpl.validar(arrayCommand);
        Assertions.assertTrue(validar, "error de validacion");
    }

    @Test
    void validarMenosTest(){
        String[] arrayComando = {"lsof","-"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

        @Test
    void validarVacioTest(){
        String[] arrayComando = {"lsof"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida,"se ha producido un error en la validacion");
    }
    @Test
    void validarFalseTest(){
        String[] arrayComando = {"lsof","aaaaaa"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void validarFalse2Test(){
        String[] arrayComando = {"lsof","-dadadad"};
        boolean valida =lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida,"se ha producido un error en la validacion");
    }

    @Test
    void procesarLineaTest(){
        String linea = "lsof -i";
        boolean procesado = lsofServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }

    @Test
    void procesarLineaMalTest(){
        String linea = "lsoaf -i";
        boolean procesado = lsofServiceImpl.procesarLinea(linea);
        Assertions.assertFalse(procesado, "error al procesar linea");
    }

        @Test
    void procesarLineaSoloTest(){
        String linea = "lsof ";
        boolean procesado = lsofServiceImpl.procesarLinea(linea);
        Assertions.assertTrue(procesado, "error al procesar linea");
    }
}
