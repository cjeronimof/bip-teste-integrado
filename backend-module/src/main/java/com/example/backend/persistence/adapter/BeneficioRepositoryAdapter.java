package com.example.backend.persistence.adapter;

import com.example.ejb.application.port.out.BeneficioRepositoryPort;
import com.example.ejb.domain.Beneficio;
import com.example.backend.persistence.entity.BeneficioEntity;
import com.example.backend.persistence.repository.BeneficioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class BeneficioRepositoryAdapter implements BeneficioRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(BeneficioRepositoryAdapter.class);
    private final BeneficioJpaRepository jpaRepository;

    @Override
    public Optional<Beneficio> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Beneficio> findByIdWithLock(Long id) {
        logger.debug("Buscando benefício com LOCK. ID: {}", id);
        return jpaRepository.findByIdWithLock(id).map(this::toDomain);
    }

    @Override
    public List<Beneficio> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Beneficio save(Beneficio beneficio) {
        BeneficioEntity entity;
        if (beneficio.getId() != null && beneficio.getVersion() != null) {
            entity = toEntity(beneficio);
        } else if (beneficio.getId() != null) {
            entity = jpaRepository.findById(beneficio.getId())
                    .orElseGet(() -> toEntity(beneficio));
            updateEntityFromDomain(entity, beneficio);
        } else {
            entity = toEntity(beneficio);
        }

        BeneficioEntity saved = jpaRepository.save(entity);
        logger.debug("Benefício salvo/atualizado. ID: {}, Version: {}", saved.getId(), saved.getVersion());
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
        logger.debug("Benefício deletado. ID: {}", id);
    }

    private Beneficio toDomain(BeneficioEntity entity) {
        return new Beneficio(
                entity.getId(),
                entity.getVersion(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getAtivo());
    }

    private BeneficioEntity toEntity(Beneficio domain) {
        BeneficioEntity entity = new BeneficioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getValor(),
                domain.getAtivo());

        if (domain.getVersion() != null) {
            entity.setVersion(domain.getVersion());
        }
        return entity;
    }

    private void updateEntityFromDomain(BeneficioEntity entity, Beneficio domain) {
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setValor(domain.getValor());
        entity.setAtivo(domain.getAtivo());
    }
}
