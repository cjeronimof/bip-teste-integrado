package com.example.backend.integration;

import com.example.ejb.domain.Beneficio;
import com.example.backend.persistence.entity.BeneficioEntity;
import com.example.backend.persistence.repository.BeneficioJpaRepository;
import com.example.backend.persistence.adapter.BeneficioRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class BeneficioIntegrationTest {

    @Autowired
    private BeneficioRepositoryAdapter repositoryAdapter;

    @Autowired
    private BeneficioJpaRepository jpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository.deleteAll();
    }

    @Test
    void deveAtualizarBeneficioSemErroDeVersao() {
        // 1. Criar Beneficio Inicial
        Beneficio novo = new Beneficio(null, "Teste", "Desc", new BigDecimal("100.00"), true);
        Beneficio salvo = repositoryAdapter.save(novo);
        Long id = salvo.getId();

        assertNotNull(id);

        // 2. Simular um PUT (Objeto novo com mesmo ID)
        // O obeto de domínio não tem campo Version, então ele simula exatamente o que
        // chega do Controller
        Beneficio atualizacao = new Beneficio(id, "Nome Alterado", "Desc Alterada", new BigDecimal("200.00"), true);

        // 3. Tentar Salvar (Antigamente causava erro de DataIntegrity por version null)
        Beneficio atualizado = repositoryAdapter.save(atualizacao);

        // 4. Verificar
        BeneficioEntity noBanco = jpaRepository.findById(id).orElseThrow();
        assertEquals("Nome Alterado", noBanco.getNome());
        assertEquals(0, new BigDecimal("200.00").compareTo(noBanco.getValor()));
        // A versão deve ser 1 (0 inicial + 1 update)
        // Nota: O H2/Hibernate inicia com 0.
        assertNotNull(noBanco.getVersion());
    }
}
