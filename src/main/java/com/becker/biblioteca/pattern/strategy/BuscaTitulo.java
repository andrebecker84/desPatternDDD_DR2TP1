package com.becker.biblioteca.pattern.strategy;

import com.becker.biblioteca.model.ItemAcervo;

import java.util.List;

public final class BuscaTitulo implements EstrategiaBusca<ItemAcervo> {

    @Override
    public List<ItemAcervo> buscar(String query, List<ItemAcervo> lista) {
        String q = query.toLowerCase();
        return lista.stream()
                .filter(item -> item.getTitulo().toLowerCase().contains(q))
                .toList();
    }
}
