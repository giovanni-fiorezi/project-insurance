package br.com.project.insurance.dto.request;

import br.com.project.insurance.entity.enums.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaRequest(Integer id,
                             BigDecimal premio,
                             FormaPagamento formaPagamento,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                             LocalDate dataPagamento) {
}
