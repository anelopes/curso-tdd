package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class AvaliadorTest {

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {

        //parte 1: cenário
        var joao = new Usuario("João");
        var jose = new Usuario("José");
        var maria = new Usuario("Maria");

        var leilao = new Leilao("Playstation 3 Novo");
        leilao.propoe(new Lance(joao, 250.0));
        leilao.propoe(new Lance(jose, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //parte 3: validação
        assertEquals(400.0, avaliador.getMaiorLance(), 0.00001);
        assertEquals(250.0, avaliador.getMenorLance(), 0.00001);

    }

    @Test
    public void testaMediaDeZeroLance() {

        //parte 1: cenário
        var ewerton = new Usuario("Ewerton");

        var leilao = new Leilao("iPhone 7");

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //parte 3: validação
        assertEquals(0, avaliador.getValorMedioLances(), 0.00001);

    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance() {

        //parte 1: cenário
        var joao = new Usuario("João");

        var leilao = new Leilao("iPhone 7");
        leilao.propoe(new Lance(joao, 1000.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //parte 3: validação
        assertEquals(1000.0, avaliador.getMaiorLance(), 0.00001);
        assertEquals(1000.0, avaliador.getMenorLance(), 0.00001);

    }

    @Test
    public void deveEncontrarOsTresMaioresLancesComCincoLances() {

        //parte 1: cenário
        var joao = new Usuario("João");
        var maria = new Usuario("Maria");

        var leilao = new Leilao("iPhone 7");
        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));
        leilao.propoe(new Lance(joao, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        var maiores = avaliador.getTresMaiores();

        //parte 3: validação
        assertEquals(3, maiores.size());
        assertEquals(400.0, maiores.get(0).getValor(), 0.0001);
        assertEquals(300.0, maiores.get(1).getValor(), 0.0001);
        assertEquals(200.0, maiores.get(2).getValor(), 0.0001);

    }

    @Test
    public void deveEncontrarOsTresMaioresLancesComDoisLances() {

        //parte 1: cenário
        var joao = new Usuario("João");
        var maria = new Usuario("Maria");

        var leilao = new Leilao("iPhone 7");
        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        var maiores = avaliador.getTresMaiores();

        //parte 3: validação
        assertEquals(2, maiores.size());
        assertEquals(200.0, maiores.get(0).getValor(), 0.0001);
        assertEquals(100.0, maiores.get(1).getValor(), 0.0001);

    }

    @Test
    public void deveEncontrarOsTresMaioresLancesSemNenhumLance() {

        //parte 1: cenário

        var leilao = new Leilao("iPhone 7");

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        var maiores = avaliador.getTresMaiores();

        //parte 3: validação
        assertEquals(0, maiores.size());

    }

    @Test
    public void deveEncontrarLancesEmOrdemAleatoria() {

        //parte 1: cenário
        var joao = new Usuario("João");
        var maria = new Usuario("Maria");

        var leilao = new Leilao("iPhone 7");
        leilao.propoe(new Lance(joao, 200.0));
        leilao.propoe(new Lance(maria, 450.0));
        leilao.propoe(new Lance(joao, 120.0));
        leilao.propoe(new Lance(maria, 700.0));
        leilao.propoe(new Lance(joao, 630.0));
        leilao.propoe(new Lance(maria, 230.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //parte 3: validação
        assertEquals(700.0, avaliador.getMaiorLance(), 0.00001);
        assertEquals(120.0, avaliador.getMenorLance(), 0.00001);

    }

    @Test
    public void deveEntenderLancesEmOrdemDecrescente() {

        //parte 1: cenário
        var joao = new Usuario("João");
        var jose = new Usuario("José");
        var maria = new Usuario("Maria");

        var leilao = new Leilao("Playstation 3 Novo");
        leilao.propoe(new Lance(joao, 400.0));
        leilao.propoe(new Lance(jose, 300.0));
        leilao.propoe(new Lance(maria, 200.0));
        leilao.propoe(new Lance(joao, 100.0));

        //parte 2: acão
        var avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //parte 3: validação
        assertEquals(400.0, avaliador.getMaiorLance(), 0.00001);
        assertEquals(100.0, avaliador.getMenorLance(), 0.00001);

    }
}
