package com.becker.biblioteca.pattern.strategy;

import java.util.List;

/**
 * Strategy — contrato genérico para algoritmos de busca intercambiáveis.
 * O tipo genérico T permite reutilizar a mesma interface para itens e usuários,
 * eliminando duplicação de lógica (DRY).
 */
public interface EstrategiaBusca<T> {
    List<T> buscar(String query, List<T> lista);
}
