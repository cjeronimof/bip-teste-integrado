package com.example.backend.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para transferência entre benefícios")
public class TransferenciaDTO {
    @Schema(description = "ID do benefício de origem (que será debitado)", example = "1")
    @NotNull(message = "ID de origem é obrigatório")
    private Long origemId;

    @Schema(description = "ID do benefício de destino (que será creditado)", example = "2")
    @NotNull(message = "ID de destino é obrigatório")
    private Long destinoId;

    @Schema(description = "Valor a ser transferido", example = "100.50")
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;
}
