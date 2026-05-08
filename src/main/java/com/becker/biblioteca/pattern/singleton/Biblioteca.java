package com.becker.biblioteca.pattern.singleton;

/**
 * Singleton — garante uma única instância durante o ciclo de vida da aplicação.
 * Implementado com Double-Checked Locking + volatile (thread-safe).
 */
public final class Biblioteca {

    private static volatile Biblioteca instancia;

    private Biblioteca() {}

    public static Biblioteca getInstance() {
        if (instancia == null) {
            synchronized (Biblioteca.class) {
                if (instancia == null) {
                    instancia = new Biblioteca();
                }
            }
        }
        return instancia;
    }

    /** Usado exclusivamente em testes para reiniciar o estado entre cenários. */
    public static void resetarParaTeste() {
        synchronized (Biblioteca.class) {
            instancia = null;
        }
    }
}
