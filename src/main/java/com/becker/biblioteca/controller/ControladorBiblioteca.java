package com.becker.biblioteca.controller;

import com.becker.biblioteca.dto.RequisicaoDvd;
import com.becker.biblioteca.dto.RequisicaoEmprestimo;
import com.becker.biblioteca.dto.RequisicaoLivro;
import com.becker.biblioteca.dto.RequisicaoUsuario;
import com.becker.biblioteca.dto.RespostaEmprestimo;
import com.becker.biblioteca.dto.RespostaItem;
import com.becker.biblioteca.dto.RespostaUsuario;
import com.becker.biblioteca.service.FachadaBiblioteca;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/biblioteca")
public class ControladorBiblioteca {

    private final FachadaBiblioteca fachada;

    public ControladorBiblioteca(FachadaBiblioteca fachada) {
        this.fachada = fachada;
    }

    // ── Acervo ──────────────────────────────────────────────────────────────

    @PostMapping("/livros")
    public ResponseEntity<RespostaItem> adicionarLivro(@Valid @RequestBody RequisicaoLivro req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RespostaItem.de(fachada.adicionarLivro(req.titulo(), req.autor(), req.isbn())));
    }

    @PostMapping("/dvds")
    public ResponseEntity<RespostaItem> adicionarDvd(@Valid @RequestBody RequisicaoDvd req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RespostaItem.de(fachada.adicionarDvd(req.titulo(), req.diretor(), req.duracaoMinutos())));
    }

    @DeleteMapping("/itens/{id}")
    public ResponseEntity<Void> removerItem(@PathVariable String id) {
        fachada.removerItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/itens")
    public ResponseEntity<List<RespostaItem>> listarItens() {
        return ResponseEntity.ok(fachada.listarItens().stream().map(RespostaItem::de).toList());
    }

    @GetMapping("/itens/busca/titulo")
    public ResponseEntity<List<RespostaItem>> buscarPorTitulo(@RequestParam String q) {
        return ResponseEntity.ok(fachada.buscarPorTitulo(q).stream().map(RespostaItem::de).toList());
    }

    @GetMapping("/itens/busca/autor")
    public ResponseEntity<List<RespostaItem>> buscarPorAutor(@RequestParam String q) {
        return ResponseEntity.ok(fachada.buscarPorAutor(q).stream().map(RespostaItem::de).toList());
    }

    @GetMapping("/itens/busca/isbn")
    public ResponseEntity<List<RespostaItem>> buscarPorIsbn(@RequestParam String q) {
        return ResponseEntity.ok(fachada.buscarPorIsbn(q).stream().map(RespostaItem::de).toList());
    }

    // ── Usuários ──────────────────────────────────────────────────────────────

    @PostMapping("/usuarios")
    public ResponseEntity<RespostaUsuario> adicionarUsuario(@Valid @RequestBody RequisicaoUsuario req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RespostaUsuario.de(fachada.adicionarUsuario(req.nome(), req.email())));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable String id) {
        fachada.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<RespostaUsuario>> listarUsuarios() {
        return ResponseEntity.ok(fachada.listarUsuarios().stream().map(RespostaUsuario::de).toList());
    }

    @GetMapping("/usuarios/busca")
    public ResponseEntity<List<RespostaUsuario>> buscarUsuarios(@RequestParam String q) {
        return ResponseEntity.ok(fachada.buscarUsuariosPorNome(q).stream().map(RespostaUsuario::de).toList());
    }

    // ── Empréstimos ───────────────────────────────────────────────────────────

    @PostMapping("/emprestimos")
    public ResponseEntity<RespostaEmprestimo> realizarEmprestimo(@Valid @RequestBody RequisicaoEmprestimo req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RespostaEmprestimo.de(fachada.realizarEmprestimo(req.itemId(), req.usuarioId())));
    }

    @PutMapping("/emprestimos/{id}/devolucao")
    public ResponseEntity<Void> registrarDevolucao(@PathVariable String id) {
        fachada.registrarDevolucao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/emprestimos")
    public ResponseEntity<List<RespostaEmprestimo>> listarEmprestimos() {
        return ResponseEntity.ok(fachada.listarEmprestimos().stream().map(RespostaEmprestimo::de).toList());
    }

    @GetMapping("/emprestimos/vencidos")
    public ResponseEntity<List<RespostaEmprestimo>> listarVencidos() {
        return ResponseEntity.ok(fachada.listarVencidos().stream().map(RespostaEmprestimo::de).toList());
    }
}
