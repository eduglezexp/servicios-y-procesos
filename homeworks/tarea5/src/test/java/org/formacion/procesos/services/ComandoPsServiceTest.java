package org.formacion.procesos.services;

import org.formacion.procesos.services.impl.PsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComandoPsServiceTest {
    PsServiceImpl psServiceImpl;

    @BeforeEach
    void beforeEach() {
        psServiceImpl = new PsServiceImpl();
        psServiceImpl.setComando("ps");
    }

    @Test
    void validarAuxTest() {
        String[] arrayComando = {"ps", "aux"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarAuxMenosTest() {
        String[] arrayComando = {"ps", "-aux"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarAuxHeadMenosTest() {
        String[] arrayComando = {"ps", "-aux", "|", "head"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarMenosVacioTest() {
        String[] arrayComando = {"ps", "-"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarVacioTest() {
        String[] arrayComando = {"ps", " "};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarSinVacioTest() {
        String[] arrayComando = {"ps", ""};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseSinGuionTest() {
        String[] arrayComando = {"ps", "lalala"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest() {
        String[] arrayComando = {"ps", "-lalala"};
        boolean valida = psServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }
}
