package br.com.caelum.leilao.infra.dao;

import br.com.caelum.leilao.dominio.Pagamento;

import java.util.List;

public interface RepositorioDePagamentos {

    void salva(Pagamento leilao);

    List<Pagamento> encerrados();

    List<Pagamento> correntes();

    void atualiza(Pagamento leilao);
}
