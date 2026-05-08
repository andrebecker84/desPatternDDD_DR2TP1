package com.becker.biblioteca.pattern.strategy;

import com.becker.biblioteca.model.Usuario;

import java.util.List;

public final class BuscaNomeUsuario implements EstrategiaBusca<Usuario> {

    @Override
    public List<Usuario> buscar(String query, List<Usuario> lista) {
        String q = query.toLowerCase();
        return lista.stream()
                .filter(u -> u.getNome().toLowerCase().contains(q))
                .toList();
    }
}
