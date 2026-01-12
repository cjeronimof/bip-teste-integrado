package com.example.ejb.application.service;

import com.example.ejb.application.port.in.TransferenciaUseCase;
import com.example.ejb.application.port.out.BeneficioRepositoryPort;
import com.example.ejb.domain.Beneficio;
import com.example.ejb.domain.exception.BeneficioNotFoundException;
import com.example.ejb.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//BeneficioEjbService
public class TransferenciaService implements TransferenciaUseCase {

    private static final Logger logger = LoggerFactory.getLogger(TransferenciaService.class);
    private final BeneficioRepositoryPort beneficioRepository;

    public TransferenciaService(BeneficioRepositoryPort beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    @Override
    public void transferir(Long origemId, Long destinoId, BigDecimal valor) {
        logger.debug("Iniciando transferência. Origem: {}, Destino: {}, Valor: {}", origemId, destinoId, valor);

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Tentativa de transferência com valor inválido: {}", valor);
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero");
        }

        if (origemId == null || destinoId == null) {
            logger.error("IDs inválidos. Origem: {}, Destino: {}", origemId, destinoId);
            throw new IllegalArgumentException("IDs de origem e destino não podem ser nulos");
        }

        if (origemId.equals(destinoId)) {
            logger.error("Tentativa de transferência para a mesma conta. ID: {}", origemId);
            throw new IllegalArgumentException("Não é possível transferir para o mesmo benefício");
        }

        Beneficio origem = beneficioRepository.findByIdWithLock(origemId)
                .orElseThrow(() -> {
                    logger.error("Benefício de origem não encontrado: {}", origemId);
                    return new BeneficioNotFoundException(origemId);
                });

        Beneficio destino = beneficioRepository.findByIdWithLock(destinoId)
                .orElseThrow(() -> {
                    logger.error("Benefício de destino não encontrado: {}", destinoId);
                    return new BeneficioNotFoundException(destinoId);
                });

        try {
            origem.debitar(valor);
        } catch (IllegalStateException e) {
            logger.error("Falha ao debitar origem {}: {}", origemId, e.getMessage());
            if (e.getMessage().contains("Saldo insuficiente")) {
                throw new InsufficientBalanceException(origemId, origem.getValor(), valor);
            }
            throw e;
        }

        destino.creditar(valor);

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        logger.info("Transferência realizada com sucesso. Origem: {}, Destino: {}, Valor: {}", origemId, destinoId,
                valor);
    }
}
