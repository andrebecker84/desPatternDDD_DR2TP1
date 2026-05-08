package com.becker.biblioteca.service;

import com.becker.biblioteca.model.Emprestimo;
import com.becker.biblioteca.model.ItemAcervo;
import com.becker.biblioteca.model.StatusEmprestimo;
import com.becker.biblioteca.model.Usuario;
import com.becker.biblioteca.pattern.factory.FabricaDvd;
import com.becker.biblioteca.pattern.factory.FabricaItem;
import com.becker.biblioteca.pattern.factory.FabricaLivro;
import com.becker.biblioteca.pattern.factory.RequisicaoItem;
import com.becker.biblioteca.pattern.observer.DespachadorEventos;
import com.becker.biblioteca.pattern.strategy.BuscaAutor;
import com.becker.biblioteca.pattern.strategy.BuscaIsbn;
import com.becker.biblioteca.pattern.strategy.BuscaNomeUsuario;
import com.becker.biblioteca.pattern.strategy.BuscaTitulo;
import com.becker.biblioteca.pattern.strategy.ContextoBusca;
import com.becker.biblioteca.repository.RepositorioEmprestimo;
import com.becker.biblioteca.repository.RepositorioItem;
import com.becker.biblioteca.repository.RepositorioUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Facade — único ponto de entrada para operações da biblioteca.
 * Orquestra Factory Method, Observer e Strategy sem expor seus detalhes
 * ao controlador (SRP + baixo acoplamento externo).
 */
@Service
@Transactional
public class FachadaBiblioteca {

    private final RepositorioItem repositorioItem;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioEmprestimo repositorioEmprestimo;
    private final DespachadorEventos despachador;
    private final Map<String, FabricaItem> fabricas;

    public FachadaBiblioteca(RepositorioItem repositorioItem,
                              RepositorioUsuario repositorioUsuario,
                              RepositorioEmprestimo repositorioEmprestimo,
                              DespachadorEventos despachador) {
        this.repositorioItem       = repositorioItem;
        this.repositorioUsuario    = repositorioUsuario;
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.despachador           = despachador;
        this.fabricas = Map.of(
                "LIVRO", new FabricaLivro(),
                "DVD",   new FabricaDvd()
        );
    }

    // ── Acervo ──────────────────────────────────────────────────────────────

    public ItemAcervo adicionarLivro(String titulo, String autor, String isbn) {
        ItemAcervo livro = fabricas.get("LIVRO").criar(
                RequisicaoItem.paraLivro(titulo).autor(autor).isbn(isbn).build());
        return repositorioItem.save(livro);
    }

    public ItemAcervo adicionarDvd(String titulo, String diretor, int duracaoMinutos) {
        ItemAcervo dvd = fabricas.get("DVD").criar(
                RequisicaoItem.paraDvd(titulo).diretor(diretor).duracaoMinutos(duracaoMinutos).build());
        return repositorioItem.save(dvd);
    }

    public void removerItem(String id) {
        repositorioItem.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ItemAcervo> listarItens() { return repositorioItem.findAll(); }

    // ── Usuários ──────────────────────────────────────────────────────────────

    public Usuario adicionarUsuario(String nome, String email) {
        return repositorioUsuario.save(new Usuario(UUID.randomUUID().toString(), nome, email));
    }

    public void removerUsuario(String id) {
        repositorioUsuario.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() { return repositorioUsuario.findAll(); }

    // ── Empréstimos ───────────────────────────────────────────────────────────

    public Emprestimo realizarEmprestimo(String itemId, String usuarioId) {
        ItemAcervo item = repositorioItem.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item não encontrado: " + itemId));
        if (!item.isDisponivel())
            throw new IllegalStateException("Item indisponível para empréstimo: " + itemId);

        Usuario usuario = repositorioUsuario.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado: " + usuarioId));

        item.retirar();
        Emprestimo emprestimo = repositorioEmprestimo.save(
                new Emprestimo(UUID.randomUUID().toString(), item, usuario));
        despachador.notificarCriacao(emprestimo);
        return emprestimo;
    }

    public void registrarDevolucao(String emprestimoId) {
        Emprestimo emprestimo = repositorioEmprestimo.findById(emprestimoId)
                .orElseThrow(() -> new NoSuchElementException("Empréstimo não encontrado: " + emprestimoId));
        emprestimo.registrarDevolucao();
        emprestimo.getItem().devolver();
        despachador.notificarDevolucao(emprestimo);
    }

    public List<Emprestimo> listarVencidos() {
        List<Emprestimo> vencidos = repositorioEmprestimo.buscarPorStatus(StatusEmprestimo.ATIVO).stream()
                .filter(Emprestimo::isVencido)
                .toList();
        vencidos.forEach(despachador::notificarVencimento);
        return vencidos;
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimos() { return repositorioEmprestimo.findAll(); }

    // ── Busca (Strategy) ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ItemAcervo> buscarPorTitulo(String query) {
        return new ContextoBusca<>(new BuscaTitulo()).buscar(query, repositorioItem.findAll());
    }

    @Transactional(readOnly = true)
    public List<ItemAcervo> buscarPorAutor(String query) {
        return new ContextoBusca<>(new BuscaAutor()).buscar(query, repositorioItem.findAll());
    }

    @Transactional(readOnly = true)
    public List<ItemAcervo> buscarPorIsbn(String isbn) {
        return new ContextoBusca<>(new BuscaIsbn()).buscar(isbn, repositorioItem.findAll());
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosPorNome(String query) {
        return new ContextoBusca<>(new BuscaNomeUsuario()).buscar(query, repositorioUsuario.findAll());
    }
}
