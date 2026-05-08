package com.becker.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "usuarios")
@Getter
public class Usuario {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    protected Usuario() {}

    public Usuario(String id, String nome, String email) {
        Objects.requireNonNull(id,    "id não pode ser nulo");
        Objects.requireNonNull(nome,  "nome não pode ser nulo");
        Objects.requireNonNull(email, "email não pode ser nulo");
        if (nome.isBlank())  throw new IllegalArgumentException("nome não pode ser vazio");
        if (email.isBlank()) throw new IllegalArgumentException("email não pode ser vazio");
        this.id    = id;
        this.nome  = nome;
        this.email = email;
    }

    public void atualizarNome(String novoNome) {
        Objects.requireNonNull(novoNome, "nome não pode ser nulo");
        if (novoNome.isBlank()) throw new IllegalArgumentException("nome não pode ser vazio");
        this.nome = novoNome;
    }

    public void atualizarEmail(String novoEmail) {
        Objects.requireNonNull(novoEmail, "email não pode ser nulo");
        if (novoEmail.isBlank()) throw new IllegalArgumentException("email não pode ser vazio");
        this.email = novoEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario outro)) return false;
        return Objects.equals(id, outro.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Usuario[id=" + id + ", nome=" + nome + ", email=" + email + "]";
    }
}
