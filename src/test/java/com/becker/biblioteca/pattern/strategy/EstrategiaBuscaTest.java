package com.becker.biblioteca.pattern.strategy;

import com.becker.biblioteca.model.Dvd;
import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;
import com.becker.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EstrategiaBuscaTest {

    private Livro livro1;
    private Livro livro2;
    private Dvd dvd;
    private List<ItemAcervo> itens;
    private Usuario usuario1;
    private Usuario usuario2;
    private List<Usuario> usuarios;

    @BeforeEach
    void setUp() {
        livro1  = new Livro("1", "Código Limpo", "Robert C. Martin", "978-0132350884");
        livro2  = new Livro("2", "Domain-Driven Design", "Eric Evans", "978-0321125217");
        dvd     = new Dvd("3", "Inception", "Christopher Nolan", 148);
        itens   = List.of(livro1, livro2, dvd);

        usuario1 = new Usuario("u1", "Ana Becker", "ana@email.com");
        usuario2 = new Usuario("u2", "Carlos Souza", "carlos@email.com");
        usuarios = List.of(usuario1, usuario2);
    }

    @Test
    void buscaTitulo_deveEncontrarItensCorrespondentes() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaTitulo());
        assertThat(ctx.buscar("código", itens)).containsExactly(livro1);
    }

    @Test
    void buscaTitulo_deveSerCaseInsensitive() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaTitulo());
        assertThat(ctx.buscar("INCEPTION", itens)).containsExactly(dvd);
    }

    @Test
    void buscaAutor_deveRetornarApenasLivros() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaAutor());
        assertThat(ctx.buscar("martin", itens)).containsExactly(livro1);
    }

    @Test
    void buscaAutor_naoDeveRetornarDvds() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaAutor());
        assertThat(ctx.buscar("nolan", itens)).isEmpty();
    }

    @Test
    void buscaIsbn_deveEncontrarCorrespondenciaExata() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaIsbn());
        assertThat(ctx.buscar("978-0321125217", itens)).containsExactly(livro2);
    }

    @Test
    void buscaIsbn_deveRetornarVazio_quandoIsbnInexistente() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaIsbn());
        assertThat(ctx.buscar("000-0000000000", itens)).isEmpty();
    }

    @Test
    void buscaNomeUsuario_deveEncontrarUsuario() {
        ContextoBusca<Usuario> ctx = new ContextoBusca<>(new BuscaNomeUsuario());
        assertThat(ctx.buscar("becker", usuarios)).containsExactly(usuario1);
    }

    @Test
    void contextoBusca_deveTrocarEstrategia() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaTitulo());
        assertThat(ctx.buscar("domain", itens)).containsExactly(livro2);

        ctx.definirEstrategia(new BuscaAutor());
        assertThat(ctx.buscar("evans", itens)).containsExactly(livro2);
    }

    @Test
    void buscaTitulo_deveRetornarVazio_quandoSemCorrespondencia() {
        ContextoBusca<ItemAcervo> ctx = new ContextoBusca<>(new BuscaTitulo());
        assertThat(ctx.buscar("xyz-nao-encontrado", itens)).isEmpty();
    }
}
