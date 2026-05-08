package com.becker.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "itens_acervo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_item", discriminatorType = DiscriminatorType.STRING)
@Getter
public abstract class ItemAcervo {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private boolean disponivel;

    protected ItemAcervo() {}

    protected ItemAcervo(String id, String titulo) {
        Objects.requireNonNull(id, "id não pode ser nulo");
        Objects.requireNonNull(titulo, "título não pode ser nulo");
        if (titulo.isBlank()) throw new IllegalArgumentException("título não pode ser vazio");
        this.id        = id;
        this.titulo    = titulo;
        this.disponivel = true;
    }

    public void retirar() {
        if (!disponivel) throw new IllegalStateException("Item indisponível para empréstimo: " + id);
        this.disponivel = false;
    }

    public void devolver() { this.disponivel = true; }

    public abstract String getTipo();
    public abstract String getDescricao();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemAcervo outro)) return false;
        return Objects.equals(id, outro.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return getTipo() + "[id=" + id + ", titulo=" + titulo + ", disponivel=" + disponivel + "]";
    }
}
