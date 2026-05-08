package com.becker.biblioteca.pattern.factory;

public final class RequisicaoItem {

    private final String titulo;
    private final String autor;
    private final String isbn;
    private final String diretor;
    private final int duracaoMinutos;

    private RequisicaoItem(Builder builder) {
        this.titulo        = builder.titulo;
        this.autor         = builder.autor;
        this.isbn          = builder.isbn;
        this.diretor       = builder.diretor;
        this.duracaoMinutos = builder.duracaoMinutos;
    }

    public String getTitulo()       { return titulo; }
    public String getAutor()        { return autor; }
    public String getIsbn()         { return isbn; }
    public String getDiretor()      { return diretor; }
    public int getDuracaoMinutos()  { return duracaoMinutos; }

    public static Builder paraLivro(String titulo) { return new Builder(titulo); }
    public static Builder paraDvd(String titulo)   { return new Builder(titulo); }

    public static final class Builder {
        private final String titulo;
        private String autor;
        private String isbn;
        private String diretor;
        private int duracaoMinutos;

        private Builder(String titulo) { this.titulo = titulo; }

        public Builder autor(String autor)                  { this.autor = autor; return this; }
        public Builder isbn(String isbn)                    { this.isbn = isbn; return this; }
        public Builder diretor(String diretor)              { this.diretor = diretor; return this; }
        public Builder duracaoMinutos(int duracaoMinutos)   { this.duracaoMinutos = duracaoMinutos; return this; }
        public RequisicaoItem build()                        { return new RequisicaoItem(this); }
    }
}
