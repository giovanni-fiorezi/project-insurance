package br.com.project.insurance.dto.response;

import br.com.project.insurance.entity.enums.FormaPagamento;
import br.com.project.insurance.entity.enums.SituacaoParcela;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaResponse(Integer id,
                              BigDecimal premio,
                              FormaPagamento formaPagamento,
                              LocalDate dataPagamento,
                              LocalDate dataPago,
                              BigDecimal juros,
                              SituacaoParcela situacao,
                              LocalDate dataCriacao,
                              LocalDate dataAlteracao,
                              Integer usuarioCriacao,
                              Integer usuarioAlteracao) {
}
