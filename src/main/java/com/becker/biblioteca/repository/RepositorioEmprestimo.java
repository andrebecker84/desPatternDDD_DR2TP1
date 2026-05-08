package com.becker.biblioteca.repository;

import com.becker.biblioteca.model.Emprestimo;
import com.becker.biblioteca.model.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioEmprestimo extends JpaRepository<Emprestimo, String> {

    @Query("SELECT e FROM Emprestimo e WHERE e.status = :status")
    List<Emprestimo> buscarPorStatus(@Param("status") StatusEmprestimo status);
}
