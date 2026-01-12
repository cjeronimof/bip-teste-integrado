package com.example.ejb.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Beneficio {

    private Long id;
    private Long version;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private Boolean ativo;

    public Beneficio(Long id, Long version, String nome, String descricao, BigDecimal valor, Boolean ativo) {
        this.id = id;
        this.version = version;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.ativo = ativo;
    }

    public Beneficio(Long id, String nome, String descricao, BigDecimal valor, Boolean ativo) {
        this(id, null, nome, descricao, valor, ativo);
    }

    public void debitar(BigDecimal quantia) {
        if (quantia == null || quantia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do débito deve ser positivo");
        }
        if (!ativo) {
            throw new IllegalStateException("Benefício inativo não pode realizar operações");
        }
        if (this.valor.compareTo(quantia) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.valor = this.valor.subtract(quantia);
    }

    public void creditar(BigDecimal quantia) {
        if (quantia == null || quantia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do crédito deve ser positivo");
        }
        if (!ativo) {
            throw new IllegalStateException("Benefício inativo não pode receber operações");
        }
        this.valor = this.valor.add(quantia);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Beneficio beneficio = (Beneficio) o;
        return Objects.equals(id, beneficio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
