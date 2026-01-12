package com.example.backend.web.controller;

import com.example.ejb.application.port.in.TransferenciaUseCase;
import com.example.ejb.application.port.out.BeneficioRepositoryPort;
import com.example.ejb.domain.Beneficio;
import com.example.backend.web.dto.BeneficioDTO;
import com.example.backend.web.dto.TransferenciaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/beneficios")
@RequiredArgsConstructor
@Tag(name = "Benefícios", description = "Gestão de Benefícios e Transferências")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BeneficioController {

    private static final Logger logger = LoggerFactory.getLogger(BeneficioController.class);

    private final BeneficioRepositoryPort beneficioRepository;
    private final com.example.backend.service.TransferenciaApplicationService transferenciaService;

    @GetMapping
    @Operation(summary = "Listar benefícios", description = "Retorna todos os benefícios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<BeneficioDTO> findAll() {
        logger.info("Solicitação para listar todos os benefícios");
        return beneficioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um benefício específico")
    @ApiResponse(responseCode = "200", description = "Benefício encontrado")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    public ResponseEntity<BeneficioDTO> findById(@PathVariable("id") Long id) {
        logger.info("Buscando benefício por ID: {}", id);
        return beneficioRepository.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Benefício não encontrado: {}", id);
                    return ResponseEntity.noContent().build();
                });
    }

    @PostMapping
    @Operation(summary = "Criar benefício", description = "Cadastra um novo benefício")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    public ResponseEntity<BeneficioDTO> create(@RequestBody @Valid BeneficioDTO dto) {
        logger.info("Criando novo benefício: {}", dto.getNome());
        Beneficio domain = toDomain(dto);
        domain.setId(null);
        Beneficio saved = beneficioRepository.save(domain);
        logger.info("Benefício criado com ID: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício", description = "Atualiza os dados de um benefício existente")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    public ResponseEntity<BeneficioDTO> update(@PathVariable("id") Long id, @RequestBody @Valid BeneficioDTO dto) {
        logger.info("Atualizando benefício ID: {}", id);
        return beneficioRepository.findById(id)
                .map(existing -> {
                    Beneficio updateDomain = toDomain(dto);
                    updateDomain.setId(id);
                    Beneficio saved = beneficioRepository.save(updateDomain);
                    logger.info("Benefício ID {} atualizado com sucesso", id);
                    return ResponseEntity.ok(toDTO(saved));
                })
                .orElseGet(() -> {
                    logger.warn("Tentativa de atualização falhou. Benefício não encontrado: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar benefício", description = "Remove um benefício")
    @ApiResponse(responseCode = "204", description = "Removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        logger.info("Solicitação para deletar benefício ID: {}", id);
        if (beneficioRepository.findById(id).isPresent()) {
            beneficioRepository.deleteById(id);
            logger.info("Benefício ID {} removido", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Tentativa de deleção falhou. Benefício não encontrado: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Realizar transferência", description = "Transfere valores entre benefícios. Operação atômica e segura.")
    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de negócio (Saldo insuficiente, Mesma conta) ou Validação")
    @ApiResponse(responseCode = "404", description = "Benefício de origem ou destino não encontrado")
    @ApiResponse(responseCode = "409", description = "Conflito de estado (Benefício inativo)")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferenciaDTO dto) {
        logger.info("Iniciando transferência. Origem: {}, Destino: {}, Valor: {}", dto.getOrigemId(),
                dto.getDestinoId(), dto.getValor());
        transferenciaService.transferir(dto.getOrigemId(), dto.getDestinoId(), dto.getValor());
        logger.info("Transferência completada com sucesso");
        return ResponseEntity.ok().build();
    }

    private BeneficioDTO toDTO(Beneficio domain) {
        return BeneficioDTO.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .descricao(domain.getDescricao())
                .valor(domain.getValor())
                .ativo(domain.getAtivo())
                .build();
    }

    private Beneficio toDomain(BeneficioDTO dto) {
        return new Beneficio(
                dto.getId(),
                dto.getNome(),
                dto.getDescricao(),
                dto.getValor(),
                dto.getAtivo());
    }
}
