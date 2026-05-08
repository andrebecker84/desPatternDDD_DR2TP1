package com.becker.biblioteca.pattern.strategy;

import java.util.List;
import java.util.Objects;

/**
 * Context — mantém a estratégia de busca ativa e delega a execução a ela.
 * Permite trocar o algoritmo em tempo de execução sem alterar o código cliente.
 */
public final class ContextoBusca<T> {

    private EstrategiaBusca<T> estrategia;

    public ContextoBusca(EstrategiaBusca<T> estrategiaInicial) {
        Objects.requireNonNull(estrategiaInicial, "estratégia não pode ser nula");
        this.estrategia = estrategiaInicial;
    }

    public void definirEstrategia(EstrategiaBusca<T> estrategia) {
        Objects.requireNonNull(estrategia, "estratégia não pode ser nula");
        this.estrategia = estrategia;
    }

    public List<T> buscar(String query, List<T> lista) {
        Objects.requireNonNull(query, "query não pode ser nula");
        Objects.requireNonNull(lista, "lista não pode ser nula");
        return estrategia.buscar(query, lista);
    }
}
