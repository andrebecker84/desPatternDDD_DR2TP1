package com.becker.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoLivro(
        @NotBlank(message = "título obrigatório") String titulo,
        @NotBlank(message = "autor obrigatório")  String autor,
        @NotBlank(message = "isbn obrigatório")   String isbn
) {}
