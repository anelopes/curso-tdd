package br.com.caelum.matematica;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatematicaMalucaTest {

    @Test
    void contaMaluca() {
        var matematica = new MatematicaMaluca();

        assertEquals(100*4, matematica.contaMaluca(100));
        assertEquals(30*3, matematica.contaMaluca(30));
        assertEquals(15*3, matematica.contaMaluca(15));
        assertEquals(10*2, matematica.contaMaluca(10));
        assertEquals(5*2, matematica.contaMaluca(5));
    }

    @Test
    void deveMultiplicarNumerosMaioresQue30() {
        var matematica = new MatematicaMaluca();
        assertEquals(50 * 4, matematica.contaMaluca(50));
    }

    @Test
    void deveMultiplicarNumerosMaioresQue10EMenoresQue30() {
        var matematica = new MatematicaMaluca();
        assertEquals(20 * 3, matematica.contaMaluca(20));
    }

    @Test
    void deveMultiplicarNumerosMenoresQue10() {
        var matematica = new MatematicaMaluca();
        assertEquals(5 * 2, matematica.contaMaluca(5));
    }
}
