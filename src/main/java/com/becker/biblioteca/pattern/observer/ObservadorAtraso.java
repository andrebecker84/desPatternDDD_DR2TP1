package com.becker.biblioteca.pattern.observer;

import com.becker.biblioteca.model.Emprestimo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer — registra logs para todos os eventos de empréstimo.
 * Novos canais (e-mail, SMS) são adicionados implementando ObservadorEmprestimo
 * sem alterar nenhuma classe existente (OCP).
 */
@Slf4j
@Component
public class ObservadorAtraso implements ObservadorEmprestimo {

    @Override
    public void aoEmprestimo(Emprestimo emprestimo) {
        log.info("Empréstimo criado: item='{}', usuário='{}', vencimento={}",
                emprestimo.getItem().getTitulo(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDataVencimento());
    }

    @Override
    public void aoDevolucao(Emprestimo emprestimo) {
        log.info("Devolução registrada: item='{}', usuário='{}'",
                emprestimo.getItem().getTitulo(),
                emprestimo.getUsuario().getNome());
    }

    @Override
    public void aoVencimento(Emprestimo emprestimo) {
        log.warn("EMPRÉSTIMO VENCIDO: item='{}', usuário='{}', vencimento={}, status={}",
                emprestimo.getItem().getTitulo(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDataVencimento(),
                emprestimo.getStatus());
    }
}
