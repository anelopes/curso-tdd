package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.dao.RepositorioDePagamentos;
import br.com.caelum.leilao.infra.relogio.Relogio;
import br.com.caelum.leilao.infra.relogio.RelogioDoSistema;

import java.util.Calendar;
import java.util.List;

public class GeradorDePagamento {

    private RepositorioDeLeiloes leiloes;
    private RepositorioDePagamentos pagamentos;
    private Avaliador avaliador;
    private Relogio relogio;

    public GeradorDePagamento(RepositorioDeLeiloes leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador, Relogio relogio) {
        this.leiloes = leiloes;
        this.pagamentos = pagamentos;
        this.avaliador = avaliador;
        this.relogio = relogio;
    }

    public GeradorDePagamento(RepositorioDeLeiloes leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador) {
        this(leiloes, pagamentos, avaliador, new RelogioDoSistema());
    }

    public void gera() {
        List<Leilao> leiloesEncerrados = this.leiloes.encerrados();

        leiloesEncerrados.forEach(leilaoEncerrado -> {
            this.avaliador.avalia(leilaoEncerrado);

            var novoPagamento = new Pagamento(avaliador.getMaiorLance(), primeiroDiaUtil());
            this.pagamentos.salva(novoPagamento);
        });
    }

    private Calendar primeiroDiaUtil() {
        var calendar = relogio.hoje();
        var dayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else if (dayWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar;
    }
}
