package org.formacion.procesos.services;

import org.formacion.procesos.services.impl.TopServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComandoTopServiceTest {
    TopServiceImpl topServiceImpl;

    @BeforeEach
    void beforeEach() {
        topServiceImpl = new TopServiceImpl();
        topServiceImpl.setComando("top");
    }

    @Test
    void validarBTest() {
        String[] arrayComando = {"top", "-b"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarBN1Test() {
        String[] arrayComando = {"top", "-b", "-n1"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarBFalseSinGuionTest() {
        String[] arrayComando = {"top", "-b", "adsada"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarBFalseTest() {
        String[] arrayComando = {"top", "-b", "-adsada"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarMenosVacioTest() {
        String[] arrayComando = {"top", "-"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarVacioTest() {
        String[] arrayComando = {"top", " "};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarSinVacioTest() {
        String[] arrayComando = {"top", ""};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseSinGuionTest() {
        String[] arrayComando = {"top", "lalala"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest() {
        String[] arrayComando = {"top", "-lalala"};
        boolean valida = topServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }
}
