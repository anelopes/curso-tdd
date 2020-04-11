package br.com.caelum.leilao.dominio;

import org.junit.Test;

public class LanceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testaLanceComValorZero() {
        new Lance(new Usuario("Steve Jobs"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaLanceComValorMenorQueZero() {
        new Lance(new Usuario("Steve Jobs"), -1);
    }

}
