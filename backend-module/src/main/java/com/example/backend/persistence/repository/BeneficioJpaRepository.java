package com.example.backend.persistence.repository;

import com.example.backend.persistence.entity.BeneficioEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BeneficioJpaRepository extends JpaRepository<BeneficioEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM BeneficioEntity b WHERE b.id = :id")
    Optional<BeneficioEntity> findByIdWithLock(@Param("id") Long id);
}
