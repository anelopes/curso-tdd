package br.com.caelum.leilao.builder;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

import java.util.Calendar;

public class CriadorDeLeilao {
    private Leilao leilao;

    public CriadorDeLeilao para(String descricao) {
        this.leilao = new Leilao(descricao);
        return this;
    }

    public CriadorDeLeilao lance(Usuario usuario, double valor) {
        leilao.propoe(new Lance(usuario, valor));
        return this;
    }

    public CriadorDeLeilao naData(Calendar data) {
        leilao.setData(data);
        return this;
    }

    public Leilao constroi() {
        return this.leilao;
    }
}
