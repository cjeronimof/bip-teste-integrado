package com.example.ejb.application.port.out;

import com.example.ejb.domain.Beneficio;
import java.util.List;
import java.util.Optional;

public interface BeneficioRepositoryPort {
    Optional<Beneficio> findById(Long id);

    Optional<Beneficio> findByIdWithLock(Long id);

    List<Beneficio> findAll();

    Beneficio save(Beneficio beneficio);

    void deleteById(Long id);
}
