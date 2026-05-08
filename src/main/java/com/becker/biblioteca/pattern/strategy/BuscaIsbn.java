package com.becker.biblioteca.pattern.strategy;

import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;

import java.util.List;

public final class BuscaIsbn implements EstrategiaBusca<ItemAcervo> {

    @Override
    public List<ItemAcervo> buscar(String query, List<ItemAcervo> lista) {
        String isbn = query.trim();
        return lista.stream()
                .filter(item -> item instanceof Livro livro
                        && livro.getIsbn().equalsIgnoreCase(isbn))
                .toList();
    }
}
