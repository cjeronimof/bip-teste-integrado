package com.example.backend.config;

import com.example.ejb.application.port.out.BeneficioRepositoryPort;
import com.example.ejb.application.service.TransferenciaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public TransferenciaService transferenciaService(BeneficioRepositoryPort beneficioRepositoryPort) {
        return new TransferenciaService(beneficioRepositoryPort);
    }
}
