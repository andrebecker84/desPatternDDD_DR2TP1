package com.becker.biblioteca.pattern.observer;

import com.becker.biblioteca.model.Emprestimo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Concrete Subject — gerencia a lista de observadores e despacha eventos de empréstimo.
 * Thread-safe via CopyOnWriteArrayList: leituras são predominantes sobre escritas.
 */
@Component
public class DespachadorEventos implements SujetoEmprestimo {

    private final List<ObservadorEmprestimo> observadores = new CopyOnWriteArrayList<>();

    @Override
    public void inscrever(ObservadorEmprestimo observador)        { observadores.add(observador); }

    @Override
    public void cancelarInscricao(ObservadorEmprestimo observador) { observadores.remove(observador); }

    @Override
    public void notificarCriacao(Emprestimo emprestimo) {
        observadores.forEach(o -> o.aoEmprestimo(emprestimo));
    }

    @Override
    public void notificarDevolucao(Emprestimo emprestimo) {
        observadores.forEach(o -> o.aoDevolucao(emprestimo));
    }

    @Override
    public void notificarVencimento(Emprestimo emprestimo) {
        observadores.forEach(o -> o.aoVencimento(emprestimo));
    }
}
