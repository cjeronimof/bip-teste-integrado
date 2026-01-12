package com.example.ejb.application.port.in;

import java.math.BigDecimal;

public interface TransferenciaUseCase {
    void transferir(Long origemId, Long destinoId, BigDecimal valor);
}
