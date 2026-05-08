package com.becker.biblioteca.pattern.factory;

import com.becker.biblioteca.model.Dvd;
import com.becker.biblioteca.model.ItemAcervo;

import java.util.UUID;

public final class FabricaDvd extends FabricaItem {

    @Override
    protected void validarRequisicao(RequisicaoItem requisicao) {
        if (requisicao.getDiretor() == null || requisicao.getDiretor().isBlank())
            throw new IllegalArgumentException("diretor não pode ser vazio para Dvd");
        if (requisicao.getDuracaoMinutos() <= 0)
            throw new IllegalArgumentException("duração deve ser positiva para Dvd");
    }

    @Override
    protected ItemAcervo executarCriacao(RequisicaoItem requisicao) {
        return new Dvd(UUID.randomUUID().toString(),
                requisicao.getTitulo(), requisicao.getDiretor(), requisicao.getDuracaoMinutos());
    }
}
