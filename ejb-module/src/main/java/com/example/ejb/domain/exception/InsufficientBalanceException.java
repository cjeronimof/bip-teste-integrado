package com.example.ejb.domain.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Long id, BigDecimal saldoAtual, BigDecimal valorSolicitado) {
        super(String.format("Saldo insuficiente no benefício %d. Atual: %s, Solicitado: %s", id, saldoAtual,
                valorSolicitado));
    }
}
