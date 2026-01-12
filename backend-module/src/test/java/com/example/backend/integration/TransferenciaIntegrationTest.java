package com.example.backend.integration;

import com.example.ejb.domain.Beneficio;
import com.example.backend.persistence.entity.BeneficioEntity;
import com.example.backend.persistence.repository.BeneficioJpaRepository;
import com.example.backend.service.TransferenciaApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class TransferenciaIntegrationTest {

    @Autowired
    private TransferenciaApplicationService transferenciaService;

    @Autowired
    private BeneficioJpaRepository beneficioRepository;

    private Long origemId;
    private Long destinoId;

    @BeforeEach
    void setUp() {
        beneficioRepository.deleteAll();

        BeneficioEntity origem = new BeneficioEntity(null, "Origem", "Desc", new BigDecimal("1000.00"), true);
        origem = beneficioRepository.save(origem);
        origemId = origem.getId();

        BeneficioEntity destino = new BeneficioEntity(null, "Destino", "Desc", new BigDecimal("500.00"), true);
        destino = beneficioRepository.save(destino);
        destinoId = destino.getId();
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        // Act
        transferenciaService.transferir(origemId, destinoId, new BigDecimal("100.00"));

        // Assert
        BeneficioEntity origemAtualizada = beneficioRepository.findById(origemId).orElseThrow();
        BeneficioEntity destinoAtualizado = beneficioRepository.findById(destinoId).orElseThrow();

        assertEquals(0, new BigDecimal("900.00").compareTo(origemAtualizada.getValor()));
        assertEquals(0, new BigDecimal("600.00").compareTo(destinoAtualizado.getValor()));
    }
}
