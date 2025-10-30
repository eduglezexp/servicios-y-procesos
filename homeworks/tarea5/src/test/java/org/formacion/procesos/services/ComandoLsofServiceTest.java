package org.formacion.procesos.services;

import org.formacion.procesos.services.impl.LsofServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ComandoLsofServiceTest {
    LsofServiceImpl lsofServiceImpl;

    @BeforeEach
    void beforeEach() {
        lsofServiceImpl = new LsofServiceImpl();
        lsofServiceImpl.setComando("lsof");
    }

    @Test
    void validarLaTest() {
        String[] arrayComando = {"lsof", "-i"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertTrue(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarMenosVacioTest() {
        String[] arrayComando = {"lsof", "-"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarVacioFalseTest() {
        String[] arrayComando = {"lsof", " "};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarSinVacioFalseTest() {
        String[] arrayComando = {"lsof", ""};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseSinGuionTest() {
        String[] arrayComando = {"lsof", "lalala"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }

    @Test
    void validarFalseTest() {
        String[] arrayComando = {"lsof", "-lalala"};
        boolean valida = lsofServiceImpl.validar(arrayComando);
        Assertions.assertFalse(valida, "Se ha producido un error en la validacion");
    }
}
