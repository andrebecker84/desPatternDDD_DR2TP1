package com.becker.biblioteca.pattern.factory;

import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;

import java.util.UUID;

public final class FabricaLivro extends FabricaItem {

    @Override
    protected void validarRequisicao(RequisicaoItem requisicao) {
        if (requisicao.getAutor() == null || requisicao.getAutor().isBlank())
            throw new IllegalArgumentException("autor não pode ser vazio para Livro");
        if (requisicao.getIsbn() == null || requisicao.getIsbn().isBlank())
            throw new IllegalArgumentException("isbn não pode ser vazio para Livro");
    }

    @Override
    protected ItemAcervo executarCriacao(RequisicaoItem requisicao) {
        return new Livro(UUID.randomUUID().toString(),
                requisicao.getTitulo(), requisicao.getAutor(), requisicao.getIsbn());
    }
}
