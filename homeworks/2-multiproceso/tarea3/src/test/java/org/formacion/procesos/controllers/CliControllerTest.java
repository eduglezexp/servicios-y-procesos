package org.formacion.procesos.controllers;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;

import org.formacion.procesos.services.impl.LsofServiceImpl;
import org.formacion.procesos.services.impl.PsServiceImpl;
import org.formacion.procesos.services.impl.TopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CliControllerTest {

    private CliController cliController;
    private PsServiceImpl psService;
    private LsofServiceImpl lsofService;
    private TopServiceImpl topService;

    @BeforeEach
    void setUp() {
        cliController = new CliController();
        psService = mock(PsServiceImpl.class);
        lsofService = mock(LsofServiceImpl.class);
        topService = mock(TopServiceImpl.class);

        cliController.setComandoPsController(psService);
        cliController.setComandoLsofController(lsofService);
        cliController.setComandoTopContoller(topService);
    }

    @Test
    void testMenuConsola_psCommand() {
        String simulatedInput = "ps aux | head\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cliController.menuConsola();
        verify(psService).procesarLinea("ps aux | head");
        verifyNoInteractions(lsofService, topService);
    }

    @Test
    void testMenuConsola_topCommand() {
        String simulatedInput = "top\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cliController.menuConsola();
        verify(topService).procesarLinea("top");
        verifyNoInteractions(lsofService, psService);
    }

    @Test
    void testMenuConsola_lsofCommand() {
        String simulatedInput = "lsof -i\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        cliController.menuConsola();
        verify(lsofService).procesarLinea("lsof -i");
        verifyNoInteractions(psService, topService);
    }
}

