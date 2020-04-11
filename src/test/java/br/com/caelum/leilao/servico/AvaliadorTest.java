package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


public class AvaliadorTest {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario jose;
    private Usuario maria;

    @Before
    public void setUp() {
        this.leiloeiro = new Avaliador();
        this.joao = new Usuario("João");
        this.jose = new Usuario("José");
        this.maria = new Usuario("Maria");
    }

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {

        //parte 1: cenário
        var leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 250.0)
                .lance(jose, 300.0)
                .lance(maria, 400.0)
                .constroi();

        //parte 2: acão
        leiloeiro.avalia(leilao);

        //parte 3: validação
        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance() {

        //parte 1: cenário
        var joao = new Usuario("João");

        var leilao = new Leilao("iPhone 7");
        leilao.propoe(new Lance(joao, 1000.0));

        //parte 2: acão
        leiloeiro.avalia(leilao);

        //parte 3: validação

        assertThat(leiloeiro.getMaiorLance(), equalTo(1000.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(1000.0));
    }

    @Test
    public void deveEncontrarOsTresMaioresLancesComCincoLances() {

        //parte 1: cenário
        var leilao = new CriadorDeLeilao().para("iPhone 7")
                .lance(joao, 100.0)
                .lance(maria, 200.0)
                .lance(joao, 300.0)
                .lance(maria, 400.0)
                .constroi();

        //parte 2: acão
        leiloeiro.avalia(leilao);

        var maiores = leiloeiro.getTresMaiores();

        //parte 3: validação
        assertThat(maiores.size(), equalTo(3));
        assertThat(maiores, hasItems(
                new Lance(maria, 400.0),
                new Lance(joao, 300.0),
                new Lance(maria, 200.0)
        ));
    }

    @Test
    public void deveEncontrarOsTresMaioresLancesComDoisLances() {

        //parte 1: cenário
        var leilao = new CriadorDeLeilao().para("iPhone 7")
                .lance(joao, 100.0)
                .lance(jose, 200.0)
                .constroi();

        //parte 2: acão
        leiloeiro.avalia(leilao);

        var maiores = leiloeiro.getTresMaiores();

        //parte 3: validação
        assertThat(maiores.size(), equalTo(2));
        assertThat(maiores, hasItems(
                new Lance(jose, 200),
                new Lance(joao, 100.0)
        ));
    }

    @Test
    public void deveEncontrarLancesEmOrdemAleatoria() {

        //parte 1: cenário
        var leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 200.0)
                .lance(maria, 450.0)
                .lance(joao, 120.0)
                .lance(maria, 700.0)
                .lance(joao, 630.0)
                .lance(maria, 230.0)
                .constroi();

        //parte 2: acão
        leiloeiro.avalia(leilao);

        //parte 3: validação
        assertThat(leiloeiro.getMaiorLance(), equalTo(700.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(120.0));
    }

    @Test
    public void deveEntenderLancesEmOrdemDecrescente() {

        //parte 1: cenário
        var leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 400.0)
                .lance(jose, 300.0)
                .lance(maria, 200.0)
                .lance(joao, 100.0)
                .constroi();

        //parte 2: acão
        leiloeiro.avalia(leilao);

        //parte 3: validação
        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(100.0));
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
        Leilao leilao = new CriadorDeLeilao()
                .para("Playstation 3 Novo")
                .constroi();

        leiloeiro.avalia(leilao);
    }
}
