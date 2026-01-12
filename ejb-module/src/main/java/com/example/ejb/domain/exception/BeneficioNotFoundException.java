package com.example.ejb.domain.exception;

public class BeneficioNotFoundException extends RuntimeException {
    public BeneficioNotFoundException(Long id) {
        super("Benefício não encontrado com ID: " + id);
    }
}
