package com.becker.biblioteca.pattern.observer;

import com.becker.biblioteca.model.Emprestimo;
import com.becker.biblioteca.model.Livro;
import com.becker.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DespachadorEventosTest {

    private DespachadorEventos despachador;
    private ObservadorRegistrador observador;
    private Emprestimo emprestimo;

    @BeforeEach
    void setUp() {
        despachador = new DespachadorEventos();
        observador  = new ObservadorRegistrador();
        despachador.inscrever(observador);

        Livro livro    = new Livro("l1", "Refactoring", "Martin Fowler", "978-0201485677");
        Usuario usuario = new Usuario("u1", "João Silva", "joao@email.com");
        emprestimo = new Emprestimo("e1", livro, usuario);
    }

    @Test
    void notificarCriacao_deveChamarObservador() {
        despachador.notificarCriacao(emprestimo);
        assertThat(observador.criados).containsExactly(emprestimo);
    }

    @Test
    void notificarDevolucao_deveChamarObservador() {
        despachador.notificarDevolucao(emprestimo);
        assertThat(observador.devolvidos).containsExactly(emprestimo);
    }

    @Test
    void notificarVencimento_deveChamarObservador() {
        despachador.notificarVencimento(emprestimo);
        assertThat(observador.vencidos).containsExactly(emprestimo);
    }

    @Test
    void cancelarInscricao_devePararDeReceberEventos() {
        despachador.cancelarInscricao(observador);
        despachador.notificarCriacao(emprestimo);
        assertThat(observador.criados).isEmpty();
    }

    @Test
    void multiplosObservadores_devemTodosReceberEventos() {
        ObservadorRegistrador segundo = new ObservadorRegistrador();
        despachador.inscrever(segundo);
        despachador.notificarCriacao(emprestimo);
        assertThat(observador.criados).hasSize(1);
        assertThat(segundo.criados).hasSize(1);
    }

    private static class ObservadorRegistrador implements ObservadorEmprestimo {
        final List<Emprestimo> criados   = new ArrayList<>();
        final List<Emprestimo> devolvidos = new ArrayList<>();
        final List<Emprestimo> vencidos  = new ArrayList<>();

        @Override public void aoEmprestimo(Emprestimo e) { criados.add(e); }
        @Override public void aoDevolucao(Emprestimo e)  { devolvidos.add(e); }
        @Override public void aoVencimento(Emprestimo e) { vencidos.add(e); }
    }
}
