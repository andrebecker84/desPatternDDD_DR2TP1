package com.becker.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequisicaoUsuario(
        @NotBlank(message = "nome obrigatório")          String nome,
        @NotBlank @Email(message = "email inválido")     String email
) {}
