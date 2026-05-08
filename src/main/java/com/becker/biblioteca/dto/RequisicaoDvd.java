package com.becker.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RequisicaoDvd(
        @NotBlank(message = "título obrigatório")   String titulo,
        @NotBlank(message = "diretor obrigatório")  String diretor,
        @Positive(message = "duração deve ser positiva") int duracaoMinutos
) {}
