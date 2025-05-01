package br.com.project.insurance.dto.request;

import br.com.project.insurance.entity.enums.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaRequest(Integer id,
                             BigDecimal premio,
                             FormaPagamento formaPagamento,
                             LocalDate dataPagamento) {
}
