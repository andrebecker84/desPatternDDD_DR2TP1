package com.becker.biblioteca.pattern.strategy;

import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;

import java.util.List;

public final class BuscaAutor implements EstrategiaBusca<ItemAcervo> {

    @Override
    public List<ItemAcervo> buscar(String query, List<ItemAcervo> lista) {
        String q = query.toLowerCase();
        return lista.stream()
                .filter(item -> item instanceof Livro livro
                        && livro.getAutor().toLowerCase().contains(q))
                .toList();
    }
}
