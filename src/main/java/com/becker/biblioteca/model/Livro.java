package com.becker.biblioteca.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.Objects;

@Entity
@DiscriminatorValue("LIVRO")
@Getter
public class Livro extends ItemAcervo {

    private String autor;
    private String isbn;

    protected Livro() {}

    public Livro(String id, String titulo, String autor, String isbn) {
        super(id, titulo);
        Objects.requireNonNull(autor, "autor não pode ser nulo");
        Objects.requireNonNull(isbn, "isbn não pode ser nulo");
        if (autor.isBlank()) throw new IllegalArgumentException("autor não pode ser vazio");
        if (isbn.isBlank())  throw new IllegalArgumentException("isbn não pode ser vazio");
        this.autor = autor;
        this.isbn  = isbn;
    }

    @Override public String getTipo()     { return "LIVRO"; }
    @Override public String getDescricao() { return "Livro de " + autor + " (ISBN: " + isbn + ")"; }
}
