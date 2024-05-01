package com.example.gomesrodris.archburgers.domain.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Criação de objetos de data/hora em um serviço isolado para facilitar os testes.
 */
@Component
public class Clock {
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }
}
