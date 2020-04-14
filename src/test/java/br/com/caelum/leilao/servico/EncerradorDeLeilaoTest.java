package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.email.EnviadorDeEmail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EncerradorDeLeilaoTest {

    private Calendar ontem;
    private Calendar antiga;
    private Leilao leilao1;
    private Leilao leilao2;
    private Leilao leilao3;
    private RepositorioDeLeiloes leilaoDao;
    private EnviadorDeEmail carteiro;
    private EncerradorDeLeilao encerrador;

    @Before
    public void setUp() {
        var antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);
        this.antiga = antiga;

        var ontem = Calendar.getInstance();
        ontem.add(Calendar.DATE, -1);
        this.ontem = ontem;

        this.leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(this.antiga).constroi();
        this.leilao2 = new CriadorDeLeilao().para("Playstation 3 Novo").naData(this.antiga).constroi();
        this.leilao3 = new CriadorDeLeilao().para("Geladeira").naData(this.ontem).constroi();

        this.leilaoDao = mock(RepositorioDeLeiloes.class);
        this.carteiro = mock(EnviadorDeEmail.class);
        this.encerrador = new EncerradorDeLeilao(this.leilaoDao, this.carteiro);
    }

    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

        when(leilaoDao.correntes()).thenReturn(leiloesAntigos);

        encerrador.encerra();

        assertEquals(2, encerrador.getTotalEncerrados());
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeilaoQueComecouOntem() {
        List<Leilao> leiloesOntem = Arrays.asList(leilao3);

        when(leilaoDao.correntes()).thenReturn(leiloesOntem);

        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao3.isEncerrado());

        verify(leilaoDao, never()).atualiza(leilao3);
    }

    @Test
    public void naoDeveFazerNadaCasoNaoHajaNenhumLeilao() {
        when(leilaoDao.correntes()).thenReturn(new ArrayList<>());

        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
    }

    @Test
    @Ignore //não é possível testar métodos staticos
    public void testeString() {
        when(LeilaoDao.teste()).thenReturn("teste");
    }

    @Test
    public void deveAtualizarLeiloesEncerrados() {
        when(leilaoDao.correntes()).thenReturn(Arrays.asList(leilao1));

        encerrador.encerra();

        // passamos os mocks que serao verificados
        InOrder inOrder = inOrder(leilaoDao, carteiro);
        // a primeira invocação
        inOrder.verify(leilaoDao, times(1)).atualiza(leilao1);
        // a segunda invocação
        inOrder.verify(carteiro, times(1)).envia(leilao1);
    }

    @Test
    public void deveContinuarAExecucaoMesmoQuandoDaoFalha() {

        when(leilaoDao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(leilaoDao).atualiza(leilao1);

        encerrador.encerra();

        verify(leilaoDao).atualiza(leilao2);
        verify(carteiro).envia(leilao2);

        verify(carteiro, times(0)).envia(leilao1);
    }

    @Test
    public void deveContinuarAExecucaoMesmoQuandoEnviadorDeEmaillFalha() {
        when(leilaoDao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(carteiro).envia(leilao1);

        encerrador.encerra();

        verify(leilaoDao).atualiza(leilao2);
        verify(carteiro).envia(leilao2);
    }

    @Test
    public void naoDeveInvocarCarteiro() {

        when(leilaoDao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(leilaoDao).atualiza(any(Leilao.class));

        encerrador.encerra();

        verify(carteiro, never()).envia(any(Leilao.class));
    }
}
