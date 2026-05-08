package com.becker.biblioteca.pattern.observer;

import com.becker.biblioteca.model.Emprestimo;

/**
 * Subject (Observable) — contrato para o publicador de eventos de empréstimo.
 * Permite registro e remoção dinâmica de observadores sem acoplar o publicador
 * às implementações concretas.
 */
public interface SujetoEmprestimo {
    void inscrever(ObservadorEmprestimo observador);
    void cancelarInscricao(ObservadorEmprestimo observador);
    void notificarCriacao(Emprestimo emprestimo);
    void notificarDevolucao(Emprestimo emprestimo);
    void notificarVencimento(Emprestimo emprestimo);
}
