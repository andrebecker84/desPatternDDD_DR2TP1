package com.becker.biblioteca.pattern.factory;

import com.becker.biblioteca.model.ItemAcervo;

import java.util.Objects;

/**
 * Factory Method — define o contrato de criação de itens do acervo.
 * Subclasses decidem qual tipo concreto instanciar (Livro, Dvd, etc.),
 * eliminando a necessidade de alterações no código cliente ao adicionar novos tipos.
 *
 * Aplica Template Method: validar → criar (princípio OCP).
 */
public abstract class FabricaItem {

    public final ItemAcervo criar(RequisicaoItem requisicao) {
        Objects.requireNonNull(requisicao, "requisição não pode ser nula");
        Objects.requireNonNull(requisicao.getTitulo(), "título não pode ser nulo");
        if (requisicao.getTitulo().isBlank()) throw new IllegalArgumentException("título não pode ser vazio");
        validarRequisicao(requisicao);
        return executarCriacao(requisicao);
    }

    protected abstract void validarRequisicao(RequisicaoItem requisicao);

    protected abstract ItemAcervo executarCriacao(RequisicaoItem requisicao);
}
