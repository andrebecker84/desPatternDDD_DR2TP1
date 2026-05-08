package com.becker.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "emprestimos")
@Getter
public class Emprestimo {

    private static final int PRAZO_PADRAO_DIAS = 14;

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemAcervo item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, updatable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false, updatable = false)
    private LocalDate dataVencimento;

    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status;

    protected Emprestimo() {}

    public Emprestimo(String id, ItemAcervo item, Usuario usuario) {
        Objects.requireNonNull(id,      "id não pode ser nulo");
        Objects.requireNonNull(item,    "item não pode ser nulo");
        Objects.requireNonNull(usuario, "usuário não pode ser nulo");
        this.id              = id;
        this.item            = item;
        this.usuario         = usuario;
        this.dataEmprestimo  = LocalDate.now();
        this.dataVencimento  = dataEmprestimo.plusDays(PRAZO_PADRAO_DIAS);
        this.status          = StatusEmprestimo.ATIVO;
    }

    public boolean isVencido() {
        return status == StatusEmprestimo.ATIVO && LocalDate.now().isAfter(dataVencimento);
    }

    public void registrarDevolucao() {
        if (status == StatusEmprestimo.DEVOLVIDO)
            throw new IllegalStateException("Empréstimo já devolvido: " + id);
        this.dataDevolucao = LocalDate.now();
        this.status        = StatusEmprestimo.DEVOLVIDO;
    }

    public void marcarVencido() {
        if (isVencido()) this.status = StatusEmprestimo.VENCIDO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emprestimo outro)) return false;
        return Objects.equals(id, outro.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Emprestimo[id=" + id + ", item=" + item.getTitulo()
                + ", usuario=" + usuario.getNome()
                + ", vencimento=" + dataVencimento + ", status=" + status + "]";
    }
}
