package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.dao.RepositorioDePagamentos;
import br.com.caelum.leilao.infra.relogio.Relogio;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GeradorDePagamentoTest {

    private RepositorioDeLeiloes leiloes;
    private RepositorioDePagamentos pagamentos;
    private Relogio relogio;
    private Avaliador avaliador;
    private Leilao leilao;
    private Usuario jose;
    private Usuario maria;

    @Before
    public void setUp() {
        this.leiloes = mock(RepositorioDeLeiloes.class);
        this.pagamentos = mock(RepositorioDePagamentos.class);
        this.relogio = mock(Relogio.class);
        this.avaliador = new Avaliador();

        this.jose = new Usuario("Jose da Silva");
        this.maria = new Usuario("Maria Pereira");

        this.leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(this.jose, 2000.0)
                .lance(this.maria, 2500.0)
                .constroi();
    }

    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado() {
        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

        var gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador);
        gerador.gera();

        var argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());

        var pagamentoGerado = argumento.getValue();

        assertEquals(2500.0, pagamentoGerado.getValor(), 0.00001);
    }

    @Test
    public void deveEmpurrarParaOProximoDiaUtil() {
        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

        var sabado = Calendar.getInstance();
        sabado.set(2012, Calendar.APRIL, 7);

        when(relogio.hoje()).thenReturn(sabado);

        var gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador, relogio);
        gerador.gera();

        var argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());

        var pagamentoGerado = argumento.getValue();

        assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
        assertEquals(9, pagamentoGerado.getData().get(Calendar.DAY_OF_MONTH));

    }

    @Test
    public void deveEmpurrarParaOProximoDiaUtilDomingo() {
        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

        var domingo = Calendar.getInstance();
        domingo.set(2012, Calendar.APRIL, 8);

        when(relogio.hoje()).thenReturn(domingo);

        var gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador, relogio);
        gerador.gera();

        var argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());

        var pagamentoGerado = argumento.getValue();

        assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
        assertEquals(9, pagamentoGerado.getData().get(Calendar.DAY_OF_MONTH));

    }
}
