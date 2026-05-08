package com.becker.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoEmprestimo(
        @NotBlank(message = "itemId obrigatório")    String itemId,
        @NotBlank(message = "usuarioId obrigatório") String usuarioId
) {}
