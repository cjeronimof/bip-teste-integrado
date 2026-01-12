package com.example.backend.service;

import com.example.ejb.application.port.in.TransferenciaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class TransferenciaApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(TransferenciaApplicationService.class);
    private final TransferenciaUseCase transferService;

    @Transactional
    public void transferir(Long origemId, Long destinoId, BigDecimal valor) {
        logger.debug("Iniciando transação de transferência. Origem: {}, Destino: {}", origemId, destinoId);
        transferService.transferir(origemId, destinoId, valor);
        logger.debug("Transação de transferência concluída com sucesso.");
    }
}
