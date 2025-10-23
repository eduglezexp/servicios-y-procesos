package org.formacion.procesos.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileRepositoryTest {

    private static final String NO_SE_HA_OBTENIDO_EL_RESULTADO_ESPERADO = "No se ha obtenido el resultado esperado";
    static FileRepository fileRepository;

    @BeforeAll
    static void beforeAll() {
        fileRepository = new FileRepository();
        fileRepository.setFileName("fichero-test.txt");
    }

    @Test
    void addContenido() {
        boolean resultado = fileRepository.add("texto");
        Assertions.assertTrue(resultado, NO_SE_HA_OBTENIDO_EL_RESULTADO_ESPERADO);
    }
}
