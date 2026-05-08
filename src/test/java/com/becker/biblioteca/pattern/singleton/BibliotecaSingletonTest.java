package com.becker.biblioteca.pattern.singleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BibliotecaSingletonTest {

    @AfterEach
    void tearDown() {
        Biblioteca.resetarParaTeste();
    }

    @Test
    void getInstance_deveRetornarMesmaInstanciaEmChamadasConsecutivas() {
        Biblioteca primeira  = Biblioteca.getInstance();
        Biblioteca segunda   = Biblioteca.getInstance();
        assertThat(primeira).isSameAs(segunda);
    }

    @Test
    void getInstance_deveSerThreadSafe() throws InterruptedException {
        int totalThreads = 20;
        Set<Biblioteca> instancias = ConcurrentHashMap.newKeySet();
        CountDownLatch latch = new CountDownLatch(totalThreads);

        for (int i = 0; i < totalThreads; i++) {
            new Thread(() -> {
                instancias.add(Biblioteca.getInstance());
                latch.countDown();
            }).start();
        }

        latch.await(5, TimeUnit.SECONDS);
        assertThat(instancias).hasSize(1);
    }
}
