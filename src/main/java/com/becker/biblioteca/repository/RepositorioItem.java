package com.becker.biblioteca.repository;

import com.becker.biblioteca.model.ItemAcervo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioItem extends JpaRepository<ItemAcervo, String> {
}
