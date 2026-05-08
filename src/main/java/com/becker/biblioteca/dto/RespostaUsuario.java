package com.becker.biblioteca.dto;

import com.becker.biblioteca.model.Usuario;

public record RespostaUsuario(String id, String nome, String email) {

    public static RespostaUsuario de(Usuario usuario) {
        return new RespostaUsuario(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
