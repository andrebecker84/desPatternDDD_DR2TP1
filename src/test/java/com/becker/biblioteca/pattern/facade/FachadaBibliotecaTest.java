package com.becker.biblioteca.pattern.facade;

import com.becker.biblioteca.model.Emprestimo;
import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.Livro;
import com.becker.biblioteca.model.Dvd;
import com.becker.biblioteca.model.StatusEmprestimo;
import com.becker.biblioteca.model.Usuario;
import com.becker.biblioteca.service.FachadaBiblioteca;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class FachadaBibliotecaTest {

    @Autowired
    private FachadaBiblioteca fachada;

    // ── Factory Method via Facade ────────────────────────────────────────────

    @Test
    void adicionarLivro_deveCriarLivroNoAcervo() {
        ItemAcervo item = fachada.adicionarLivro("Código Limpo", "Robert C. Martin", "978-0132350884");
        assertThat(item).isInstanceOf(Livro.class);
        assertThat(item.getTitulo()).isEqualTo("Código Limpo");
        assertThat(fachada.listarItens()).hasSize(1);
    }

    @Test
    void adicionarDvd_deveCriarDvdNoAcervo() {
        ItemAcervo item = fachada.adicionarDvd("Inception", "Christopher Nolan", 148);
        assertThat(item).isInstanceOf(Dvd.class);
        assertThat(item.getTipo()).isEqualTo("DVD");
        assertThat(fachada.listarItens()).hasSize(1);
    }

    @Test
    void removerItem_deveRemoverDoAcervo() {
        ItemAcervo livro = fachada.adicionarLivro("Livro Teste", "Autor", "123");
        fachada.removerItem(livro.getId());
        assertThat(fachada.listarItens()).isEmpty();
    }

    // ── Gerenciamento de Usuários ────────────────────────────────────────────

    @Test
    void adicionarUsuario_deveCriarUsuario() {
        Usuario usuario = fachada.adicionarUsuario("Maria Silva", "maria@email.com");
        assertThat(usuario.getNome()).isEqualTo("Maria Silva");
        assertThat(fachada.listarUsuarios()).hasSize(1);
    }

    @Test
    void removerUsuario_deveRemoverDoSistema() {
        Usuario usuario = fachada.adicionarUsuario("João", "joao@email.com");
        fachada.removerUsuario(usuario.getId());
        assertThat(fachada.listarUsuarios()).isEmpty();
    }

    // ── Ciclo de Vida do Empréstimo ──────────────────────────────────────────

    @Test
    void realizarEmprestimo_deveCriarEmprestimoEMarcarItemIndisponivel() {
        ItemAcervo livro = fachada.adicionarLivro("Refactoring", "Fowler", "978-0201485677");
        Usuario usuario  = fachada.adicionarUsuario("Ana", "ana@email.com");

        Emprestimo emprestimo = fachada.realizarEmprestimo(livro.getId(), usuario.getId());

        assertThat(emprestimo.getStatus()).isEqualTo(StatusEmprestimo.ATIVO);
        assertThat(livro.isDisponivel()).isFalse();
        assertThat(fachada.listarEmprestimos()).hasSize(1);
    }

    @Test
    void registrarDevolucao_deveMarcarLivroDisponivel() {
        ItemAcervo livro = fachada.adicionarLivro("DDD", "Evans", "978-0321125217");
        Usuario usuario  = fachada.adicionarUsuario("Pedro", "pedro@email.com");
        Emprestimo emprestimo = fachada.realizarEmprestimo(livro.getId(), usuario.getId());

        fachada.registrarDevolucao(emprestimo.getId());

        assertThat(emprestimo.getStatus()).isEqualTo(StatusEmprestimo.DEVOLVIDO);
        assertThat(livro.isDisponivel()).isTrue();
    }

    @Test
    void realizarEmprestimo_deveLancarExcecao_quandoItemIndisponivel() {
        ItemAcervo livro = fachada.adicionarLivro("Livro Ocupado", "Autor", "111");
        Usuario u1 = fachada.adicionarUsuario("User1", "u1@email.com");
        Usuario u2 = fachada.adicionarUsuario("User2", "u2@email.com");
        fachada.realizarEmprestimo(livro.getId(), u1.getId());

        assertThatThrownBy(() -> fachada.realizarEmprestimo(livro.getId(), u2.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void realizarEmprestimo_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        ItemAcervo livro = fachada.adicionarLivro("Livro", "Autor", "222");
        assertThatThrownBy(() -> fachada.realizarEmprestimo(livro.getId(), "usuario-inexistente"))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ── Strategy via Facade ──────────────────────────────────────────────────

    @Test
    void buscarPorTitulo_deveRetornarItensCorrespondentes() {
        fachada.adicionarLivro("Código Limpo", "Martin", "111");
        fachada.adicionarLivro("Arquitetura Limpa", "Martin", "222");
        fachada.adicionarDvd("Inception", "Nolan", 148);

        List<ItemAcervo> resultado = fachada.buscarPorTitulo("limp");
        assertThat(resultado).hasSize(2);
    }

    @Test
    void buscarPorAutor_deveRetornarApenasLivros() {
        fachada.adicionarLivro("Livro A", "Eric Evans", "333");
        fachada.adicionarDvd("DVD B", "Eric Evans Diretor", 90);

        assertThat(fachada.buscarPorAutor("evans")).hasSize(1);
    }

    @Test
    void buscarPorIsbn_deveEncontrarCorrespondenciaExata() {
        fachada.adicionarLivro("DDD", "Evans", "978-0321125217");
        assertThat(fachada.buscarPorIsbn("978-0321125217")).hasSize(1);
    }

    @Test
    void buscarUsuariosPorNome_deveRetornarUsuariosCorrespondentes() {
        fachada.adicionarUsuario("Carlos Becker", "c@email.com");
        fachada.adicionarUsuario("Maria Souza", "m@email.com");
        assertThat(fachada.buscarUsuariosPorNome("becker")).hasSize(1);
    }

    @Test
    void listarVencidos_deveRetornarVazio_quandoSemEmprestimosVencidos() {
        ItemAcervo livro = fachada.adicionarLivro("Livro", "Autor", "999");
        Usuario usuario  = fachada.adicionarUsuario("Usuário", "u@email.com");
        fachada.realizarEmprestimo(livro.getId(), usuario.getId());
        assertThat(fachada.listarVencidos()).isEmpty();
    }
}
