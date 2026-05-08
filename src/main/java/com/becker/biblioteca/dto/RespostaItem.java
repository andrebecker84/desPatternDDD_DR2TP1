package com.becker.biblioteca.dto;

import com.becker.biblioteca.model.ItemAcervo;

public record RespostaItem(
        String id,
        String tipo,
        String titulo,
        boolean disponivel,
        String descricao
) {
    public static RespostaItem de(ItemAcervo item) {
        return new RespostaItem(
                item.getId(),
                item.getTipo(),
                item.getTitulo(),
                item.isDisponivel(),
                item.getDescricao()
        );
    }
}
