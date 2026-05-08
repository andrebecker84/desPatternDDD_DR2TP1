package com.becker.biblioteca.repository;

import com.becker.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioUsuario extends JpaRepository<Usuario, String> {
}
