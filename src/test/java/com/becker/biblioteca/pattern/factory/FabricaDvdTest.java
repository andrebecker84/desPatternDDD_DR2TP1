package com.becker.biblioteca.pattern.factory;

import com.becker.biblioteca.model.Dvd;
import com.becker.biblioteca.model.ItemAcervo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FabricaDvdTest {

    private FabricaDvd fabrica;

    @BeforeEach
    void setUp() { fabrica = new FabricaDvd(); }

    @Test
    void criar_deveRetornarDvd() {
        RequisicaoItem req = RequisicaoItem.paraDvd("Inception")
                .diretor("Christopher Nolan").duracaoMinutos(148).build();

        ItemAcervo item = fabrica.criar(req);

        assertThat(item).isInstanceOf(Dvd.class);
        assertThat(item.getTitulo()).isEqualTo("Inception");
        assertThat(item.getTipo()).isEqualTo("DVD");
        assertThat(((Dvd) item).getDiretor()).isEqualTo("Christopher Nolan");
        assertThat(((Dvd) item).getDuracaoMinutos()).isEqualTo(148);
    }

    @Test
    void criar_deveLancarExcecao_quandoDiretorVazio() {
        RequisicaoItem req = RequisicaoItem.paraDvd("Filme")
                .diretor("").duracaoMinutos(120).build();
        assertThatThrownBy(() -> fabrica.criar(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("diretor");
    }

    @Test
    void criar_deveLancarExcecao_quandoDuracaoZero() {
        RequisicaoItem req = RequisicaoItem.paraDvd("Filme")
                .diretor("Diretor").duracaoMinutos(0).build();
        assertThatThrownBy(() -> fabrica.criar(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duração");
    }
}
