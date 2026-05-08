package com.becker.biblioteca.dto;

import com.becker.biblioteca.model.Emprestimo;
import com.becker.biblioteca.model.StatusEmprestimo;

import java.time.LocalDate;

public record RespostaEmprestimo(
        String id,
        String itemId,
        String tituloItem,
        String usuarioId,
        String nomeUsuario,
        LocalDate dataEmprestimo,
        LocalDate dataVencimento,
        StatusEmprestimo status
) {
    public static RespostaEmprestimo de(Emprestimo emprestimo) {
        return new RespostaEmprestimo(
                emprestimo.getId(),
                emprestimo.getItem().getId(),
                emprestimo.getItem().getTitulo(),
                emprestimo.getUsuario().getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataVencimento(),
                emprestimo.getStatus()
        );
    }
}
