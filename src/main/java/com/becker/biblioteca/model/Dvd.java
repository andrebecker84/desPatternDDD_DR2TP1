package com.becker.biblioteca.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.Objects;

@Entity
@DiscriminatorValue("DVD")
@Getter
public class Dvd extends ItemAcervo {

    private String diretor;
    private int duracaoMinutos;

    protected Dvd() {}

    public Dvd(String id, String titulo, String diretor, int duracaoMinutos) {
        super(id, titulo);
        Objects.requireNonNull(diretor, "diretor não pode ser nulo");
        if (diretor.isBlank())     throw new IllegalArgumentException("diretor não pode ser vazio");
        if (duracaoMinutos <= 0)   throw new IllegalArgumentException("duração deve ser positiva");
        this.diretor       = diretor;
        this.duracaoMinutos = duracaoMinutos;
    }

    @Override public String getTipo()     { return "DVD"; }
    @Override public String getDescricao() { return "DVD dirigido por " + diretor + " (" + duracaoMinutos + " min)"; }
}
