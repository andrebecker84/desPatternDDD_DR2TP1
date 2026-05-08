package com.becker.biblioteca.config;

import com.becker.biblioteca.pattern.observer.DespachadorEventos;
import com.becker.biblioteca.pattern.observer.ObservadorAtraso;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoObservadores {

    @Bean
    public CommandLineRunner registrarObservadores(
            DespachadorEventos despachador,
            ObservadorAtraso observadorAtraso) {
        return args -> despachador.inscrever(observadorAtraso);
    }
}
