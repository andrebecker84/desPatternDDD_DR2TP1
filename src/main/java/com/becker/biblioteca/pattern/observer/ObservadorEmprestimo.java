package com.becker.biblioteca.pattern.observer;

import com.becker.biblioteca.model.Emprestimo;

/**
 * Observer — contrato para receber notificações de eventos de empréstimo.
 * Qualquer implementação é elegível para receber alertas sem alterar classes existentes (OCP).
 */
public interface ObservadorEmprestimo {
    void aoEmprestimo(Emprestimo emprestimo);
    void aoDevolucao(Emprestimo emprestimo);
    void aoVencimento(Emprestimo emprestimo);
}
