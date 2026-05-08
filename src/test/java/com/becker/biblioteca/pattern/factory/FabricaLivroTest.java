package com.becker.biblioteca.pattern.factory;

import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FabricaLivroTest {

    private FabricaLivro fabrica;

    @BeforeEach
    void setUp() { fabrica = new FabricaLivro(); }

    @Test
    void criar_deveRetornarLivro() {
        RequisicaoItem req = RequisicaoItem.paraLivro("Código Limpo")
                .autor("Robert C. Martin").isbn("978-0132350884").build();

        ItemAcervo item = fabrica.criar(req);

        assertThat(item).isInstanceOf(Livro.class);
        assertThat(item.getTitulo()).isEqualTo("Código Limpo");
        assertThat(item.getTipo()).isEqualTo("LIVRO");
        assertThat(item.isDisponivel()).isTrue();
        assertThat(((Livro) item).getAutor()).isEqualTo("Robert C. Martin");
        assertThat(((Livro) item).getIsbn()).isEqualTo("978-0132350884");
    }

    @Test
    void criar_deveGerarIdsUnicos() {
        RequisicaoItem req = RequisicaoItem.paraLivro("Domain-Driven Design")
                .autor("Eric Evans").isbn("978-0321125217").build();
        ItemAcervo l1 = fabrica.criar(req);
        ItemAcervo l2 = fabrica.criar(req);
        assertThat(l1.getId()).isNotEqualTo(l2.getId());
    }

    @Test
    void criar_deveLancarExcecao_quandoRequisicaoNula() {
        assertThatThrownBy(() -> fabrica.criar(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void criar_deveLancarExcecao_quandoAutorVazio() {
        RequisicaoItem req = RequisicaoItem.paraLivro("Título")
                .autor("").isbn("123").build();
        assertThatThrownBy(() -> fabrica.criar(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("autor");
    }

    @Test
    void criar_deveLancarExcecao_quandoIsbnVazio() {
        RequisicaoItem req = RequisicaoItem.paraLivro("Título")
                .autor("Autor").isbn("").build();
        assertThatThrownBy(() -> fabrica.criar(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("isbn");
    }
}
