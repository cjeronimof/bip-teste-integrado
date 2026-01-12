package com.example.ejb.application.service;

import com.example.ejb.application.port.out.BeneficioRepositoryPort;
import com.example.ejb.domain.Beneficio;
import com.example.ejb.domain.exception.BeneficioNotFoundException;
import com.example.ejb.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private BeneficioRepositoryPort repository;

    @InjectMocks
    private TransferenciaService service;

    private Beneficio origem;
    private Beneficio destino;

    @BeforeEach
    void setup() {
        origem = new Beneficio(1L, "Origem", "Desc", new BigDecimal("1000.00"), true);
        destino = new Beneficio(2L, "Destino", "Desc", new BigDecimal("500.00"), true);
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso")
    void deveTransferirComSucesso() {
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.of(origem));
        when(repository.findByIdWithLock(2L)).thenReturn(Optional.of(destino));

        service.transferir(1L, 2L, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("900.00"), origem.getValor());
        assertEquals(new BigDecimal("600.00"), destino.getValor());

        verify(repository).save(origem);
        verify(repository).save(destino);
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo insuficiente")
    void deveFalharSaldoInsuficiente() {
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.of(origem));
        when(repository.findByIdWithLock(2L)).thenReturn(Optional.of(destino));

        assertThrows(InsufficientBalanceException.class, () -> service.transferir(1L, 2L, new BigDecimal("1500.00")));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando benefício inativo")
    void deveFalharBeneficioInativo() {
        origem.setAtivo(false);
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.of(origem));
        when(repository.findByIdWithLock(2L)).thenReturn(Optional.of(destino));

        assertThrows(IllegalStateException.class, () -> service.transferir(1L, 2L, new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Deve lançar exceção quando origem não encontrada")
    void deveFalharOrigemNaoEncontrada() {
        when(repository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        assertThrows(BeneficioNotFoundException.class, () -> service.transferir(1L, 2L, new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Deve validar parâmetros inválidos")
    void deveValidarParametros() {
        assertThrows(IllegalArgumentException.class, () -> service.transferir(null, 2L, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, null, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 1L, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 2L, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 2L, new BigDecimal("-10")));
    }
}
