package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Avaliador {

    private double maiorDeTodos = Double.NEGATIVE_INFINITY;
    private double menorDeTodos = Double.POSITIVE_INFINITY;
    private double valorMedioLances;
    private List<Lance> maiores;

    public void avalia(Leilao leilao) {
        var valorTotalLances = new AtomicReference<>(0.0);
        leilao.getLances().forEach(lance -> {
            valorTotalLances.set(valorTotalLances.get() + lance.getValor());
            if (lance.getValor() > maiorDeTodos) {
                maiorDeTodos = lance.getValor();
            }
            if (lance.getValor() < menorDeTodos) {
                menorDeTodos = lance.getValor();
            }
        });

        if (valorTotalLances.get() != 0) {
            valorMedioLances = valorTotalLances.get() / leilao.getLances().size();
        }

        maiores = new ArrayList<>(leilao.getLances());
        Collections.sort(maiores, (o1, o2) -> {
            if (o1.getValor() < o2.getValor()) return 1;
            if (o1.getValor() > o2.getValor()) return -1;
            return 0;
        });

        maiores = maiores.subList(0, maiores.size() > 3 ? 3 : maiores.size());
    }

    public List<Lance> getTresMaiores() {
        return maiores;
    }

    public double getValorMedioLances() {
        return valorMedioLances;
    }

    public double getMaiorLance() {
        return maiorDeTodos;
    }

    public double getMenorLance() {
        return menorDeTodos;
    }
}
